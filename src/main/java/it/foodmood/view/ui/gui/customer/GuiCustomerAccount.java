package it.foodmood.view.ui.gui.customer;

import it.foodmood.bean.ActorBean;
import it.foodmood.domain.value.Allergen;
import it.foodmood.view.ui.gui.GuiRouter;
import it.foodmood.view.ui.gui.utils.BaseGui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class GuiCustomerAccount extends BaseGui {

    @FXML private BorderPane accountPane;
    @FXML private BorderPane preferencesPane;
    @FXML private BorderPane cardPane;
    @FXML private BorderPane passwordPane;

    @FXML private Button btnAccount;
    @FXML private Button btnCart;
    @FXML private Button btnBack;
    @FXML private Button btnBackToHome;
    @FXML private Button btnLogout;
    @FXML private Button btnChangePassword;
    @FXML private Button btnFoodPreferences;
    @FXML private Button btnFidelityCard;
    @FXML private Button btnPersonalDetails;
    @FXML private Button btnSavePersonalDetails;
    @FXML private Button btnSavePreferences;

    @FXML private Label lblFullNameCard;
    @FXML private Label lblFullNameAccount;
    @FXML private Label lblUserInitials;
    @FXML private Label errorMessageLabel;

    @FXML private BorderPane personalDetailsPane;

    @FXML private TextField tfName;
    @FXML private TextField tfSurname;

    @FXML private CheckBox cbCelery;
    @FXML private CheckBox cbCrustaceans;
    @FXML private CheckBox cbEggs;
    @FXML private CheckBox cbFish;
    @FXML private CheckBox cbGluten;
    @FXML private CheckBox cbLupin;
    @FXML private CheckBox cbMilk;
    @FXML private CheckBox cbMolluscs;
    @FXML private CheckBox cbMustard;
    @FXML private CheckBox cbNuts;
    @FXML private CheckBox cbPeanuts;
    @FXML private CheckBox cbSesame;
    @FXML private CheckBox cbSoy;
    @FXML private CheckBox cbSulphites;

    @FXML private ComboBox<Allergen> cbType;

    private GuiRouter router;

    @FXML
    private void initialize() {

        showAccountPage();

        btnPersonalDetails.setOnAction( event -> showPersonalDetailsPage());

        btnChangePassword.setOnAction(event -> showChangePasswordPage());

        btnFidelityCard.setOnAction(event -> showFidelityCardPage());

        btnFoodPreferences.setOnAction(event -> showFoodPreferencesPage());
    }

    public void setRouter(GuiRouter router) {
        this.router = router;
        updateLabel();
    }

    @FXML
    void onLogoutClicked(ActionEvent event) {

        if (router == null) {
            return;
        }

        router.clearContext();
        router.showLoginView();
    }

    @FXML
    void onBackToHome(ActionEvent event) {

        if (!ensureActorContext(router)) {
            return;
        }
        router.showHomeCustumerView();
    }

    @FXML
    void onBackToHomePage(MouseEvent event) {
        if (!ensureActorContext(router)) {
            return;
        }
        router.showHomeCustumerView();
    }

    @FXML
    void onBackToAccount(ActionEvent event) {
        if (!ensureActorContext(router)) {
            return;
        }
        showAccountPage();
    }

    @FXML
    void onCartClicked(ActionEvent event) {
        if (!ensureActorContext(router)) {
            return;
        }
        router.showCustomerRecapOrder();
    }

    @FXML
    void onAccountClicked(ActionEvent event) {
        if (!ensureActorContext(router)) {
            return;
        }
        ActorBean actor = router.getActor();

        if (!actor.isGuest()) {
            showAccountPage();
        } else {
            showInfo("Effettua l'accesso per vedere la sezione Account");
        }
    }

    private void updateLabel() {

        if (router == null || router.getActor() == null) {
            return;
        }

        ActorBean actor = router.getActor();

        String fullName = getUserFullName(actor);

        String initials = getUserInitials(actor);

        if (lblFullNameCard != null) {
            lblFullNameCard.setText(fullName);
        }

        if (lblFullNameAccount != null) {
            lblFullNameAccount.setText(fullName);
        }

        if (lblUserInitials != null) {
            lblUserInitials.setText(initials);
        }
    }

    private void showOnly(BorderPane paneToShow) {

        if (accountPane != null) {
            accountPane.setVisible(false);
            accountPane.setManaged(false);
        }

        if (personalDetailsPane != null) {
            personalDetailsPane.setVisible(false);
            personalDetailsPane.setManaged(false);
        }

        if (passwordPane != null) {
            passwordPane.setVisible(false);
            passwordPane.setManaged(false);
        }

        if (cardPane != null) {
            cardPane.setVisible(false);
            cardPane.setManaged(false);
        }

        if (preferencesPane != null) {
            preferencesPane.setVisible(false);
            preferencesPane.setManaged(false);
        }

        if (paneToShow != null) {
            paneToShow.setVisible(true);
            paneToShow.setManaged(true);
        }
    }

    private void showAccountPage() {
        showOnly(accountPane);
    }

    private void showPersonalDetailsPage() {
        showOnly(personalDetailsPane);
    }

    private void showChangePasswordPage() {
        showOnly(passwordPane);
    }

    private void showFidelityCardPage() {
        showOnly(cardPane);
    }

    private void showFoodPreferencesPage() {
        showOnly(preferencesPane);
    }
}