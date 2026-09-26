package it.foodmood.persistence.mysql;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import it.foodmood.config.JdbcConnectionManager;
import it.foodmood.domain.model.Cart;
import it.foodmood.domain.model.CartItem;
import it.foodmood.domain.value.Money;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.CartDao;

public class JdbcCartDao implements CartDao {

    private static final String CALL_INSERT_CART = "{CALL insert_cart(?,?,?,?)}";
    private static final String CALL_GET_CART_BY_ID = "{CALL get_cart_by_id(?)}";
    private static final String CALL_GET_CART_BY_ACTOR_AND_TABLE_SESSION = "{CALL get_cart_by_actor_and_table_session(?,?)}";
    private static final String CALL_GET_ALL_CARTS = "{CALL get_all_carts()}";
    private static final String CALL_UPDATE_CART = "{CALL update_cart(?,?)}";
    private static final String CALL_DELETE_CART_BY_ID = "{CALL delete_cart_by_id(?)}";

    public JdbcCartDao() {
        // Costruttore
    }

    @Override
    public void insert(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Il carrello non può essere nullo");
        }

        try {
            Connection conn = JdbcConnectionManager.getInstance().getConnection();

            try (CallableStatement cs = conn.prepareCall(CALL_INSERT_CART)) {
                cs.setString(1, cart.getId().toString());
                cs.setString(2, cart.getActorId().toString());
                cs.setString(3, cart.getTableSessionId().toString());
                cs.setString(4, toItemsJson(cart.getItems()));
                cs.execute();
            }
        } catch (SQLException e) {
            throw new PersistenceException("Errore durante l'inserimento del carrello", e);
        }
    }

    @Override
    public Optional<Cart> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }

        try {
            Connection conn = JdbcConnectionManager.getInstance().getConnection();

            try (CallableStatement cs = conn.prepareCall(CALL_GET_CART_BY_ID)) {
                cs.setString(1, id.toString());

                try (ResultSet rs = cs.executeQuery()) {
                    List<Cart> carts = mapResultSetToCarts(rs);

                    if (carts.isEmpty()) {
                        return Optional.empty();
                    }

                    return Optional.of(carts.get(0));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Errore durante il recupero del carrello", e);
        }
    }

    @Override
    public Optional<Cart> findByActorAndTableSession(UUID actorId, UUID tableSessionId) {
        if (actorId == null || tableSessionId == null) {
            return Optional.empty();
        }

        try {
            Connection conn = JdbcConnectionManager.getInstance().getConnection();

            try (CallableStatement cs = conn.prepareCall(CALL_GET_CART_BY_ACTOR_AND_TABLE_SESSION)) {
                cs.setString(1, actorId.toString());
                cs.setString(2, tableSessionId.toString());

                try (ResultSet rs = cs.executeQuery()) {
                    List<Cart> carts = mapResultSetToCarts(rs);

                    if (carts.isEmpty()) {
                        return Optional.empty();
                    }

                    return Optional.of(carts.get(0));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Errore durante il recupero del carrello", e);
        }
    }

    @Override
    public List<Cart> findAll() {
        try {
            Connection conn = JdbcConnectionManager.getInstance().getConnection();

            try (CallableStatement cs = conn.prepareCall(CALL_GET_ALL_CARTS);
                 ResultSet rs = cs.executeQuery()) {
                return mapResultSetToCarts(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Errore durante il recupero dei carrelli", e);
        }
    }

    @Override
    public void update(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Il carrello non può essere nullo");
        }

        try {
            Connection conn = JdbcConnectionManager.getInstance().getConnection();

            try (CallableStatement cs = conn.prepareCall(CALL_UPDATE_CART)) {
                cs.setString(1, cart.getId().toString());
                cs.setString(2, toItemsJson(cart.getItems()));
                cs.execute();
            }
        } catch (SQLException e) {
            throw new PersistenceException("Errore durante l'aggiornamento del carrello", e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        if (id == null) {
            return;
        }

        try {
            Connection conn = JdbcConnectionManager.getInstance().getConnection();

            try (CallableStatement cs = conn.prepareCall(CALL_DELETE_CART_BY_ID)) {
                cs.setString(1, id.toString());
                cs.execute();
            }
        } catch (SQLException e) {
            throw new PersistenceException("Errore durante l'eliminazione del carrello", e);
        }
    }

    private List<Cart> mapResultSetToCarts(ResultSet rs) throws SQLException {
        Map<UUID, CartData> carts = new LinkedHashMap<>();

        while (rs.next()) {
            UUID cartId = UUID.fromString(rs.getString("id"));
            CartData data = carts.get(cartId);

            if (data == null) {
                UUID actorId = UUID.fromString(rs.getString("actor_id"));
                UUID tableSessionId = UUID.fromString(rs.getString("table_session_id"));
                data = new CartData(actorId, tableSessionId);
                carts.put(cartId, data);
            }

            String dishIdValue = rs.getString("dish_id");

            if (dishIdValue != null) {
                UUID dishId = UUID.fromString(dishIdValue);
                String productName = rs.getString("product_name");
                BigDecimal price = rs.getBigDecimal("unit_price");
                int quantity = rs.getInt("quantity");

                CartItem item = new CartItem(
                    dishId,
                    productName,
                    new Money(price),
                    quantity
                );

                data.items.add(item);
            }
        }

        List<Cart> result = new ArrayList<>();

        for (Map.Entry<UUID, CartData> entry : carts.entrySet()) {
            UUID cartId = entry.getKey();
            CartData data = entry.getValue();

            result.add(
                Cart.fromPersistence(
                    cartId,
                    data.actorId,
                    data.tableSessionId,
                    data.items
                )
            );
        }

        return result;
    }

    private String toItemsJson(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);

            if (i > 0) {
                builder.append(",");
            }

            builder.append("{")
                .append("\"dishId\":\"").append(item.getDishId()).append("\",")
                .append("\"productName\":\"").append(escapeJson(item.getProductName())).append("\",")
                .append("\"unitPrice\":").append(item.getUnitPrice().getAmount()).append(",")
                .append("\"quantity\":").append(item.getQuantity())
                .append("}");
        }

        builder.append("]");
        return builder.toString();
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static class CartData {

        private final UUID actorId;
        private final UUID tableSessionId;
        private final List<CartItem> items;

        private CartData(UUID actorId, UUID tableSessionId) {
            this.actorId = actorId;
            this.tableSessionId = tableSessionId;
            this.items = new ArrayList<>();
        }
    }
}