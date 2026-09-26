package it.foodmood.infrastructure.bootstrap;

import it.foodmood.domain.model.Credential;
import it.foodmood.domain.model.Manager;
import it.foodmood.domain.model.Waiter;
import it.foodmood.domain.value.Email;
import it.foodmood.domain.value.Person;
import it.foodmood.persistence.dao.CredentialDao;
import it.foodmood.persistence.dao.DaoFactory;
import it.foodmood.persistence.dao.UserDao;
import it.foodmood.utils.security.PasswordHasher;

public final class DemoBootstrap {
    private DemoBootstrap(){
        // costruttore vuoto
    }

    public static void initDemo(){
        UserDao userDao = DaoFactory.getInstance().getUserDao();
        CredentialDao credentialDao = DaoFactory.getInstance().getCredentialDao();
        PasswordHasher hasher = new PasswordHasher();

        Manager manager = new Manager(new Person("Mario", "Rossi"), new Email("mariorossi@email.com"));
        Waiter waiter = new Waiter(new Person("Luigi", "Verdi"), new Email("luigiverdi@email.com"));
        String password = "Password123";
        String hashPassword = hasher.hash(password.toCharArray());

        Credential credentialManager = new Credential(manager.getId(), hashPassword);
        Credential credentialWaiter = new Credential(waiter.getId(), hashPassword);
    
        userDao.insert(manager);
        userDao.insert(waiter);
        credentialDao.saveCredential(credentialManager);
        credentialDao.saveCredential(credentialWaiter);
    }
}
