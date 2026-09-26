package it.foodmood.domain.value;

/*
 * Tipologia della portata nel menù
 * Usato da: Dish (per classificare la tipologia del piatto)
 * GRASP: Information Expert 
 */

public enum CourseType {
    APPETIZER("Antipasto"), 
    FIRST_COURSE("Primo"), 
    MAIN_COURSE("Secondo"), 
    SIDE_DISH("Contorno"), 
    PIZZA("Pizza"), 
    BEVERAGE("Bevanda"),
    FRUIT("Frutta"), 
    DESSERT("Dolce");

    private final String description;

    CourseType(String description){
        this.description = description;
    }

    public String description(){
        return description;
    }

    public static CourseType fromName(String value){
        if(value == null || value.isBlank()){
            throw new IllegalArgumentException("Tipologia di portata nulla o vuota");
        }
        return CourseType.valueOf(value.trim());
    }

    @Override
    public String toString(){
        return name() + " - " + description;
    }
}

