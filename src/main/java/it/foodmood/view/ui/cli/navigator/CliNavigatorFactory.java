package it.foodmood.view.ui.cli.navigator;

import it.foodmood.config.UserMode;
import it.foodmood.view.ui.cli.CliFactory;
import it.foodmood.view.ui.cli.customer.CustomerCliNavigator;
import it.foodmood.view.ui.cli.manager.ManagerCliNavigator;
import it.foodmood.view.ui.cli.waiter.WaiterCliNavigator;

public class CliNavigatorFactory {

    private CliNavigatorFactory() {
        // Costruttore vuoto
    }

    public static CliNavigator create(UserMode mode, CliFactory factory){
        return switch(mode){
            case CUSTOMER -> new CustomerCliNavigator(factory);
            case WAITER -> new WaiterCliNavigator(factory);
            case MANAGER -> new ManagerCliNavigator(factory);
        };
    }
}
