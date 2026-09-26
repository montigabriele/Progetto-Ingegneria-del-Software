package it.foodmood.view.ui.cli;

public class CliLogoutView extends ConsoleView {

    public CliLogoutView(){
        super();
    }

    public boolean displayPage(){

        boolean choice =
                askConfirmation(
                        "Vuoi uscire dal tuo account?"
                );

        if(choice){

            clearScreen();

            showSuccess(
                    "Logout completato\n"
            );

            waitForEnter(null);

            return true;
        }

        waitForEnter(
                "Operazione annullata. Premi INVIO per continuare "
        );

        return false;
    }
}