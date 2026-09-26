package it.foodmood.bean;

import java.util.LinkedHashSet;
import java.util.Set;

public class OrderFlowBean {

    private Set<String> courses;
    private Set<String> dietCategories;
    private Set<String> allergens;

    private Integer kcalPreference;
    private Integer budgetPreference;

    public OrderFlowBean() {
        clear();
    }

    public Set<String> getCourses() {
        return Set.copyOf(courses);
    }

    public void setCourses(Set<String> courses) {
        this.courses = copyOf(courses);
    }

    public Set<String> getDietCategories() {
        return Set.copyOf(dietCategories);
    }

    public void setDietCategories(Set<String> dietCategories) {
        this.dietCategories = copyOf(dietCategories);
    }

    public Set<String> getAllergens() {
        return Set.copyOf(allergens);
    }

    public void setAllergens(Set<String> allergens) {
        this.allergens = copyOf(allergens);
    }

    public Integer getKcalPreference() {
        return kcalPreference;
    }

    public void setKcalPreference(Integer kcalPreference) {
        this.kcalPreference = kcalPreference;
    }

    public Integer getBudgetPreference() {
        return budgetPreference;
    }

    public void setBudgetPreference(Integer budgetPreference) {
        this.budgetPreference = budgetPreference;
    }

    public void clear() {
        this.courses = new LinkedHashSet<>();
        this.dietCategories = new LinkedHashSet<>();
        this.allergens = new LinkedHashSet<>();

        this.kcalPreference = null;
        this.budgetPreference = null;
    }

    private Set<String> copyOf(Set<String> values) {

        if (values == null) {
            return new LinkedHashSet<>();
        }

        return new LinkedHashSet<>(values);
    }
}