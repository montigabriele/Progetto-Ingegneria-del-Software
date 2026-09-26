package it.foodmood.domain.model;

import java.util.Set;

import org.junit.jupiter.api.Test;

import it.foodmood.domain.value.Allergen;
import it.foodmood.domain.value.Macronutrients;
import it.foodmood.domain.value.Quantity;
import it.foodmood.domain.value.Unit;

import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {
    @Test
    void testIngredientCreation(){
        Macronutrients macro = new Macronutrients(10,5,2);
        Set<Allergen> allergens = Set.of(Allergen.MILK);
        Ingredient ingredient = new Ingredient("Latte", macro, Unit.MILLILITER, allergens);

        assertEquals("Latte", ingredient.getName());
        assertEquals(macro, ingredient.getMacro());
        assertTrue(ingredient.isAllergenic());
        assertTrue(ingredient.contains(Allergen.MILK));
    }

    @Test
    void testKcalForQuantity(){
        Macronutrients macro = new Macronutrients(10,5,2);
        Ingredient ingredient = new Ingredient("Latte", macro, Unit.MILLILITER, Set.of(Allergen.MILK));
        Quantity q = new Quantity(50, Unit.MILLILITER);

        double expected = macro.kcal() * 0.5;
        assertEquals(expected, ingredient.kcalFor(q));
    }

    @Test
    void testToStringFormat(){
        Set<Allergen> allergens = Set.of(Allergen.CELERY);
        Ingredient ingredient = new Ingredient("Pane", new Macronutrients(7,60,1), Unit.GRAM, allergens);
        assertTrue(ingredient.toString().contains("Pane"));
    }
}
