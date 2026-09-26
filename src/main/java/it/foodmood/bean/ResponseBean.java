package it.foodmood.bean;

import java.util.List;
import java.util.Set;

import it.foodmood.domain.value.Allergen;
import it.foodmood.domain.value.StepType;

public class ResponseBean {
    private StepType nextStep;
    private Set<Allergen> allergens;
    private List<Integer> values;
    private List<DishBean> dishes;

    public ResponseBean(){
        // Costruttore vuoto
    }

    public StepType getNextStep(){
        return nextStep;
    }

    public void setNextStep(StepType nextStep){
        this.nextStep = nextStep;
    }

    public Set<Allergen> getAllergens(){
        return allergens;
    }

    public void setAllergens(Set<Allergen> allergens){
        this.allergens = allergens;
    }

    public List<Integer> getValues(){
        return values;
    }

    public void setValues(List<Integer> values){
        this.values = values;
    }

    public List<DishBean> getDishes(){
        return dishes;
    }

    public void setDishes(List<DishBean> dishes){
        this.dishes = dishes;
    }
}
