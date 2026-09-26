module it.foodmood {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Database
    requires java.sql;
    
    opens it.foodmood.view.ui.gui to javafx.fxml;
    opens it.foodmood.view.ui.gui.customer to javafx.fxml;
    opens it.foodmood.view.ui.gui.waiter to javafx.fxml;
    opens it.foodmood.view.ui.gui.manager to javafx.fxml;
    opens it.foodmood.view.ui.gui.utils to javafx.fxml;

    opens it.foodmood to javafx.graphics;
}
