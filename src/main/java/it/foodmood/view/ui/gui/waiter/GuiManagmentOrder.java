package it.foodmood.view.ui.gui.waiter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import it.foodmood.bean.DishBean;
import it.foodmood.controller.MenuController;
import it.foodmood.controller.RestaurantRoomController;
import it.foodmood.domain.value.CourseType;
import it.foodmood.view.ui.gui.GuiRouter;
import it.foodmood.view.ui.gui.utils.GuiCard;
import it.foodmood.view.ui.gui.utils.RoomController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class GuiManagmentOrder extends RoomController {

    @FXML private Button btnClearOrder;

    @FXML private Button btnRequestBill;

    @FXML private Button btnSendOrder;

    @FXML private ComboBox<CourseType> categoryFilter;

    @FXML private Button freeTableBtn;

    @FXML private Label lblDateTime;

    @FXML private GridPane menuGridPane;

    @FXML private Tab menuTab;

    @FXML private ListView<?> orderList;

    @FXML private Pane roomPane;

    @FXML private TextField searchField;

    private static final String NOT_IMPLEMENTED = "Funzionalità non ancora implementata";

    @FXML
    void onClearOrder(ActionEvent event) {
        showInfo(NOT_IMPLEMENTED);
    }

    @FXML
    void onFreeTable(ActionEvent event) {
        showInfo(NOT_IMPLEMENTED);
    }

    @FXML
    void onRequestBill(ActionEvent event) {
        showInfo(NOT_IMPLEMENTED);
    }

    @FXML
    void onSendOrder(ActionEvent event) {
        showInfo(NOT_IMPLEMENTED);
    }

    @FXML
    private void initialize(){

        lblSelectedTable.setText("-");

        initRoomController();

        updateDateTimeLabel();

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> updateDateTimeLabel()), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();

        initMenuFilters();
    }

    
    @Override
    protected Pane getRoomPane(){
        return roomPane;
    }

    @Override
    protected RestaurantRoomController getController(){
        return roomCotroller;
    }

    @Override
    protected void onRoomLoaded(){
        updateTableCounter();
    }
    
    private final ObservableList<DishBean> allDishes = FXCollections.observableArrayList();

    private final RestaurantRoomController roomCotroller = new RestaurantRoomController();

    private MenuController menuController;

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private GuiRouter router;

    public void setRouter(GuiRouter router){
        this.router = router;
    }

    public void setMenuController(MenuController menuController){
        this.menuController = menuController;
        loadMenuDishes();
    }

    private void updateDateTimeLabel(){
        LocalDateTime now = LocalDateTime.now();
        String formatted = now.format(DATE_TIME);
        lblDateTime.setText(formatted);
    }

    private void initMenuFilters(){
        categoryFilter.getItems().clear();
        categoryFilter.getItems().add(null);
        categoryFilter.getItems().addAll(CourseType.values());

        setupComboBox(
            categoryFilter,
            categoryFilter.getItems().toArray(new CourseType[0]),
            item -> item == null ? "Tutte le categorie" :item.description()
        );

        categoryFilter.setValue(null);
        searchField.textProperty().addListener((obs, oldV, newV) -> refreshMenuGrid());
        categoryFilter.valueProperty().addListener((obs, oldV, newV) -> refreshMenuGrid());
    }

    private void loadMenuDishes(){
        try {
            List<DishBean> dishes = menuController.loadAllDishes();
            allDishes.setAll(dishes);
            refreshMenuGrid();
        } catch (Exception e) {
            showError("Errore durante il caricamente dei piatti: " + e.getMessage());
        }
    }

    private void refreshMenuGrid(){
        menuGridPane.getChildren().clear();

        String text = searchField.getText();
        String search = (text == null) ? "" : text.trim().toUpperCase();

        CourseType selectedType = categoryFilter.getValue();

        List<DishBean> filtered = allDishes.stream()
            .filter(d -> selectedType == null || d.getCourseType() == selectedType)
            .filter(d -> {
                if(search.isEmpty()) return true;
                String name = d.getName() != null ? d.getName() : "" ;
                String desc = d.getDescription() != null ? d.getDescription() : "";
                return name.toUpperCase().contains(search) || desc.toUpperCase().contains(search);
            }).toList();
        
        int card = 3;
        int col = 0;
        int row = 0;

        for(DishBean dish: filtered){
            try {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/card.fxml")
                );

                AnchorPane cardNode = loader.load();
                double scale = 0.92;
                cardNode.setScaleX(scale);
                cardNode.setScaleY(scale);

                GuiCard cardController = loader.getController();
                
                cardController.setData(dish);

                menuGridPane.add(cardNode, col, row);
                GridPane.setMargin(cardNode, new Insets(5));

                col ++;
                if(col == card){
                    col = 0;
                    row++;
                }
            } catch (Exception e) {
                showError("Errore nell'inserimento dei prodotti nel menù: " + e.getMessage());
            }
        }
    }

    @FXML 
    void onBackHome(ActionEvent event){
        if(router != null){
            router.showHomeCustumerView();
        }
    }
}
