package it.foodmood.exception;

public class DishException extends Exception {
    public DishException(String message){
        super(message);
    }

    public DishException(String message, Throwable cause){
        super(message, cause);
    }
}
