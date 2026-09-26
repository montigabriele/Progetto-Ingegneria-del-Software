package it.foodmood.domain.value;

/*
 * Categoria dietetica del piatto
*/

public enum DietCategory {
    TRADITIONAL("Tradizionale"),
    VEGETARIAN("Vegetariana"),
    VEGAN("Vegana"),
    GLUTEN_FREE("Senza glutine"),
    LACTOSE_FREE("Senza lattosio");

    private final String description;

    DietCategory(String description){
        this.description = description;
    }

    public String description(){
        return description;
    }

    public static DietCategory fromName(String value){
        if(value == null || value.isBlank()){
            throw new IllegalArgumentException("Tipologia dietetica nulla o vuota");
        }
        return DietCategory.valueOf(value.trim());
    }

    @Override
    public String toString(){
        return name() + " - " + description;
    }
}
