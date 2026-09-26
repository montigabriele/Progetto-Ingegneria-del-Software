package it.foodmood.exception;

public class DomainException extends RuntimeException {
    public DomainException(String message){
        super(message);
    }
}
