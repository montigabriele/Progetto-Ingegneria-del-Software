package it.foodmood.exception;

public class BackRequestedException extends RuntimeException {
    public BackRequestedException(){
        super("Indietro");
    }
}
