package it.foodmood.view.ui.cli;

import it.foodmood.bean.ActorBean;
import it.foodmood.bean.LoginBean;
import it.foodmood.config.UserMode;
import it.foodmood.controller.LoginController;
import it.foodmood.exception.AuthenticationException;

public class CliLoginView extends ConsoleView {

    private static final String TITLE = "LOGIN";

    private final UserMode userMode;

    public CliLoginView(UserMode userMode){
        super();
        this.userMode = userMode;
    }

    public ActorBean displayPage(){
        clearScreen();
        while(true){
            showTitle(TITLE);
            showInfo("Accedi al tuo account\n");
            LoginBean loginBean = new LoginBean();
            while(true){
                String email = askInput("Email: ");
                try {
                    loginBean.setEmail(email);
                    break;
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
            while(true){
                String password = askInput("Password: ");
                try {
                    loginBean.setPassword(password.toCharArray());
                    break;
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }



            try {
                LoginController loginController = new LoginController();
                ActorBean actor = loginController.login(loginBean, userMode);
                showSuccess("Login effettuato con successo");
                waitForEnter(null);
                return actor;
            } catch (AuthenticationException e) {
                clearScreen();
                showError(e.getMessage());
                showInfo("Riprova\n");
            }
        }
    }
}
