package it.foodmood.domain.model;

import java.util.UUID;

import it.foodmood.domain.value.Email;
import it.foodmood.domain.value.Person;
import it.foodmood.domain.value.Role;

public final class Manager extends User{

    public Manager(Person person, Email email){
        super(person, email, Role.MANAGER);
    }

    public Manager(UUID id, Person person, Email email){
        super(id, person, email, Role.MANAGER);
    }
}
