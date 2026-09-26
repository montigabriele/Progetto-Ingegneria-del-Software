package it.foodmood.domain.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import it.foodmood.domain.model.Ingredient;

class IngredientPortionTest {
    
    @Test
        void of_createsEqualsPortions(){
        Macronutrients macro = new Macronutrients(4,3,3.5);
        Ingredient latte = new Ingredient("Latte", macro, Unit.MILLILITER, Set.of(Allergen.MILK));

        Quantity q = new Quantity(200, Unit.MILLILITER);

        IngredientPortion ingredient1 = new IngredientPortion(latte, q);
        IngredientPortion ingredient2 = new IngredientPortion(latte, q);

        assertEquals(ingredient1, ingredient2);
        assertEquals(ingredient2, ingredient1);
        assertTrue(ingredient1.toString().contains("Latte"));
    }
}
