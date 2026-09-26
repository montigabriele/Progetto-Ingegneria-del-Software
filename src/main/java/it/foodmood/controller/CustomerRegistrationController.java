package it.foodmood.controller;

import java.util.Arrays;

import it.foodmood.bean.RegistrationBean;
import it.foodmood.domain.model.Credential;
import it.foodmood.domain.model.Customer;
import it.foodmood.domain.value.Email;
import it.foodmood.domain.value.Person;
import it.foodmood.exception.PersistenceException;
import it.foodmood.exception.RegistrationException;
import it.foodmood.persistence.dao.CredentialDao;
import it.foodmood.persistence.dao.DaoFactory;
import it.foodmood.persistence.dao.UserDao;
import it.foodmood.utils.security.PasswordHasher;

public class CustomerRegistrationController {
    
    private final UserDao userDao = DaoFactory.getInstance().getUserDao();
    private final CredentialDao credentialDao = DaoFactory.getInstance().getCredentialDao();
    private final PasswordHasher passwordHasher = new PasswordHasher();

    public void registration(RegistrationBean registrationBean) throws RegistrationException{
        try{
            // 1) Verifico se l'email è già registrata
            Email email = new Email(registrationBean.getEmail());
            if(userDao.findByEmail(email).isPresent()){
                throw new RegistrationException("Email già registrata.");
            }

            // 2) Creazione del dominio
            Person person = new Person(registrationBean.getName(), registrationBean.getSurname());
            Customer newUser = new Customer(person, email);

            // 3) Verifica delle password
            if(!Arrays.equals(registrationBean.getPassword(), registrationBean.getConfirmPassword())){
                throw new RegistrationException("Le password non coincidono.");
            }

            // 4) Hash della password
            String hashedPassword = passwordHasher.hash(registrationBean.getPassword());

            // 5) Creazione credenziali
            Credential credential = new Credential(newUser.getId(), hashedPassword);

            // 6) Salvataggio in persistenza
            userDao.insert(newUser);
            credentialDao.saveCredential(credential);

        } catch (IllegalArgumentException e){
            throw new RegistrationException("Dati di registrazione non validi.", e);
        } catch (PersistenceException e){
            throw new RegistrationException("Errore tecnico durante la registrazione. Riprova più tardi.", e);
        } finally {
            // 8 Pulizia della password in memoria
            char[] password = registrationBean.getPassword();
            char[] confirmPassword = registrationBean.getConfirmPassword();
            if(password != null){
                Arrays.fill(password, '\0');
            }
            if(confirmPassword != null){
                Arrays.fill(confirmPassword, '\0');
            }
        }
    }
}
