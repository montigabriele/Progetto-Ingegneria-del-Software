package it.foodmood.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import it.foodmood.domain.value.Money;

public class Cart {

    private final UUID id;
    private final UUID actorId;
    private final UUID tableSessionId;
    private final List<CartItem> items;

    private Cart(UUID id,UUID actorId,UUID tableSessionId,List<CartItem> items) {
        this.id = Objects.requireNonNull(id,"L'ID del carrello non può essere nullo");
        this.actorId = Objects.requireNonNull(actorId,"L'ID dell'attore non può essere nullo");
        this.tableSessionId = Objects.requireNonNull(tableSessionId,"L'ID della sessione tavolo non può essere nullo");

        Objects.requireNonNull(items,"La lista degli elementi del carrello non può essere nulla");

        this.items = new ArrayList<>();

        for (CartItem item : items) {
            this.items.add(Objects.requireNonNull(item,"Un elemento del carrello non può essere nullo"));
        }
    }

    public static Cart create(UUID actorId,UUID tableSessionId) {
        return new Cart(UUID.randomUUID(),actorId,tableSessionId,List.of());
    }

    public static Cart fromPersistence(UUID id,UUID actorId,UUID tableSessionId,List<CartItem> items) {
        return new Cart(id,actorId,tableSessionId,items);
    }

    public UUID getId() {
        return id;
    }

    public UUID getActorId() {
        return actorId;
    }

    public UUID getTableSessionId() {
        return tableSessionId;
    }

    public void addLine(UUID dishId,String productName,Money unitPrice,int quantity) {
        Objects.requireNonNull(dishId,"L'ID del piatto non può essere nullo");
        Objects.requireNonNull(productName,"Il nome del prodotto non può essere nullo");
        Objects.requireNonNull(unitPrice,"Il prezzo non può essere nullo");

        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantità deve essere maggiore di zero");
        }

        for (int i = 0; i < items.size(); i++) {
            CartItem existing = items.get(i);

            if (existing.getDishId().equals(dishId)) {
                int newQuantity = existing.getQuantity() + quantity;

                items.set( i,new CartItem(dishId,productName,unitPrice,newQuantity));
                return;
            }
        }

        items.add( new CartItem( dishId, productName, unitPrice, quantity));
    }

    public void removeLine(UUID dishId) {
        Objects.requireNonNull(dishId,"L'ID del piatto non può essere nullo");

        items.removeIf(item -> item.getDishId().equals(dishId));
    }

    public List<CartItem> getItems() {
        return List.copyOf(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    public BigDecimal getTotal() {

        Money total = Money.zero();

        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }

        return total.getAmount();
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof Cart other)) {
            return false;
        }

        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}