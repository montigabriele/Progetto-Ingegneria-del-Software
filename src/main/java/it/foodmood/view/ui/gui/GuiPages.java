package it.foodmood.view.ui.gui;

public enum GuiPages {
    LOGIN("/fxml/login.fxml"),
    REGISTRATION("/fxml/registration.fxml"),
    HOME_CUSTOMER("/fxml/home_customer.fxml"),
    CUSTOMER_ACCOUNT("/fxml/customer_account.fxml"),
    CUSTOMER_ORDER("/fxml/customer_order.fxml"),
    CUSTOMER_DIGITAL_MENU("/fxml/customer_digital_menu.fxml"),
    CUSTOMER_RECAP_ORDER("/fxml/customer_recap_order.fxml"),
    HOME_MANAGER("/fxml/home_manager.fxml"),
    HOME_WAITER("/fxml/home_waiter.fxml"),
    MANAGMENT_ROOM_RESTAURANT("/fxml/managment_restaurant_room.fxml"),
    MANAGMENT_DISH("/fxml/managment_dish.fxml"),
    MANAGMENT_ORDER("/fxml/managment_order.fxml"),
    MANAGMENT_INGREDIENTS("/fxml/managment_ingredients.fxml"),
    TABLE_SESSION("/fxml/table_session.fxml"),
    CARD("/fxml/card.fxml");

    private final String path;

    GuiPages(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }
}
