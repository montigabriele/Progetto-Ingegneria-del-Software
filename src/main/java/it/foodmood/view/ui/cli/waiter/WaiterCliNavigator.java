package it.foodmood.view.ui.cli.waiter;

import it.foodmood.view.ui.WaiterUi;
import it.foodmood.view.ui.cli.navigator.CliNavigator;

public class WaiterCliNavigator implements CliNavigator {

    private final WaiterUi ui;

    public WaiterCliNavigator(WaiterUi ui){
        this.ui = ui;
    }

    @Override
    public void start(){
        ui.showHomeWaiterView();
    }
}
