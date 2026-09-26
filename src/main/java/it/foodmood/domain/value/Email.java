package it.foodmood.domain.value;

import java.util.Objects;

import it.foodmood.utils.Validator;

public record Email(String email) {
    
    public Email{
        Objects.requireNonNull(email, "L'email non può essere nulla.");
        String emailNormalized = email.trim().toLowerCase();
        if(!Validator.isValidEmail(emailNormalized)) {
            throw new IllegalArgumentException("Formato email non valido");
        }
        email = emailNormalized;
    }

    @Override
    public String toString(){
        return email;
    }
}
