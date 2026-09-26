package it.foodmood.view.ui.gui.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Consumer;

import it.foodmood.bean.DishBean;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class GuiCard {
    
    @FXML private AnchorPane card;

    @FXML private Button productAddBtn;

    @FXML private ImageView productImageView;

    @FXML private Label productName;

    @FXML private Label productPrice;

    private Consumer<DishBean> onAddToOrder;

    public void setOnAddToOrder(Consumer<DishBean> onAddToOrder){
        this.onAddToOrder = onAddToOrder;
    }
    
    public void setData(DishBean dishBean){

        productName.setText(dishBean.getName());

        BigDecimal price = dishBean.getPrice().setScale(2, RoundingMode.HALF_UP);
        NumberFormat number = NumberFormat.getCurrencyInstance(Locale.ITALY);
        productPrice.setText(number.format(price));

        if(dishBean.getImageUri() != null){
            try {
                Image image = new Image(dishBean.getImageUri(), true);
                productImageView.setImage(image);
            } catch (Exception _) {
                productImageView.setImage(null);
            }
        } else {
            productImageView.setImage(null);
        }

        productAddBtn.setDisable(false);

        productAddBtn.setOnAction(e -> {
            if(onAddToOrder != null) {
                onAddToOrder.accept(dishBean);
            }
        });
    }
}
