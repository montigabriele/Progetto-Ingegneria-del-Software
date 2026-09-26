package it.foodmood.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import it.foodmood.domain.value.Allergen;
import it.foodmood.domain.value.CourseType;
import it.foodmood.domain.value.DietCategory;
import it.foodmood.domain.value.DishParams;
import it.foodmood.domain.value.DishState;
import it.foodmood.domain.value.IngredientPortion;
import it.foodmood.domain.value.Macronutrients;
import it.foodmood.domain.value.Money;
import it.foodmood.domain.value.Quantity;
import it.foodmood.domain.value.Unit;

class DishTest {
    @Test
    void testDish(){
        // Creo ingredienti
        Macronutrients macroPasta = new Macronutrients(12,75,1.5);
        Ingredient pasta = new Ingredient("Spaghetti", macroPasta, Unit.GRAM, Set.of(Allergen.GLUTEN));

        Macronutrients macroGuanciale = new Macronutrients(9,0,45);
        Ingredient guanciale = new Ingredient("Guanciale", macroGuanciale, Unit.GRAM, Set.of());

        Macronutrients macroCheese = new Macronutrients(26,0,32);
        Ingredient pecorino = new Ingredient("Pecorino", macroCheese, Unit.GRAM, Set.of(Allergen.MILK));

        Macronutrients macroEggs = new Macronutrients(13,1,11);
        Ingredient eggs = new Ingredient("Uova", macroEggs, Unit.GRAM, Set.of(Allergen.EGGS));

        IngredientPortion portionPasta = new IngredientPortion(pasta, new Quantity(100, Unit.GRAM));
        IngredientPortion portionGuanciale = new IngredientPortion(guanciale, new Quantity(50, Unit.GRAM));
        IngredientPortion portionEggs = new IngredientPortion(eggs, new Quantity(60, Unit.GRAM));
        IngredientPortion portionCheese = new IngredientPortion(pecorino, new Quantity(30, Unit.GRAM));

        List<IngredientPortion> ingredients = List.of(portionPasta, portionGuanciale, portionEggs, portionCheese);

        Money price = new Money(9);

        EnumSet<DietCategory> categories = EnumSet.of(DietCategory.TRADITIONAL);

        DishParams params = new DishParams("Carbonara", "Carbonara romana", CourseType.FIRST_COURSE, categories, ingredients, DishState.AVAILABLE, null, price);

        Dish carbonara = Dish.create(params);

        assertEquals("Carbonara", carbonara.getName());
        assertEquals(CourseType.FIRST_COURSE, carbonara.getCourseType());
        assertTrue(carbonara.isAllergenic());
        assertTrue(carbonara.allergens().contains(Allergen.GLUTEN));
        assertTrue(carbonara.containsAllergen(Allergen.EGGS));
        assertTrue(carbonara.containsAllergen(Allergen.MILK));
        assertFalse(carbonara.containsAllergen(Allergen.FISH));
        assertTrue(carbonara.getKcal() > 0);
        assertTrue(carbonara.isAvailable());
        assertNotNull(carbonara.getId());
    }
}
