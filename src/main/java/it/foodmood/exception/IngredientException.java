package it.foodmood.exception;

public class IngredientException extends Exception {
    public IngredientException(String message){
        super(message);
    }

    public IngredientException(String message, Throwable cause){
        super(message, cause);
    }
}