package it.foodmood.domain.policy;

import java.util.Set;

import it.foodmood.domain.value.CourseType;
import it.foodmood.domain.value.OrderComplexity;

public class OrderComplexityEvaluator {
    public OrderComplexity evaluate(Set<CourseType> courses){
        int score = calculateScore(courses);
        int courseCount = courses.size();

        if(score <= 1 && courseCount == 1){
            return OrderComplexity.MINIMAL;
        }

        if(score <= 3 && courseCount <= 2){
            return OrderComplexity.SIMPLE;
        }

        if(score <= 5 && courseCount <= 3){
            return OrderComplexity.MODERATE;
        }

        return OrderComplexity.COMPLETE;
    }

    private int calculateScore(Set<CourseType> courses){
        return courses.stream().mapToInt(this::scoreFor).sum();
    }

    private int scoreFor(CourseType courseType){
        return switch(courseType){
            case BEVERAGE, FRUIT -> 1;
            case SIDE_DISH -> 2;
            case APPETIZER, DESSERT -> 3;
            case FIRST_COURSE, MAIN_COURSE, PIZZA -> 4;
        };
    }
}
