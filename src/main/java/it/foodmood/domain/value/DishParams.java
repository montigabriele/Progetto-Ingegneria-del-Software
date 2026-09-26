package it.foodmood.domain.value;

import java.util.List;
import java.util.Set;

public record DishParams(    
    String name,
    String description,
    CourseType courseType,
    Set<DietCategory> dietCategories,
    List<IngredientPortion> ingredients,
    DishState state,
    Image image,
    Money price) {}