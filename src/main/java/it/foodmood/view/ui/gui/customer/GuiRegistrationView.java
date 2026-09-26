package it.foodmood.view.ui.gui.customer;

import it.foodmood.bean.RegistrationBean;
import it.foodmood.controller.CustomerRegistrationController;
import it.foodmood.exception.RegistrationException;
import it.foodmood.view.ui.gui.GuiRouter;
import it.foodmood.view.ui.gui.utils.BaseGui;
import it.foodmood.view.ui.gui.utils.PasswordToggleController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class GuiRegistrationView extends BaseGui {

    @FXML private Button btnBackToLogin;

    @FXML private Button btnCreateAccount;

    @FXML private Label errorMessageLabel;

    @FXML private ImageView ivTogglePassword;

    @FXML private ImageView ivToggleConfirmPassword;

    @FXML private PasswordField pfConfirmPassword;

    @FXML private PasswordField pfPassword;

    @FXML private TextField tfEmail;

    @FXML private TextField tfName;

    @FXML private TextField tfPasswordVisible;

    @FXML private TextField tfConfirmPasswordVisible;

    @FXML private TextField tfSurname;

    private GuiRouter router;

    private PasswordToggleController passwordToggleController;
    private PasswordToggleController confirmPasswordToggleController;

    public GuiRegistrationView(){
        // costruttore vuooto
    }

    public void setRouter(GuiRouter router){
        this.router = router;
    }

    @FXML
    private void initialize(){
        passwordToggleController = new PasswordToggleController(pfPassword, tfPasswordVisible, ivTogglePassword);
        confirmPasswordToggleController = new PasswordToggleController(pfConfirmPassword, tfConfirmPasswordVisible, ivToggleConfirmPassword);
    }

    @FXML
    private void onTogglePasswordClicked(){
        passwordToggleController.toggle();
    }

    @FXML
    private void onToggleConfirmPasswordClicked(){
        confirmPasswordToggleController.toggle();
    }

    @FXML
    private void onCreateAccountClicked(){
        errorMessageLabel.setText("");

        String name = tfName.getText();
        String surname = tfSurname.getText();
        String email = tfEmail.getText();
        String password = passwordToggleController.getPassword();
        String confirmPassword = confirmPasswordToggleController.getPassword();

        try {
            RegistrationBean registrationBean = new RegistrationBean();

            registrationBean.setName(name);
            registrationBean.setSurname(surname);
            registrationBean.setEmail(email);
            registrationBean.setPassword(password.toCharArray());
            registrationBean.setConfirmPassword(confirmPassword.toCharArray());

            CustomerRegistrationController controller = new CustomerRegistrationController();

            controller.registration(registrationBean);
            showInfo("Registrazione effettuata con successo");
            router.showLoginView(true);
        } catch (IllegalArgumentException e){
            errorMessageLabel.setText(e.getMessage());
        } catch (RegistrationException e) {
            errorMessageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onBackToLoginClicked(){
        router.showLoginView(true);
    }
}
