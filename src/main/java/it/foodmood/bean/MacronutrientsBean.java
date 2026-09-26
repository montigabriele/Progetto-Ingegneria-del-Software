package it.foodmood.bean;

import java.util.Locale;

public class MacronutrientsBean {
    private Double protein;
    private Double carbohydrates;
    private Double fat;

    public MacronutrientsBean(){
        // Costruttore vuoto
    }

    // Getter
    public Double getProtein(){ return protein;}

    public Double getCarbohydrates(){ return carbohydrates;}

    public Double getFat(){ return fat;}

    // Setter
    public void setProtein(Double protein){
        if(protein != null && !isNonNegative(protein)){
            throw new IllegalArgumentException("Le proteine devono essere >= 0");
        }
        this.protein = normalize(protein);
    }

    public void setCarbohydrates(Double carbohydrates){
        if(carbohydrates != null && !isNonNegative(carbohydrates)){
            throw new IllegalArgumentException("I carboidrati devono essere >= 0");
        }
        this.carbohydrates = normalize(carbohydrates);
    }

    public void setFat(Double fat){
        if(fat != null && !isNonNegative(fat)){
            throw new IllegalArgumentException("I grassi devono essere >= 0");
        }
        this.fat = normalize(fat);
    }

    public boolean isEmpty(){
        return protein == null &&
               carbohydrates == null &&
               fat == null;
    }

    public double calculateKcal(){
        double p = (protein == null) ? 0.0 : protein;
        double c = (carbohydrates == null) ? 0.0 : carbohydrates;
        double f = (fat == null) ? 0.0 : fat;
        return (p*4) + (c*4) + (f*9);

    }

    // Metodi privati per validazione sintattica
    private boolean isNonNegative(Double number){
        return number != null && Double.isFinite(number) && number >= 0.0d;
    }

    private Double normalize(Double number){
        return (number == null) ? null : Double.valueOf(String.format(Locale.ROOT, "%.2f", number));
    }
}
