package it.foodmood.view.ui.gui.customer;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import it.foodmood.bean.ActorBean;
import it.foodmood.bean.CartItemBean;
import it.foodmood.controller.CartController;
import it.foodmood.controller.CustomerOrderController;
import it.foodmood.exception.CartException;
import it.foodmood.exception.OrderException;
import it.foodmood.view.ui.gui.GuiRouter;
import it.foodmood.view.ui.gui.utils.BaseGui;
import it.foodmood.view.ui.gui.utils.ButtonDelete;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class GuiCustomerCart extends BaseGui {

    @FXML private Label lblTotalOrder;

    @FXML private TableView<CartItemBean> tblOrder;

    @FXML private TableColumn<CartItemBean, String> colProduct;

    @FXML private TableColumn<CartItemBean, Integer> colQty;

    @FXML private TableColumn<CartItemBean, Void> colDelete;

    @FXML private TableColumn<CartItemBean, String> colPrice;

    @FXML private Button btnAccount;

    @FXML private Button btnCart;

    @FXML private Button btnOrder;

    @FXML private BorderPane cardPane;

    @FXML private Label lblUserInitials;

    private final NumberFormat currency =
            NumberFormat.getCurrencyInstance(Locale.ITALY);

    private final CartController cartController =
            new CartController();

    private final ObservableList<CartItemBean> observableItems =
            FXCollections.observableArrayList();

    private GuiRouter router;

    @FXML
    private void initialize() {

        tblOrder.setItems(observableItems);

        colProduct.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(col.getValue().getProductName()));

        colQty.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(col.getValue().getQuantity()));

        colPrice.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(currency.format(col.getValue().getSubTotal())));

        colDelete.setCellFactory(tabCell -> new ButtonDelete(line -> removeItem(line.getDishId())));
    }

    public void setRouter(GuiRouter router) {

        this.router = router;

        updateLabel();
        showOrderRecap();
    }

    private void showOrderRecap() {

        if (!hasOrderContext()) {
            observableItems.clear();
            updateTotalLabel(BigDecimal.ZERO);
            return;
        }

        try {

            List<CartItemBean> items = cartController.getCartItems(router.getActor().getActorId(), router.getTableSession().getTableSessionId());
            observableItems.setAll(items);
            updateTotal();

        } catch (CartException e) {
            showError(e.getMessage());
            observableItems.clear();
            updateTotalLabel(BigDecimal.ZERO);
        }
    }

    @FXML
    void onAccountClicked(ActionEvent event) {
        ActorBean actor = router.getActor();
        if (!actor.isGuest()) {
            router.showCustomerAccountView();
        } else {
            showInfo( "Effettua l'accesso per vedere la sezione Account");
        }
    }

    @FXML
    void onBackToHomePage(MouseEvent event) {
        router.showHomeCustumerView();
    }

    @FXML
    void onCartClicked(ActionEvent event) {
        showOrderRecap();
    }

    @FXML
    void onOrder(ActionEvent event) {
        if (!hasOrderContext()) {
            showError("Contesto ordine non valido");
            return;
        }

        try {
            CustomerOrderController orderController = new CustomerOrderController();

            String orderId = orderController.createOrder(router.getActor().getActorId(),router.getTableSession().getTableSessionId());
            showInfo( "Ordine creato correttamente.\nID: " + orderId);
            showOrderRecap();

        } catch (OrderException e) {
            showError(e.getMessage());
        }
    }

    private void updateLabel() {
        if (router == null) {
            return;
        }

        String initials = getUserInitials( router.getActor());
        if (lblUserInitials != null) {
            lblUserInitials.setText(initials);
        }
    }

    private void removeItem(String dishId) {

        if (!hasOrderContext()) {
            showError("Contesto ordine non valido");
            return;
        }

        try {
            cartController.removeFromCart(router.getActor().getActorId(),router.getTableSession().getTableSessionId(),dishId);
            showOrderRecap();
        } catch (CartException e) {
            showError(e.getMessage());
        }
    }

    private void updateTotal() {

        if (lblTotalOrder == null) {
            return;
        }
        if (!hasOrderContext()) {
            updateTotalLabel(BigDecimal.ZERO);
            return;
        }
        try {
            BigDecimal total = cartController.getTotal( router.getActor().getActorId(), router.getTableSession().getTableSessionId());
            updateTotalLabel(total);

        } catch (CartException e) {

            showError(e.getMessage());

            updateTotalLabel(BigDecimal.ZERO);
        }
    }

    private void updateTotalLabel( BigDecimal total) {

        if (lblTotalOrder != null) {
            lblTotalOrder.setText(currency.format(total));
        }
    }

    private boolean hasOrderContext() {
        return router != null && router.getActor() != null && router.getActor().getActorId() != null && router.getTableSession() != null && router.getTableSession().getTableSessionId() != null;
    }
}