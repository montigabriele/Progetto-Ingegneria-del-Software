package it.foodmood.exception;

import it.foodmood.domain.value.TableStatus;

public class TableException extends DomainException {
    public TableException(String action, TableStatus state){
        super("Impossibile " + action + " il tavolo nello stato: " + state);
    }

    public TableException(String message){
        super(message);
    }
    
}
