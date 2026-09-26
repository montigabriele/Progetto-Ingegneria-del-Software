package it.foodmood.domain.model;

import java.util.HashSet;
import java.util.Set;

import it.foodmood.domain.value.Allergen;
import it.foodmood.domain.value.CourseType;
import it.foodmood.domain.value.DietCategory;

public class OrderFlowState {
    private Set<DietCategory> dietCategories;
    private Set<CourseType> courseTypes;
    private Set<Allergen> allergens;
    private Integer kcalPreference;
    private Integer budgetPreference;

    public OrderFlowState(){
        this.dietCategories = new HashSet<>();
        this.courseTypes = new HashSet<>();
        this.allergens = new HashSet<>();
    }

    public void setDietCategory(Set<DietCategory> categories){
        this.dietCategories = categories != null ? new HashSet<>(categories) : new HashSet<>();
    }

    public void setCourseType(Set<CourseType> courses){
        this.courseTypes = courses != null ? new HashSet<>(courses) : new HashSet<>();
    }

    public void setAllergens(Set<Allergen> allergens){
        this.allergens = allergens != null ? new HashSet<>(allergens) : new HashSet<>();
    }

    public void setKcalPreference(Integer kcalPreference){
        this.kcalPreference = kcalPreference;
    }

    public void setBudgetPreference(Integer budgetPreference){
        this.budgetPreference = budgetPreference;
    }

    public Set<DietCategory> getDietCategories(){
        return new HashSet<>(dietCategories);
    }

    public Set<CourseType> getCourseType(){
        return new HashSet<>(courseTypes);
    }

    public Set<Allergen> getAllergens(){
        return new HashSet<>(allergens);
    }

    public Integer getKcalPreference(){
        return kcalPreference;
    }

    public Integer getBudgetPreference(){
        return budgetPreference;
    }
}
