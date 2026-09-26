package it.foodmood.domain.value;

public enum Kcal {
    LIGHT("Leggero"),
    BALANCED("Moderato"),
    COMPLETE("Abbondante"),
    FREE("Nessun limite");

    private final String description;

    Kcal(String description){
        this.description = description;
    }

    public String description(){
        return description;
    }

    public static Allergen fromName(String value){
        if(value == null || value.isBlank()){
            throw new IllegalArgumentException("Allergene nullo o vuoto");
        }
        return Allergen.valueOf(value.trim());
    }

    @Override
    public String toString(){
        return name() + " - " + description;
    }
}
