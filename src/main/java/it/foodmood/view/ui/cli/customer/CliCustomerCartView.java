package it.foodmood.view.ui.cli.customer;

import java.math.BigDecimal;
import java.util.List;

import it.foodmood.bean.ActorBean;
import it.foodmood.bean.CartItemBean;
import it.foodmood.bean.TableSessionBean;
import it.foodmood.controller.CartController;
import it.foodmood.controller.CustomerOrderController;
import it.foodmood.exception.CartException;
import it.foodmood.exception.OrderException;
import it.foodmood.view.ui.cli.TableConsoleView;

public class CliCustomerCartView extends TableConsoleView {

    public CliCustomerCartView() {
        super();
    }

    public void displayPage(
            ActorBean actorBean,
            TableSessionBean tableSessionBean
    ) {

        boolean back = false;

        while (!back) {

            clearScreen();
            showTitle("Carrello");

            if (!showOrderRecap(
                    actorBean,
                    tableSessionBean
            )) {

                showWarning(
                        "Nessun articolo nell'ordine"
                );

                waitForEnter(null);
                return;
            }

            showInfo("1. Conferma ordine");
            showInfo("2. Rimuovi articolo");
            showInfo("0. Torna al menù principale");

            String choice =
                    askInput(
                            "\nSeleziona un'opzione: "
                    );

            switch (choice) {

                case "1" ->
                        confirmOrder(
                            actorBean,
                            tableSessionBean
                        );

                case "2" ->
                        removeItem(
                                actorBean,
                                tableSessionBean
                        );

                case "0" ->
                        back = true;

                default ->
                        showError(
                                "Scelta non valida, riprova."
                        );
            }
        }
    }

    private void confirmOrder(
            ActorBean actorBean,
            TableSessionBean tableSessionBean
    ) {

        boolean choice =
                askConfirmation(
                        "Vuoi confermare il tuo ordine?"
                );

        if (!choice) {
            return;
        }

        try {

            CustomerOrderController orderController =
                    new CustomerOrderController();

            String orderId =
                    orderController.createOrder(
                            actorBean.getActorId(),
                            tableSessionBean.getTableSessionId()
                    );

            showSuccess(
                    "Ordine creato con successo!\nID: "
                            + orderId
            );

        } catch (OrderException e) {

            showError(
                    e.getMessage()
            );
        }
    }

    private boolean showOrderRecap(
            ActorBean actorBean,
            TableSessionBean tableSessionBean
    ) {

        CartController cartController =
                new CartController();

        try {

            List<CartItemBean> items =
                    cartController.getCartItems(
                            actorBean.getActorId(),
                            tableSessionBean.getTableSessionId()
                    );

            if (items.isEmpty()) {
                return false;
            }

            BigDecimal total =
                    cartController.getTotal(
                            actorBean.getActorId(),
                            tableSessionBean.getTableSessionId()
                    );

            showOrderRecapTable(items);

            showBold(
                    "Totale: "
                            + total
                            + "€\n"
            );

            return true;

        } catch (CartException e) {

            showWarning(
                    e.getMessage()
            );

            return false;
        }
    }

    private void removeItem(
            ActorBean actorBean,
            TableSessionBean tableSessionBean
    ) {

        CartController cartController =
                new CartController();

        try {

            List<CartItemBean> items =
                    cartController.getCartItems(
                            actorBean.getActorId(),
                            tableSessionBean.getTableSessionId()
                    );

            if (items.isEmpty()) {

                showWarning(
                        "Nessun articolo da rimuovere"
                );

                return;
            }

            clearScreen();
            showTitle("Rimuovi articolo");

            for (int i = 0; i < items.size(); i++) {

                CartItemBean item =
                        items.get(i);

                showInfo(
                        (i + 1)
                                + ". "
                                + item.getProductName()
                );
            }

            int index =
                    askPositiveInt(
                            "\nSeleziona l'articolo da rimuovere: "
                    );

            if (index > items.size()) {

                showWarning(
                        "Articolo non valido"
                );

                waitForEnter(null);
                return;
            }

            CartItemBean selected =
                    items.get(index - 1);

            boolean confirmed =
                    askConfirmation(
                            "Vuoi rimuovere '"
                                    + selected.getProductName()
                                    + "' dal carrello?"
                    );

            if (!confirmed) {
                return;
            }

            cartController.removeFromCart(
                    actorBean.getActorId(),
                    tableSessionBean.getTableSessionId(),
                    selected.getDishId()
            );

            showSuccess(
                    "Articolo rimosso dal carrello."
            );

        } catch (CartException e) {

            showWarning(
                    e.getMessage()
            );
        }
    }
}