package it.foodmood.view.ui.gui.utils;

import java.util.Optional;
import java.util.function.Function;

import it.foodmood.bean.ActorBean;
import it.foodmood.view.ui.gui.GuiRouter;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;

public abstract class BaseGui {

    protected boolean ensureActorContext(GuiRouter router) {

        if (router == null
                || router.getActor() == null
                || router.getActor().getActorId() == null) {

            showError("Contesto utente non valido.");

            if (router != null) {
                router.showLoginView();
            }

            return false;
        }

        return true;
    }

    protected String getUserInitials(ActorBean actor){
        if(actor == null) return "";

        String name = actor.getName();
        String surname = actor.getSurname();

        String n = (name != null && !name.isEmpty()) ? name.substring(0,1).toUpperCase() : "";
        String s = (surname != null && !surname.isEmpty()) ? surname.substring(0,1).toUpperCase() : "";
        return n + s;
    }

    protected String getUserFullName(ActorBean actor){
        if(actor == null) return "";

        String name = actor.getName();
        String surname = actor.getSurname();

        StringBuilder stringBuilder = new StringBuilder();

        if(name != null && !name.isBlank()){
            stringBuilder.append(name.trim());
        }
        if(surname != null && !surname.isBlank()){
            stringBuilder.append(" ");
            stringBuilder.append(surname.trim());
        } 
        return stringBuilder.toString();
    }

    protected void showError(String message){
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }

    protected void showInfo(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informazione");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected boolean showConfirmation(String title, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        var result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    protected void swap(Node nodeShow, Node nodeHide){
        if(nodeShow != null){
            nodeShow.setVisible(true);
            nodeShow.setManaged(true);
        }
        if(nodeHide != null){
            nodeHide.setVisible(false);
            nodeHide.setManaged(false);
        }
    }
    
    protected <E> void setupComboBox(ComboBox<E> comboBox, E[] values, Function<E, String> description){
        comboBox.getItems().setAll(values);

        comboBox.setCellFactory(combo -> new ListCell<>(){
            @Override
            protected void updateItem(E item, boolean empty){
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : description.apply(item));
            }
        });

        comboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(E item, boolean empty){
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : description.apply(item));
            }
        });
    }

    protected <T> void setupListCell(ListView<T> listView, Function<T, String> formatter){
        listView.setCellFactory(view -> new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty){
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : formatter.apply(item));
            }
        });
    }

    protected <T> void setupColumn(TableColumn<T, String> column, Function<T, String> extract){
        column.setCellValueFactory(cell -> new SimpleStringProperty(Optional.ofNullable(extract.apply(cell.getValue())).orElse("")));
    }
}
