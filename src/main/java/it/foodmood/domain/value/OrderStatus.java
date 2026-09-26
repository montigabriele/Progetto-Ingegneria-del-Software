package it.foodmood.domain.value;

public enum OrderStatus {
    OPEN("APERTO"),
    CONFIRMED("CONFERMATO"),
    SERVED("SERVITO"),
    CANCELLED("CANCELLATO");

    private final String description;

    OrderStatus(String description){
        this.description = description;
    }

    public String description(){
        return description;
    }
}
