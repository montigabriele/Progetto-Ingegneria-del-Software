package it.foodmood.domain.value;

import java.util.Set;

public record FoodPreferences (Set<DietCategory> dietCategories, Set<Allergen> allergens) {

    public static final FoodPreferences EMPTY = new FoodPreferences(Set.of(), Set.of());

    public FoodPreferences {
        dietCategories = dietCategories == null ? Set.of() : Set.copyOf(dietCategories);
        allergens = allergens == null ? Set.of() : Set.copyOf(allergens);
    }
}
