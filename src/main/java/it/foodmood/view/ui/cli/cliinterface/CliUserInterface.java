package it.foodmood.view.ui.cli.cliinterface;

public interface CliUserInterface extends UserInterface {
    void showTitle(String message);
    void showSeparator(String message); 
    void waitForEnter(String message); 
    void clearScreen();
}
