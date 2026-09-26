package it.foodmood.view.ui.cli.cliinterface;

public interface UserInterface {
    void showInfo(String message);
    void showBold(String message);
    void showError(String message);
    void showSuccess(String message);
    void showWarning(String message);
}
