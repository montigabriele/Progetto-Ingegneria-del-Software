package it.foodmood.domain.policy;

import java.util.Set;

import it.foodmood.domain.value.CourseType;

public class KcalWeight {
    
    public double weightFor(CourseType courseType){
        return switch(courseType){
            case APPETIZER -> 0.20;
            case FIRST_COURSE -> 0.40;
            case MAIN_COURSE -> 0.35;
            case PIZZA -> 0.45;
            case DESSERT -> 0.20;
            case FRUIT -> 0.05;
            case BEVERAGE -> 0.03;
            case SIDE_DISH -> 0.10;
        };
    }

    public double normalized(CourseType courseType, Set<CourseType> selected){
        if(selected == null || selected.isEmpty()) return 1.0;

        double sum = selected.stream().mapToDouble(this::weightFor).sum();

        if(sum <= 0) return 1.0;

        return weightFor(courseType)/sum;
    }
}
