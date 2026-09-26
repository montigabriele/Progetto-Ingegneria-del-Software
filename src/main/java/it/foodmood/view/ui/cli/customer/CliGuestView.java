package it.foodmood.view.ui.cli.customer;

import it.foodmood.bean.ActorBean;
import it.foodmood.controller.GuestAccessController;
import it.foodmood.view.ui.cli.ConsoleView;

public class CliGuestView extends ConsoleView {
    
    private final GuestAccessController controller = new GuestAccessController();

    public CliGuestView(){
        super();
    }

    public ActorBean displayPage(){
        clearScreen();
        return controller.enterAsGuest();
    }
}
