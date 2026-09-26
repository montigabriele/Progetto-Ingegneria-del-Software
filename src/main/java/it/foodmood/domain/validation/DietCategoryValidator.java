package it.foodmood.domain.validation;

import java.util.EnumSet;
import java.util.Set;

import it.foodmood.domain.model.Dish;
import it.foodmood.domain.value.Allergen;
import it.foodmood.domain.value.DietCategory;
import it.foodmood.exception.DishException;

public final class DietCategoryValidator {
    private DietCategoryValidator(){
        // costruttore vuoto
    }

    public static void validate(Dish dish) throws DishException{
        Set<DietCategory> categories = dish.getDietCategories();

        for(DietCategory category : categories){
            switch (category) {
                case VEGAN -> validateVegan(dish);
                case GLUTEN_FREE -> validateGlutenFree(dish);
                case LACTOSE_FREE -> validateLactoseFree(dish);
                case VEGETARIAN -> validateVegetarian(dish);
                case TRADITIONAL -> { /* nessuna validazione */}
            }
        }
    }

    private static void validateVegan(Dish dish) throws DishException{
        Set<Allergen> allergensForbitten = EnumSet.of( Allergen.MILK, Allergen.EGGS, Allergen.FISH);
        ensureNoAllergens(dish, allergensForbitten, "Vegana");
    }

    private static void validateVegetarian(Dish dish) throws DishException{
        Set<Allergen> allergensForbitten = EnumSet.of(Allergen.FISH);
        ensureNoAllergens(dish, allergensForbitten, "Vegetariana");
    }

    private static void validateLactoseFree(Dish dish) throws DishException{
        Set<Allergen> allergensForbitten = EnumSet.of(Allergen.MILK);
        ensureNoAllergens(dish, allergensForbitten, "Senza lattosio");
    }

    private static void validateGlutenFree(Dish dish) throws DishException{
        Set<Allergen> allergensForbitten = EnumSet.of(Allergen.GLUTEN);
        ensureNoAllergens(dish, allergensForbitten, "Senza glutine");
    }

    private static void ensureNoAllergens(Dish dish, Set<Allergen> forbitten, String category) throws DishException{
        Set<Allergen> present = dish.allergens();

        for(Allergen allergen: forbitten){
            if(present.contains(allergen)){
                throw new DishException("Il piatto " + dish.getName() + " non rispetta la categoria '" + category + "', contiene: " + allergen.description()); 
            }
        }
    }
}
