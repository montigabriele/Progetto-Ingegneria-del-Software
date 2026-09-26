package it.foodmood.view.ui.cli.manager;

import it.foodmood.bean.ActorBean;
import it.foodmood.view.ui.cli.ConsoleView;
import it.foodmood.view.ui.cli.pages.ManagerPages;

public class CliManagerMenuView extends ConsoleView {

    public CliManagerMenuView(){
        super();
    }

    public ManagerPages displayPage(ActorBean actorBean){
        clearScreen();
        while(true){
            showTitle("Menù Manager");
            showInfo("Utente: " + actorBean.getName() + " " + actorBean.getSurname() + "\n");

            showInfo("1. Gestisci Ingredienti");
            showInfo("2. Gestisci Piatti");
            showInfo("3. Logout");
            showInfo("4. Esci");

            String choice = askInput("\nSeleziona un'opzione: ");

            switch(choice){
                case "1": return ManagerPages.MANAGMENT_INGREDIENTS;
                case "2": return ManagerPages.MANAGMENT_DISH;
                case "3": return ManagerPages.LOGOUT;
                case "4": return ManagerPages.EXIT;
                default : showError("Scelta non valida, riprova.");
            }
        }
    }
}
