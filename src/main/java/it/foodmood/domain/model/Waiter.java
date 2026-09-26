package it.foodmood.domain.model;

import java.util.UUID;

import it.foodmood.domain.value.Email;
import it.foodmood.domain.value.Person;
import it.foodmood.domain.value.Role;

public final class Waiter extends User{
    public Waiter(Person person, Email email){
        super(person, email, Role.WAITER);
    }

    public Waiter(UUID id, Person person, Email email){
        super(id, person, email, Role.WAITER);
    }
}
