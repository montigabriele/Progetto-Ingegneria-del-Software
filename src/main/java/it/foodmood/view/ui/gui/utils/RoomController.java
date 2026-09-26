package it.foodmood.view.ui.gui.utils;

import java.util.HashMap;
import java.util.Map;

import it.foodmood.bean.RestaurantRoomBean;
import it.foodmood.bean.TableBean;
import it.foodmood.controller.RestaurantRoomController;
import it.foodmood.exception.RestaurantRoomException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public abstract class RoomController extends BaseGui {

    protected RestaurantRoomBean currentRoom;

    protected final Map<Integer, ImageView> tableNodes = new HashMap<>();

    protected TableBean selectedTable;
    protected ImageView selectedNode;

    protected double cellWidth = 100.0;
    protected double cellHeight = 100.0;

    @FXML protected Label lblSelectedTable;

    @FXML protected Label lblSelectedTableState;

    @FXML private Label lblFreeTables;

    @FXML private Label lblOccupiedTables;

    @FXML private Label lblReservedTables;

    // Ogni sottoclasse utilizzerà il proprio Pane
    protected abstract Pane getRoomPane();
 
    protected abstract RestaurantRoomController getController();

    protected void initRoomController(){
        Pane pane = getRoomPane();

        pane.widthProperty().addListener((obs, oldV, newV) -> updateLayout());
        pane.heightProperty().addListener((obs, oldV, newV) -> updateLayout());

        loadRestaurantRoom();
    }

    protected void loadRestaurantRoom(){
        try {
            currentRoom = getController().loadRestaurantRoom();
            renderRoom(currentRoom);
            onRoomLoaded();
        } catch (RestaurantRoomException e) {
            showError(e.getMessage());
        }
    }

    protected void onRoomLoaded(){
        updateTableCounter();
    }

    protected void renderRoom(RestaurantRoomBean restaurantRoomBean){
        Pane pane = getRoomPane();

        pane.getChildren().clear();
        tableNodes.clear();
        selectedTable = null;
        selectedNode = null;
        if(lblSelectedTable != null){
            lblSelectedTable.setText("-");
        }

        if(restaurantRoomBean == null) return;

        currentRoom = restaurantRoomBean;

        for(TableBean tableBean : restaurantRoomBean.getTables()){
            ImageView node = createTableNode(tableBean);
            pane.getChildren().add(node);
            tableNodes.put(tableBean.getId(), node);
        }
        updateLayout();
    }

    protected ImageView createTableNode(TableBean tableBean){
        Image image = getImageForTable(tableBean);

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        imageView.setUserData(tableBean);
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> selectTable(tableBean, imageView));

        return imageView;
    }

    protected void updateLayout(){
        if(currentRoom == null) return;

        Pane pane = getRoomPane();
        
        double paneWidth = pane.getWidth() > 0 ? pane.getWidth() : pane.getPrefWidth();
        double paneHeight = pane.getHeight() > 0 ? pane.getHeight() : pane.getPrefHeight();

        int rows = currentRoom.getRows();
        int cols = currentRoom.getCols();

        cellWidth = paneWidth / cols;
        cellHeight = paneHeight / rows;

        double factor = 0.9;
        double iconSize = Math.min(cellWidth, cellHeight) * factor;

        for(TableBean tableBean : currentRoom.getTables()){
            ImageView node = tableNodes.get(tableBean.getId());
            if(node == null) continue;

            node.setFitWidth(iconSize);
            node.setFitHeight(iconSize);

            double x = tableBean.getCol() * cellWidth + (cellWidth - iconSize) / 2;
            double y = 20 + tableBean.getRow() * cellHeight + (cellHeight - iconSize) / 2;

            node.setLayoutX(x);
            node.setLayoutY(y);
        }
    }

    protected Image getImageForTable(TableBean tableBean){
        String suffix = null;

        switch (tableBean.getStatus()) {
            case FREE:
                suffix = "_free";
                break;
            case BOOKED:
                suffix = "_booked";
                break;
            case OCCUPIED:
                suffix = "_occupied";
                break;
        }

        String fileName = "table_" + tableBean.getSeats() + suffix + ".png";
        String path = "/tables/" + fileName;

        return new Image(getClass().getResourceAsStream(path));
    }

    protected void selectTable(TableBean table, ImageView node){
        if(selectedNode != null) {
            selectedNode.setStyle("");
        }

        node.setStyle("-fx-effect: dropshadow(gaussian, yellow, 15, 0.5, 0, 0)");
        
        selectedNode = node;
        selectedTable = table;

        lblSelectedTable.setText("Tavolo: " + table.getId() + " - " + table.getSeats() + " posti");
        if(lblSelectedTableState != null){
            lblSelectedTableState.setText(table.getStatus().name());
        }
    }

    protected void updateTableCounter(){

        if(lblFreeTables == null || lblOccupiedTables == null || lblReservedTables == null){
            return;
        }

        if(currentRoom == null || currentRoom.getTables() == null){
            lblFreeTables.setText("0");
            lblReservedTables.setText("0");
            lblOccupiedTables.setText("0");
            return;
        }

        int free = 0;
        int booked = 0;
        int occupied = 0;

        for(TableBean t: currentRoom.getTables()){
            switch ((t.getStatus())) {
                case FREE:
                    free++;
                    break;
                case BOOKED:
                    booked++;
                    break;
                case OCCUPIED:
                    occupied++;
                    break;
            }
        }

        lblFreeTables.setText(String.valueOf(free));
        lblOccupiedTables.setText(String.valueOf(occupied));
        lblReservedTables.setText(String.valueOf(booked));
    }
}
