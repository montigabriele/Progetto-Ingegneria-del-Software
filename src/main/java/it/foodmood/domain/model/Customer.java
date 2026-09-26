package it.foodmood.domain.model;

import java.util.Objects;
import java.util.UUID;

import it.foodmood.domain.value.Email;
import it.foodmood.domain.value.FoodPreferences;
import it.foodmood.domain.value.Person;
import it.foodmood.domain.value.Role;

public final class Customer extends User{

    private FoodPreferences foodPreferences;

    public Customer(Person person, Email email){
        super(person, email, Role.CUSTOMER);
        this.foodPreferences = FoodPreferences.EMPTY;
    }

    public Customer(UUID id, Person person, Email email){
        super(id, person, email, Role.CUSTOMER);
        this.foodPreferences = FoodPreferences.EMPTY;
    }

    public FoodPreferences getFoodPreferences(){
        return foodPreferences;
    }

    public void updateFoodPreferences(FoodPreferences newPreferences){
        this.foodPreferences = Objects.requireNonNull(newPreferences);
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Customer)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode(){
        return super.hashCode();
    }
}
