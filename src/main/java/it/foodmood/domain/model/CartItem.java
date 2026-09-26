package it.foodmood.domain.model;

import java.util.Objects;
import java.util.UUID;

import it.foodmood.domain.value.Money;

public class CartItem {
    
    private final UUID dishId;
    private final String productName;
    private final Money unitPrice;
    private final int quantity;

    public CartItem(UUID dishId, String productName, Money unitPrice, int quantity) {
        Objects.requireNonNull(dishId, "L'ID del prodotto non può essere nullo");
        Objects.requireNonNull(productName, "Il nome del prodotto non può essere nullo");
        Objects.requireNonNull(unitPrice, "Il prezzo non può essere nullo");
        if(productName.isBlank()) throw new IllegalArgumentException("Nome prodotto vuoto");
        if(!unitPrice.isPositive()) throw new IllegalArgumentException("Il prezzo deve essere positivo");
        if(quantity <= 0) throw new IllegalArgumentException("La quantità deve essere maggiore di zero");

        this.dishId = dishId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public UUID getDishId(){
        return dishId;
    }

    public String getProductName(){
        return productName;
    }

    public Money getUnitPrice(){
        return unitPrice;
    }

    public int getQuantity(){
        return quantity;
    }

    public Money getSubtotal(){
        return unitPrice.multiply(quantity);
    }
}
