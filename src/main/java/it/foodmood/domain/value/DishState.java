package it.foodmood.domain.value;

public enum DishState {
    AVAILABLE("Disponibile"),
    UNAVAILABLE("Non disponibile");

    private final String description;

    DishState(String description){
        this.description = description;
    }

    public String description(){
        return description;
    }

    public boolean available(){
        return this == AVAILABLE;
    }

    @Override
    public String toString(){
        return name() + " - " + description;
    }
}
