package it.foodmood.view.ui.gui.waiter;

import it.foodmood.controller.MenuController;
import it.foodmood.view.ui.gui.GuiPages;
import it.foodmood.view.ui.gui.GuiRouter;
import it.foodmood.view.ui.gui.utils.BaseGui;
import it.foodmood.view.ui.gui.utils.PaneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class GuiHomeWaiter extends BaseGui {

    @FXML private Button btnBillManagment;
    @FXML private Button btnDishStatus;
    @FXML private Button btnHome;
    @FXML private Button btnLogout;
    @FXML private Button btnManagmentBooking;
    @FXML private Button btnOrderManagment;

    @FXML private AnchorPane contentArea;
    @FXML private Label lblWaiter;
    @FXML private AnchorPane mainForm;

    private final MenuController menuController = new MenuController();

    private GuiRouter router;
    private PaneNavigator paneNavigator;

    public GuiHomeWaiter() {
        // costruttore vuoto
    }

    @FXML
    private void initialize() {
        paneNavigator = new PaneNavigator(contentArea);
    }

    public void setRouter(GuiRouter router) {

        this.router = router;

        updateLabel();
        showOrderManagement();
    }

    private void updateLabel() {
        if (lblWaiter != null && router != null && router.getActor() != null) {
            lblWaiter.setText( getUserFullName( router.getActor()));
        }
    }

    private void showOrderManagement() {
        if (router == null) {
            return;
        }
        GuiManagmentOrder controller = paneNavigator.show( GuiPages.MANAGMENT_ORDER);
        controller.setRouter(router);
        controller.setMenuController( menuController );
    }

    @FXML
    void switchForm(ActionEvent event) {

        if (router == null
                || router.getActor() == null) {
            return;
        }

        Object source = event.getSource();

        if (source == btnOrderManagment) {
            showOrderManagement();
        } else {
            showInfo( "Funzionalità non ancora implementata");
        }
    }

    @FXML
    void onLogoutClicked(ActionEvent event) {
        if (router == null) {
            return;
        }
        router.clearContext();
        router.showLoginView();
    }
}