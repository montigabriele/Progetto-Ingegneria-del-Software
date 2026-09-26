package it.foodmood.view.ui.cli.waiter;

import it.foodmood.view.ui.cli.ConsoleView;

public class CliWaiterMenuView extends ConsoleView{
    
    public CliWaiterMenuView(){
        super();
    }

    public void displayPage(){
        clearScreen();
        showTitle("Benvenuto in FoodMood");

        showWarning("Modalità cameriere non implementata in modalità CLI.");
        waitForEnter(null);
    }
}
