package it.foodmood.view.ui.cli.customer;

import it.foodmood.bean.ActorBean;
import it.foodmood.bean.TableSessionBean;
import it.foodmood.view.ui.cli.TableConsoleView;
import it.foodmood.view.ui.cli.pages.HomeCustomerPages;

public class CliCustomerHomeView extends TableConsoleView {

    public CliCustomerHomeView(){
        super();
    }

    public HomeCustomerPages displayPage(ActorBean actorBean, TableSessionBean tableSessionBean){
        while(true){
            clearScreen();
            boolean logged = actorBean.isLogged();
            showTitle("Benvenuto al Ristorante il Casale");
            showBold("\nScopri il piacere di ordinare su misura per te\n");
            showInfo("Tavolo: " + tableSessionBean.getTableId());
            showInfo("Utente: " + actorBean.getName() + " " + actorBean.getSurname() + "\n");

            showInfo("1. Ordina con suggerimenti");
            showInfo("2. Menù digitale");
            showInfo("3. Carrello");
            showInfo("4. Chiama un cameriere");
            showInfo("5. Richiedi il conto");
            showInfo("6. Account");
            if(logged)  showInfo("7. Logout");
            showInfo("0. Esci");

            String choice = askInput("\nSeleziona un'opzione: ");

            switch(choice){
                case "1": return HomeCustomerPages.ORDER_CUSTOMIZATION;
                case "2": return HomeCustomerPages.DIGITAL_MENU;
                case "3": return HomeCustomerPages.RECAP_ORDER;
                case "4": return HomeCustomerPages.CALL_WAITER;
                case "5": return HomeCustomerPages.REQUIRE_BILL;
                case "6": return HomeCustomerPages.ACCOUNT;
                case "7":
                    if(logged) return HomeCustomerPages.LOGOUT;
                    break;
                case "0": 
                    return HomeCustomerPages.EXIT;
                default: 
                    showError("Scelta non valida, riprova.");
            }
        }
    }
}
