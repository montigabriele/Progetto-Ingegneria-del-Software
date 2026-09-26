package it.foodmood.view.ui.cli.customer;

import it.foodmood.view.ui.cli.ConsoleView;
import it.foodmood.view.ui.cli.pages.MenuCustomerPages;

public class CliCustomerMenuView extends ConsoleView {

    public CliCustomerMenuView(){
        super();
    }

    public MenuCustomerPages displayPage(){
        clearScreen();
        while(true){
            showTitle("Ristorante il Casale");

            showInfo("1. Login");
            showInfo("2. Registrazione");
            showInfo("3. Continua come ospite");
            showInfo("4. Esci");

            String choice = askInput("\nSeleziona un'opzione: ");

            switch(choice){
                case "1": return MenuCustomerPages.LOGIN;
                case "2": return MenuCustomerPages.REGISTRATION;
                case "3": return MenuCustomerPages.GUEST;
                case "4": return MenuCustomerPages.EXIT;
                default : showError("Scelta non valida, riprova.");
            }
        }
    }
}
