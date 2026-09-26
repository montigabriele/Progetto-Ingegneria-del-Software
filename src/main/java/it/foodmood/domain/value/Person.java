package it.foodmood.domain.value;

import java.util.Objects;

public record Person(String firstName, String lastName) {
    
    public Person{
        firstName = normalize(firstName, "Il nome è obbligatorio");
        lastName = normalize(lastName, "Il cognome è obbligatorio");
    }

    private static String normalize(String value, String message){
        String trimmed = Objects.requireNonNull(value, message).trim();
        if(trimmed.isEmpty()){
            throw new IllegalArgumentException(message + "vuoto");
        }
        return trimmed;
    }

    @Override
    public String toString(){
        return firstName + " " + lastName;
    }
}
