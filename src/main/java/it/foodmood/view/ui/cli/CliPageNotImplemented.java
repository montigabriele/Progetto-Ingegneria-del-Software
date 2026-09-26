package it.foodmood.view.ui.cli;

public class CliPageNotImplemented extends ConsoleView {
    public void displayPage(){
        showWarning("Funzionalità non ancora implementata.");
        waitForEnter(null);
        clearScreen();
    }
}
