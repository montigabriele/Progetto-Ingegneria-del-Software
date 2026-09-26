package it.foodmood.domain.value;

import java.util.Objects;

import it.foodmood.domain.model.Ingredient;

/*
 * Rappresenta la porzione di un ingrediente
 */

public record IngredientPortion(Ingredient ingredient, Quantity quantity) {

    public IngredientPortion {
        ingredient = Objects.requireNonNull(ingredient, "Ingrediente non può essere nullo.");
        quantity = Objects.requireNonNull(quantity, "Quantità non può essere nulla.");
    }

    @Override
    public String toString(){
        return quantity + " x " + ingredient.getName();
    }
}