package it.foodmood.exception;

public class CartException extends Exception {
    public CartException(String message){
        super(message);
    }

    public CartException(String message, Throwable cause){
        super(message, cause);
    }
}
