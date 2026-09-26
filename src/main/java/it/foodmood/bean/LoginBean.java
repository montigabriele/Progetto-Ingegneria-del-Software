package it.foodmood.bean;

import it.foodmood.utils.Validator;

public class LoginBean {
    private String email;
    private char[] password;

    public LoginBean(){
        // Costruttore vuoto
    }

    public String getEmail(){
        return email;
    }

    public char[] getPassword(){
        return password;
    }

    public void setPassword(char[] password){
        this.password = password;
    }

    // validazione sintattica
    public void setEmail(String email){
        if(email == null){
            throw new IllegalArgumentException("E-mail obbligatoria");
        }
        String cleaned = email.trim();
        if(Validator.isValidEmail(cleaned)){
            this.email = cleaned.toLowerCase();
        } else {
            throw new IllegalArgumentException("Formato e-mail non valido");
        }
    }
}
