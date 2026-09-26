package it.foodmood.exception;

public class ConsoleReadException extends RuntimeException {
    public ConsoleReadException(String message, Throwable cause){
        super(message, cause);
    }
}
