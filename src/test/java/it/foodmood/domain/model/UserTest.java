package it.foodmood.domain.model;

import org.junit.jupiter.api.Test;

import it.foodmood.domain.value.Email;
import it.foodmood.domain.value.Person;
import it.foodmood.domain.value.Role;
import it.foodmood.utils.security.PasswordHasher;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private final PasswordHasher hasher = new PasswordHasher();

    @Test
    void createCustomerUser(){
        char[] password = "PasswordUser123".toCharArray();
        String passwordHash = hasher.hash(password);

        // Creo il cliente
        User user = new Customer(new Person("Nome", "Cognome"), new Email("nome.cognome@email.it"));
        
        Credential credential = new Credential(user.getId(), passwordHash);

        assertEquals("Nome", user.getPerson().firstName());
        assertEquals("Cognome", user.getPerson().lastName());
        assertEquals("nome.cognome@email.it", user.getEmail().email());
        assertEquals(Role.CUSTOMER, user.getRole());

        assertTrue(hasher.verify(password, credential.passwordHash()));
        assertFalse(hasher.verify("password".toCharArray(), credential.passwordHash()));
    }

        
    @Test
    void createWaiter(){
        char[] password = "PasswordWaiter123".toCharArray();
        String passwordHash = hasher.hash(password);

        // Creo il cameriere
        User user = new Waiter(new Person("Luca", "Bianchi"), new Email("lucabianchi@foodmood.it"));
        
        Credential credential = new Credential(user.getId(), passwordHash);

        assertEquals("Luca", user.getPerson().firstName());
        assertEquals("Bianchi", user.getPerson().lastName());
        assertEquals("lucabianchi@foodmood.it", user.getEmail().email());
        assertEquals(Role.WAITER, user.getRole());

        assertTrue(hasher.verify(password, credential.passwordHash()));
        assertFalse(hasher.verify("password".toCharArray(), credential.passwordHash()));
    }

    @Test
    void createManager(){
        char[] password = "PasswordWaiter123".toCharArray();
        String passwordHash = hasher.hash(password);

        // Creo il cameriere
        User user = new Manager(new Person("Giulia", "Gialli"), new Email("giuliagialli@foodmood.it"));
        
        Credential credential = new Credential(user.getId(), passwordHash);

        assertEquals("Giulia", user.getPerson().firstName());
        assertEquals("Gialli", user.getPerson().lastName());
        assertEquals("giuliagialli@foodmood.it", user.getEmail().email());
        assertEquals(Role.MANAGER, user.getRole());

        assertTrue(hasher.verify(password, credential.passwordHash()));
        assertFalse(hasher.verify("password".toCharArray(), credential.passwordHash()));
    }
}
