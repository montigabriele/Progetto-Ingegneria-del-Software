package it.foodmood.controller;

import java.util.Arrays;

import it.foodmood.bean.ActorBean;
import it.foodmood.bean.LoginBean;
import it.foodmood.config.UserMode;
import it.foodmood.domain.model.Credential;
import it.foodmood.domain.model.User;
import it.foodmood.domain.value.Email;
import it.foodmood.exception.AuthenticationException;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.CredentialDao;
import it.foodmood.persistence.dao.DaoFactory;
import it.foodmood.persistence.dao.UserDao;
import it.foodmood.utils.security.PasswordHasher;

public class LoginController {

    private static final String INCORRECT_CREDENTIALS = "Credenziali errate.";

    private final UserDao userDao;
    private final CredentialDao credentialDao;
    private final PasswordHasher passwordHasher;

    public LoginController() {

        DaoFactory daoFactory = DaoFactory.getInstance();

        this.userDao = daoFactory.getUserDao();
        this.credentialDao = daoFactory.getCredentialDao();
        this.passwordHasher = new PasswordHasher();
    }

    public ActorBean login( LoginBean loginBean, UserMode mode) throws AuthenticationException {

        if (loginBean == null) {
            throw new AuthenticationException( INCORRECT_CREDENTIALS);
        }

        try {

            Email email = new Email(loginBean.getEmail());

            User user = userDao.findByEmail(email).orElseThrow(() -> new AuthenticationException(INCORRECT_CREDENTIALS));

            Credential credential = credentialDao.findByUserId(user.getId());

            if (credential == null || credential.passwordHash() == null) {
                throw new AuthenticationException(INCORRECT_CREDENTIALS);
            }

            boolean validPassword = passwordHasher.verify( loginBean.getPassword(), credential.passwordHash());

            if (!validPassword) {
                throw new AuthenticationException(INCORRECT_CREDENTIALS);
            }

            boolean authorized = user.hasRole( mode.requireRole());

            if (!authorized) {
                throw new AuthenticationException( INCORRECT_CREDENTIALS);
            }
            
            ActorBean actor = new ActorBean();

            actor.setActorId(user.getId());

            actor.setName(user.getPerson().firstName());

            actor.setSurname(user.getPerson().lastName());

            actor.setGuest(false);
            actor.setLogged(true);

            return actor;

        } catch (IllegalArgumentException e) {
            throw new AuthenticationException(INCORRECT_CREDENTIALS,e);

        } catch (PersistenceException e) {
            throw new AuthenticationException( "Errore tecnico durante il login. " + "Riprova più tardi.", e);

        } finally {

            char[] password = loginBean.getPassword();
            if (password != null) {
                Arrays.fill( password,'\0');
            }
        }
    }
}