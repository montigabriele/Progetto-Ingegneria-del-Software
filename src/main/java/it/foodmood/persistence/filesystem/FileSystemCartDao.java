package it.foodmood.persistence.filesystem;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import it.foodmood.domain.model.Cart;
import it.foodmood.domain.model.CartItem;
import it.foodmood.domain.value.Money;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.CartDao;

public class FileSystemCartDao extends AbstractCsvDao implements CartDao {

    private static final String SEPARATOR = ";";

    private static final String ITEM_SEPARATOR_REGEX = "\\|";
    private static final String ITEM_SEPARATOR = "|";

    private static final String FIELD_SEPARATOR = ",";

    public FileSystemCartDao() {
        super(FileSystemPaths.CARTS);
    }

    @Override
    public synchronized void insert(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Il carrello non può essere nullo");
        }
        if (findById(cart.getId()).isPresent()) {
            throw new PersistenceException("Esiste già un carrello con id: " + cart.getId());
        }
        if (findByActorAndTableSession(
                cart.getActorId(),
                cart.getTableSessionId()
        ).isPresent()) {
            throw new PersistenceException( "Esiste già un carrello per l'attore " + cart.getActorId() + " nella sessione tavolo " + cart.getTableSessionId() );
        }
        appendLine(toCsv(cart));
    }

    @Override
    public Optional<Cart> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return findAll().stream().filter(cart -> cart.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Cart> findByActorAndTableSession( UUID actorId, UUID tableSessionId ) {
        if (actorId == null || tableSessionId == null) {
            return Optional.empty();
        }
        return findAll().stream().filter(cart -> cart.getActorId().equals(actorId) && cart.getTableSessionId().equals(tableSessionId)).findFirst();
    }

    @Override
    public List<Cart> findAll() {
        List<String> lines = readAllLines();
        List<Cart> carts = new ArrayList<>();
        for (String line : lines) {
            if (!line.isBlank()) {
                carts.add(fromCsv(line));
            }
        }
        return carts;
    }

    @Override
    public synchronized void update(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException(
                    "Il carrello non può essere nullo"
            );
        }
        List<Cart> carts = findAll();
        boolean found = false;

        for (int i = 0; i < carts.size(); i++) {
            Cart existing = carts.get(i);
            if (existing.getId().equals(cart.getId())) {
                carts.set(i, cart);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new PersistenceException( "Carrello non trovato: " + cart.getId());
        }
        List<String> lines = carts.stream().map(this::toCsv).toList();
        overwriteAllLines(lines);
    }

    @Override
    public synchronized void deleteById(UUID id) {
        if (id == null) {
            return;
        }
        List<Cart> carts = findAll();
        boolean removed = carts.removeIf(cart -> cart.getId().equals(id));
        if (!removed) {
            return;
        }
        List<String> lines = carts.stream().map(this::toCsv).toList();
        overwriteAllLines(lines);
    }

    private String toCsv(Cart cart) {
        String serializedItems = cartItemsToString( cart.getItems());
        return cart.getId() + SEPARATOR + cart.getActorId() + SEPARATOR + cart.getTableSessionId() + SEPARATOR + serializedItems;
    }

    private Cart fromCsv(String line) {

        String[] tokens = line.split(SEPARATOR, -1);
        if (tokens.length != 4) {
            throw new PersistenceException( "Riga carrello malformata: " + line);
        }

        try {
            UUID id = UUID.fromString( tokens[0].trim());
            UUID actorId = UUID.fromString( tokens[1].trim());
            UUID tableSessionId = UUID.fromString( tokens[2].trim());
            List<CartItem> items = parseCartItems(tokens[3]);
            return Cart.fromPersistence( id, actorId, tableSessionId, items );
        } catch (IllegalArgumentException e) {
            throw new PersistenceException( "Errore durante il parsing della riga: " + line, e);
        }
    }

    private String cartItemsToString( List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        List<String> serializedItems = new ArrayList<>();

        for (CartItem item : items) {
            String encodedProductName = Base64.getEncoder().encodeToString(item.getProductName().getBytes(StandardCharsets.UTF_8));
            String serialized = item.getDishId() + FIELD_SEPARATOR + encodedProductName + FIELD_SEPARATOR + item.getUnitPrice().getAmount() + FIELD_SEPARATOR + item.getQuantity();
            serializedItems.add(serialized);
        }
        return String.join( ITEM_SEPARATOR,serializedItems);
    }

    private List<CartItem> parseCartItems( String field ) {

        List<CartItem> items = new ArrayList<>();

        if (field == null || field.isBlank()) {
            return items;
        }

        String[] tokens = field.split( ITEM_SEPARATOR_REGEX,-1);

        for (String token : tokens) {

            if (token.isBlank()) {
                continue;
            }
            String[] itemFields = token.split( FIELD_SEPARATOR, -1);
            if (itemFields.length != 4) {
                throw new IllegalArgumentException( "Elemento del carrello malformato: " + token);
            }
            UUID dishId = UUID.fromString( itemFields[0].trim());
            String productName = new String( Base64.getDecoder().decode(itemFields[1].trim()),StandardCharsets.UTF_8);
            Money unitPrice = new Money( new BigDecimal(itemFields[2].trim()));
            int quantity = Integer.parseInt(itemFields[3].trim());
            items.add( new CartItem( dishId, productName, unitPrice, quantity));
        }
        return items;
    }
}