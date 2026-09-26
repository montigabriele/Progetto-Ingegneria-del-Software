package it.foodmood.exception;

/*
 * Eccezioni per tutti gli errori di persistenza (Dao, DB, ...)
 * Sono unchecked in quanto non gestite dal chiamante
 */

public class PersistenceException extends RuntimeException{
    public PersistenceException(String message){ super(message);}
    public PersistenceException(String message, Throwable cause){ super(message, cause);}
    public PersistenceException(Throwable cause){ super(cause);}
}
