package it.foodmood.view.ui.gui.utils;

import java.util.function.Consumer;

import it.foodmood.bean.CartItemBean;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class ButtonDelete extends TableCell<CartItemBean, Void> {
                
    private final Button btn = new Button();
    private final ImageView icon;
    private final Consumer<CartItemBean> onDelete;
    private static final String TRASH = "/icons/trash.png";

    public ButtonDelete(Consumer<CartItemBean> onDelete){
        this.onDelete = onDelete;

        String path = TRASH;
        Image img = new Image(getClass().getResourceAsStream(path));
        icon = new ImageView(img);

        icon.setFitHeight(20);
        icon.setFitWidth(20);
        icon.setPreserveRatio(true);
        btn.setGraphic(icon);
        btn.setFocusTraversable(false);
        btn.setTooltip(new Tooltip("Elimina riga"));
        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        btn.setOnAction(e -> {
            CartItemBean line = getCurrentLine();
            if(line != null) this.onDelete.accept(line);
        });
    }
    
    private CartItemBean getCurrentLine(){
        int index = getIndex();
        if(index < 0 || getTableView() == null || index >= getTableView().getItems().size()) return null;
        return getTableView().getItems().get(index);
    }

    @Override
    protected void updateItem(Void item, boolean empty){
        super.updateItem(item, empty);
        setText(null);
        setStyle("");
        setGraphic(empty ? null : btn);
    }
}

