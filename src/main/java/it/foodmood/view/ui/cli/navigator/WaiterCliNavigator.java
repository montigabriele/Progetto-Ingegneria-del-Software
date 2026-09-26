package it.foodmood.view.ui.cli.navigator;

import it.foodmood.view.ui.cli.TableConsoleView;

public class WaiterCliNavigator extends TableConsoleView implements CliNavigator {

    @Override
    public void start(){
        showWarning("Schermata non implementata in versione CLI");
    }
}
