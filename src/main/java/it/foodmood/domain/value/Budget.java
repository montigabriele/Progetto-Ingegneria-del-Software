package it.foodmood.domain.value;

public enum Budget {
    ECONOMIC("Economico"),
    BALANCED("Equilibrato"),
    PREMIUM("Premium"),
    FREE("Nessun limite");

    private final String description;

    Budget(String description){
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
