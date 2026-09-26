package it.foodmood.view.ui.gui.manager;

import it.foodmood.controller.DishController;
import it.foodmood.controller.IngredientController;
import it.foodmood.view.ui.gui.GuiPages;
import it.foodmood.view.ui.gui.GuiRouter;
import it.foodmood.view.ui.gui.utils.BaseGui;
import it.foodmood.view.ui.gui.utils.PaneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class GuiHomeManager extends BaseGui {

    @FXML private Button btnHome;
    @FXML private Button btnLogout;
    @FXML private Button btnManagmentBooking;
    @FXML private Button btnManagmentRestaurantRoom;
    @FXML private Button btnManagmentDishes;
    @FXML private Button btnManagmentEmployees;
    @FXML private Button btnManagmentIngredients;

    @FXML private AnchorPane contentArea;
    @FXML private Label lblManager;
    @FXML private AnchorPane mainForm;

    private final IngredientController ingredientController = new IngredientController();

    private final DishController dishController = new DishController();

    private GuiRouter router;
    private PaneNavigator paneNavigator;

    public GuiHomeManager() {
        // costruttore vuoto
    }

    @FXML
    private void initialize() {

        paneNavigator = new PaneNavigator(contentArea);
    }

    public void setRouter(GuiRouter router) {
        this.router = router;
        updateLabel();
    }

    private void updateLabel() {

        if (lblManager != null && router != null && router.getActor() != null) {
            lblManager.setText( getUserFullName( router.getActor()));
        }
    }

    @FXML
    void switchForm(ActionEvent event) {
        if (!ensureActorContext(router)) {
            return;
        }
        Object source = event.getSource();
        if (source == btnHome) {
            openDefaultPage();
        } else if (source == btnManagmentIngredients) {
            GuiManagmentIngredients controller = paneNavigator.show( GuiPages.MANAGMENT_INGREDIENTS);
            controller.setIngredientController(ingredientController);
        } else if (source == btnManagmentDishes) {
            GuiManagmentDish controller = paneNavigator.show( GuiPages.MANAGMENT_DISH);
            controller.setIngredientController( ingredientController);
            controller.setDishController(dishController);
        } else if (source == btnManagmentRestaurantRoom) {
            GuiManagmentRestaurantRoom controller = paneNavigator.show( GuiPages.MANAGMENT_ROOM_RESTAURANT);
            controller.setRouter(router);
        } else {
            showInfo("Funzionalità non ancora implementata");
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

    public void openDefaultPage() {

        GuiManagmentDish controller = paneNavigator.show( GuiPages.MANAGMENT_DISH);
        controller.setIngredientController(ingredientController);
        controller.setDishController(dishController);
    }
}