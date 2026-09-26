package it.foodmood.domain.policy;

import java.util.EnumSet;
import java.util.Set;

import it.foodmood.domain.value.Allergen;
import it.foodmood.domain.value.CourseType;

public class AllergenFilterPolicy {
    
    
    public Set<Allergen> getAllergens(Set<CourseType> courseTypes){
        if(courseTypes == null || courseTypes.isEmpty()){
            return EnumSet.allOf(Allergen.class);
        }

        EnumSet<Allergen> relevant = EnumSet.noneOf(Allergen.class);
        for(CourseType courseType : courseTypes){
            relevant.addAll(plausibleAllergen(courseType));
        }
        return relevant;
    }

    private Set<Allergen> plausibleAllergen(CourseType courseType){
        return switch(courseType){
            case APPETIZER, FIRST_COURSE, PIZZA, MAIN_COURSE -> EnumSet.allOf(Allergen.class);

            case DESSERT -> EnumSet.of(Allergen.GLUTEN, Allergen.EGGS, Allergen.MILK, 
                                       Allergen.NUTS, Allergen.SOY, Allergen.SESAME, Allergen.SULPHITES);

            case SIDE_DISH -> EnumSet.of(Allergen.GLUTEN, Allergen.EGGS, Allergen.MILK, 
                                       Allergen.NUTS, Allergen.CELERY, Allergen.MUSTARD, Allergen.SESAME, Allergen.SULPHITES); 

            case FRUIT -> EnumSet.of(Allergen.NUTS, Allergen.SULPHITES);

            case BEVERAGE -> EnumSet.of(Allergen.SULPHITES, Allergen.GLUTEN, Allergen.MILK, Allergen.SOY);
        };
    }
}
