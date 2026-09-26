package it.foodmood.bean;

public class IngredientPortionBean {
    private IngredientBean ingredient;
    private double quantity;
    private String unit;

    public IngredientPortionBean(){
        // Costruttore vuoto
    }

    public IngredientBean getIngredient() {return ingredient;}

    public double getQuantity(){ return quantity;}

    public String getUnit(){ return unit;}

    public void setIngredient(IngredientBean ingredient){
        if(ingredient == null) {
            throw new IllegalArgumentException("L'ingrediente non può essere nullo");
        }
        this.ingredient = ingredient;
    }

    public void setQuantity(double quantity){
        if(!Double.isFinite(quantity) || quantity <= 0.0d){
            throw new IllegalArgumentException("La quantità deve essere maggiore di zero");
        }
        this.quantity = quantity;
    }

    public void setUnit(String unit){
        if(unit == null || unit.isBlank()){
            throw new IllegalArgumentException("L'unità non può essere nulla");
        }
        this.unit = unit.trim().toUpperCase();
    }
}
