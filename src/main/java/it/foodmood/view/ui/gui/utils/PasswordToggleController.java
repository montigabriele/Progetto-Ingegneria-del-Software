package it.foodmood.view.ui.gui.utils;

import java.net.URL;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PasswordToggleController {
    private final PasswordField passwordField;
    private final TextField visibleField;
    private final ImageView toggleIcon;

    private boolean visible = false;

    public PasswordToggleController(PasswordField passwordField, TextField visibleField, ImageView toggleIcon){
        this.passwordField = passwordField;
        this.visibleField = visibleField;
        this.toggleIcon = toggleIcon;

        initialize();
    }

    private void initialize(){
        // stato iniziale password nascosta
        visibleField.setVisible(false);
        visibleField.setManaged(false);

        visibleField.managedProperty().bind(visibleField.visibleProperty());
        passwordField.managedProperty().bind(passwordField.visibleProperty());

        visibleField.textProperty().bindBidirectional(passwordField.textProperty());

        updateToggleIcon();
    }

    public void toggle(){
        setVisible(!visible);
    }

    private void setVisible(boolean visible){
        this.visible = visible;

        visibleField.setVisible(visible);
        passwordField.setVisible(!visible);

        updateToggleIcon();

        if(visible){
            visibleField.requestFocus();
            visibleField.positionCaret(visibleField.getText().length());
        } else {
            passwordField.requestFocus();
            passwordField.positionCaret(passwordField.getText().length());
        }
    }

    private void updateToggleIcon(){
        String iconName = visible ? "eye.png" : "eye_hidden.png";
        String path = "/icons/" + iconName;
        URL url = getClass().getResource(path);
        if(url == null){
            System.err.print("Icona non trovata: " + path);
            return;
        }
        Image img = new Image(url.toExternalForm());
        toggleIcon.setImage(img);
    }

    public String getPassword(){
        return visible ? visibleField.getText() : passwordField.getText();
    }
}
