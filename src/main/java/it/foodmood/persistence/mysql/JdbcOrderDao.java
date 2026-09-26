package it.foodmood.persistence.mysql;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import it.foodmood.config.JdbcConnectionManager;
import it.foodmood.domain.model.Order;
import it.foodmood.domain.model.OrderLine;
import it.foodmood.domain.value.Money;
import it.foodmood.domain.value.OrderStatus;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.OrderDao;

public class JdbcOrderDao implements OrderDao {
        
    private static final String CALL_INSERT_ORDER = "{CALL insert_customer_order(?,?,?,?)}";
    private static final String CALL_INSERT_ORDER_LINE = "{CALL insert_customer_order_line(?,?,?,?,?)}";
    private static final String CALL_GET_ORDER_BY_ID = "{CALL get_order_by_id(?)}";

    public JdbcOrderDao(){
        // costruttore 
    }

    @Override
    public void insert(Order order){
        try {
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try (CallableStatement cs = conn.prepareCall(CALL_INSERT_ORDER)){
                cs.setString(1, order.getId().toString());
                cs.setString(2, order.getUserId().toString());
                cs.setString(3, order.getTableSessionId().toString());
                cs.setString(4, order.getStatus().name());
                cs.execute();

                insertOrderLine(order, conn);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private void insertOrderLine(Order order, Connection conn) throws SQLException{
        try (CallableStatement cs = conn.prepareCall(CALL_INSERT_ORDER_LINE)){
            for(OrderLine orderLine : order.getOrderLines()){
                cs.setString(1, order.getId().toString());
                cs.setString(2, orderLine.getDishId().toString());
                cs.setString(3, orderLine.getProductName());
                cs.setBigDecimal(4, orderLine.getUnitPrice().getAmount());
                cs.setInt(5, orderLine.getQuantity());
                cs.addBatch();
            }
            cs.executeBatch();
        }
    }

    @Override
    public Optional<Order> findById(UUID id){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            return executeFindById(conn, id);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private Optional<Order> executeFindById(Connection conn, UUID id) throws SQLException{
        try (CallableStatement cs = conn.prepareCall(CALL_GET_ORDER_BY_ID)){
            cs.setString(1, id.toString());
            try(ResultSet rs = cs.executeQuery()){
                if(!rs.next()) return Optional.empty();

                Order order = mapRowToOrder(rs);
                return Optional.of(order);
            }
        }
    }

    @Override
    public List<Order> findAll(){
        // Funzionalità non implementata
        return List.of();
    }

    @Override
    public void deleteById(UUID id){
        // Funzionalità non implementata
    }

    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        UUID orderId = UUID.fromString(rs.getString("id"));
        UUID userId = UUID.fromString(rs.getString("user_id"));
        UUID tableSessionId = UUID.fromString(rs.getString("table_session_id"));
        OrderStatus status = OrderStatus.valueOf(rs.getString("status"));

        List<OrderLine> lines = new ArrayList<>();
        do{
            lines.add(mapRowToOrderLine(rs));
        } while (rs.next());

        return new Order(orderId, userId, tableSessionId, status, lines);
    }

    private OrderLine mapRowToOrderLine(ResultSet rs) throws SQLException {
        UUID dishId = UUID.fromString(rs.getString("dish_id"));
        String productName = rs.getString("product_name");
        BigDecimal price = rs.getBigDecimal("unit_price");
        Money unitPrice = new Money(price);
        int quantity = rs.getInt("quantity");

        return new OrderLine(dishId, productName, unitPrice, quantity);
    }
}
