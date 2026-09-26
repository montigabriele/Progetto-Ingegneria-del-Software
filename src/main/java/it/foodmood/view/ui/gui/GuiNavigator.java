package it.foodmood.view.ui.gui;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class GuiNavigator {
    private final Scene scene;

    public GuiNavigator(Scene scene){
        this.scene = scene;
    }

    public <T> T goTo(GuiPages page){
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(page.getPath())));
            Parent root = loader.load();

            scene.setRoot(root);

            return loader.getController(); // Restiuisco il controller della pagina
        } catch (IOException e) {
            throw new IllegalStateException("Errore nel caricamento della pagina: " + page, e);
        }
    }
}
