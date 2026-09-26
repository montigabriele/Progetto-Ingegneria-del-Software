package it.foodmood.bean;

import java.util.Arrays;

import it.foodmood.utils.Validator;

public class RegistrationBean {

    private String name;
    private String surname;
    private String email;
    private char[] password;
    private char[] confirmPassword;

    public RegistrationBean(){
        // Costruttore vuoto
    }

    public String getName(){
        return name; 
    }

    public String getSurname(){
        return surname;
    }

    public String getEmail(){
        return email;
    }

    public char[] getPassword(){
        return password;
    }

    public char[] getConfirmPassword(){
        return confirmPassword;
    }

    public void setName(String name){
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Il nome non deve essere vuoto");
        }
        this.name = name.trim();
    }

    public void setSurname(String surname){
        if(surname == null || surname.isBlank()){
            throw new IllegalArgumentException("Il cognome non deve essere vuoto");
        }
        this.surname = surname.trim();
    }

    public void setEmail(String email){
        if(email == null){
            throw new IllegalArgumentException("E-mail non deve essere vuota");
        }
        String cleaned = email.trim();
        if(Validator.isValidEmail(cleaned)){
            this.email = cleaned.toLowerCase();
        } else {
            throw new IllegalArgumentException("Formato e-mail non valido");
        }
    }

    public void setPassword(char[] password){
        if(password == null || password.length == 0){
            throw new IllegalArgumentException("La password è obbligatoria");
        }
        if(Validator.isValidPassword(password)){
            this.password = password; 
        } else {
            Arrays.fill(password, '\0');
            throw new IllegalArgumentException("Password non valida: deve avere almeno 8 caratteri, una maiuscola, una minuscola e un numero");
        }
    }

    public void setConfirmPassword(char[] confirmPassword){
        if(confirmPassword == null || confirmPassword.length == 0){
            throw new IllegalArgumentException("La conferma password è obbligatoria");
        }
        this.confirmPassword = confirmPassword;
    }
}
