package it.foodmood.exception;

public class TableSessionException extends Exception{
    public TableSessionException(String message){
        super(message);
    }

    public TableSessionException(String message, Throwable cause){
        super(message, cause);
    }
}

