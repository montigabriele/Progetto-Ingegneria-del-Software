package it.foodmood.domain.model;

import java.util.*;

import it.foodmood.domain.value.Allergen;
import it.foodmood.domain.value.Macronutrients;
import it.foodmood.domain.value.Quantity;
import it.foodmood.domain.value.Unit;

public record Ingredient(String name, Macronutrients macro, Unit unit, Set<Allergen> allergens) {

    public Ingredient {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome non può essere vuoto.");
        Objects.requireNonNull(macro, "I macronutrienti non possono essere nulli.");
        Objects.requireNonNull(unit, "L'unità non può essere nulla.");
        allergens = (allergens == null) ? Set.of() : Set.copyOf(allergens);
    }

    public String getName() {return name;}
    public Macronutrients getMacro() {return macro;}
    public Set<Allergen> getAllergens() {return allergens;}
    public Unit getUnit() {return unit;}

    // GRASP: Information Expert -> kcal per la quantità usata
    public double kcalFor(Quantity qt){
        return macro.kcal() * qt.ratioToBase();
    }

    public Macronutrients getMacroFor(Quantity qt){
        Objects.requireNonNull(qt, "La quantità non può essere nulla!");
        return macro.scale(qt.ratioToBase());
    }

    public boolean isAllergenic() {return !allergens.isEmpty();}

    public boolean contains(Allergen allergen) {return allergens.contains(allergen);}

    @Override public String toString(){
        return "%s (per 100: %.1f kcal, allergeni = %s)".formatted(name, macro.kcal(), allergens);
    }
    
}
