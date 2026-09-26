package it.foodmood.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.foodmood.bean.ActorBean;
import it.foodmood.bean.LoginBean;
import it.foodmood.config.PersistenceMode;
import it.foodmood.config.UserMode;
import it.foodmood.exception.AuthenticationException;
import it.foodmood.persistence.dao.DaoFactory;

class LoginControllerTest {

    @BeforeAll
    static void initDao() {
        DaoFactory.init(PersistenceMode.FILESYSTEM);
    }

    @Test
    void testLoginSuccessfully() throws AuthenticationException {

        LoginController controller = new LoginController();

        LoginBean bean = new LoginBean();

        bean.setEmail( "mariorossi@email.com");

        bean.setPassword( "Password123".toCharArray());

        ActorBean actor = controller.login( bean, UserMode.CUSTOMER);

        Assertions.assertNotNull(actor);

        Assertions.assertNotNull( actor.getActorId() );

        Assertions.assertTrue( actor.isLogged());

        Assertions.assertFalse( actor.isGuest());
    }

    @Test
    void testLoginEmailNotFound() {

        LoginController controller = new LoginController();

        LoginBean bean = new LoginBean();

        bean.setEmail( "emailnonvalida@email.com");

        bean.setPassword( "Password123".toCharArray());

        Assertions.assertThrows(AuthenticationException.class,() -> controller.login(bean, UserMode.CUSTOMER));
    }

    @Test
    void testLoginInvalidPassword() {

        LoginController controller = new LoginController();

        LoginBean bean = new LoginBean();

        bean.setEmail( "mariorossi@email.com" );

        bean.setPassword( "password".toCharArray());

        Assertions.assertThrows(AuthenticationException.class,() -> controller.login(bean,UserMode.CUSTOMER));
    }
}