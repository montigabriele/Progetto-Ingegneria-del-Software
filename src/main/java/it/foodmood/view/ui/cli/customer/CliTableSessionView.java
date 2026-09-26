package it.foodmood.view.ui.cli.customer;

import it.foodmood.bean.TableSessionBean;
import it.foodmood.controller.TableSessionController;
import it.foodmood.exception.TableException;
import it.foodmood.exception.TableSessionException;
import it.foodmood.view.ui.cli.ConsoleView;

public class CliTableSessionView extends ConsoleView {

    private static final String TITLE = "TAVOLO";

    private final TableSessionController controller = new TableSessionController();

    public CliTableSessionView(){
        super();
    }

    public TableSessionBean displayPage(){
        clearScreen();
        while(true){
            showTitle(TITLE);
            try {
                int tableNumber = askPositiveInt("Inserisci il numero del tuo tavolo: ");

                return controller.enterSession(tableNumber);
                
            } catch (TableException e) {
                clearScreen();
                showError(e.getMessage());
                showInfo("Riprova\n");
            } catch (TableSessionException e){
                showError(e.getMessage());
            }
        }
    }
}
