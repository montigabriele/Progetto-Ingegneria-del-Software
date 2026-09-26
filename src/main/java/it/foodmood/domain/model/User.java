package it.foodmood.domain.model;

import java.util.Objects;
import java.util.UUID;

import it.foodmood.domain.value.Email;
import it.foodmood.domain.value.Person;
import it.foodmood.domain.value.Role;

public abstract class User {

    private final UUID id;
    private Person person;
    private Email email;
    private final Role role;

    // Costruttore protected per farlo instanziare solo dalle sottoclassi, per creare un nuovo utente
    protected User(Person person, Email email, Role role) {
        this.id = UUID.randomUUID();
        this.person = Objects.requireNonNull(person, "L'utente è obbligatorio.");
        this.email = Objects.requireNonNull(email, "L'email è obbligatoria.");
        this.role = Objects.requireNonNull(role, "Ruolo obbligatorio");
    }

    // Costruttore per ricreare l'utente dalla persistenza
    protected User(UUID id, Person person, Email email, Role role) {
        this.id = Objects.requireNonNull(id);
        this.person = Objects.requireNonNull(person, "L'utente è obbligatorio.");
        this.email = Objects.requireNonNull(email, "L'email è obbligatoria.");
        this.role = Objects.requireNonNull(role, "Ruolo obbligatorio");
    }

    public Person getPerson() {
        return person;
    }

    public Email getEmail() {
        return email;
    }

    public UUID getId() {
        return id;
    }

    public Role getRole(){
        return role;
    }

    public boolean hasRole(Role role){
        return this.role.equals(role);
    }

    public void changeEmail(Email newEmail){
        this.email = Objects.requireNonNull(newEmail);
    }

    @Override
    public boolean equals(Object object){
        if(this == object) return true;
        if(!(object instanceof User)) return false;
        User user = (User) object;
        return id.equals(user.id);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

    @Override
    public String toString(){
        return String.format("User { id = %s, %s , email = %s , role = %s }", id, person, email, role);
    }
}
