package it.foodmood.view.ui.gui.manager;

import it.foodmood.bean.RestaurantRoomBean;
import it.foodmood.bean.TableBean;
import it.foodmood.controller.RestaurantRoomController;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import it.foodmood.exception.RestaurantRoomException;
import it.foodmood.view.ui.gui.GuiRouter;
import it.foodmood.view.ui.gui.utils.RoomController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class GuiManagmentRestaurantRoom extends RoomController {
    
    @FXML private Button btnAddTable2;

    @FXML private Button btnAddTable4;

    @FXML private Button btnAddTable6;

    @FXML private Button btnAddTable8;

    @FXML private Button btnRemoveAllTables;

    @FXML private Button btnRemoveSelected;

    @FXML private Button btnSaveRoom;

    @FXML private Button btnToggleEditMode;

    @FXML private Pane roomPane;

    private final RestaurantRoomController controller = new RestaurantRoomController();

    private boolean editMode = false;

    private GuiRouter router;

    public void setRouter(GuiRouter router){
        this.router = router;
    }

    @FXML
    private void initialize(){
        btnAddTable2.setOnAction(e -> addTable(2));
        btnAddTable4.setOnAction(e -> addTable(4));
        btnAddTable6.setOnAction(e -> addTable(6));
        btnAddTable8.setOnAction(e -> addTable(8));

        btnRemoveAllTables.setOnAction(e -> removeAllTables());
        btnRemoveSelected.setOnAction(e -> removeSelected());
        btnToggleEditMode.setOnAction(e -> toggleEditMode());
        btnSaveRoom.setOnAction(e -> saveRoom());

        lblSelectedTable.setText("-");

        initRoomController();
    }

    @Override
    protected Pane getRoomPane(){
        return roomPane;
    }

    @Override
    protected RestaurantRoomController getController(){
        return controller;
    }

    @Override
    protected void onRoomLoaded(){
        super.onRoomLoaded();
        refreshDrag();
    }

    @Override
    protected ImageView createTableNode(TableBean tableBean){
        String table = "table_" + tableBean.getSeats() + ".png";
        String path = "/tables/" + table;
        Image image = new Image(getClass().getResourceAsStream(path));

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        imageView.setUserData(tableBean);

        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> selectTable(tableBean, imageView));
        enableDragIfNeeded(imageView, tableBean);

        return imageView;
    }

    private void enableDragIfNeeded(ImageView imageView, TableBean tableBean){
        if(!editMode){
            imageView.setOnMousePressed(null);
            imageView.setOnMouseDragged(null);
            imageView.setOnMouseReleased(null);
            return;
        }

        final Delta dragDelta = new Delta();
        dragDelta.startRow = tableBean.getRow();
        dragDelta.startCol = tableBean.getCol();

        imageView.setOnMousePressed( e -> {
            dragDelta.startX = e.getSceneX() - imageView.getLayoutX();
            dragDelta.startY = e.getSceneY() - imageView.getLayoutY();
        });

        imageView.setOnMouseDragged( e -> {
            double newX = e.getSceneX() - dragDelta.startX;
            double newY = e.getSceneY() - dragDelta.startY;
            imageView.setLayoutX(newX);
            imageView.setLayoutY(newY);
        });

        imageView.setOnMouseReleased( e -> {

            double centerX = imageView.getLayoutX() + imageView.getFitWidth() / 2;
            double centerY = imageView.getLayoutY() + imageView.getFitHeight() / 2;

            int newCol = (int) Math.floor(centerX / cellWidth);
            int newRow = (int) Math.floor(centerY / cellHeight);

            if(currentRoom != null){
                newCol = Math.max(0, Math.min(currentRoom.getCols() - 1, newCol));
                newRow = Math.max(0, Math.min(currentRoom.getRows() - 1, newRow));
            }

            double snappedX = newCol * cellWidth + (cellWidth -imageView.getFitWidth()) / 2;
            double snappedY = newRow * cellHeight + (cellHeight -imageView.getFitHeight()) / 2;
            imageView.setLayoutX(snappedX);
            imageView.setLayoutY(snappedY);

            try {
                controller.moveTable(tableBean.getId(), newRow, newCol);
                loadRestaurantRoom();
            } catch (RestaurantRoomException _) {
                double oldX = dragDelta.startCol * cellWidth + (cellWidth -imageView.getFitWidth()) / 2;
                double oldY = dragDelta.startRow * cellHeight + (cellHeight -imageView.getFitHeight()) / 2;
                imageView.setLayoutX(oldX);
                imageView.setLayoutY(oldY);
            }
        });
    }

    private void refreshDrag(){
        if(currentRoom == null) return;
        for(TableBean table: currentRoom.getTables()){
            ImageView imageView = tableNodes.get(table.getId());
            if(imageView != null){
                enableDragIfNeeded(imageView, table);
            }
        }
    }

    private void addTable(int seats){
        if(currentRoom == null) {
            showError("Sala non caricata");
            return;
        }

        try {
            int [] pos = findFirstFreeCell(currentRoom);
            int row = pos[0];
            int col = pos[1];

            TableBean tableBean = new TableBean();
            tableBean.setSeats(seats);
            tableBean.setRow(row);
            tableBean.setCol(col);

            controller.addTable(tableBean);

            loadRestaurantRoom();

        } catch (RestaurantRoomException e) {
            showError(e.getMessage());
        }
    }

    private int[] findFirstFreeCell(RestaurantRoomBean room){
        boolean[][] occupied = new boolean[room.getRows()][room.getCols()];

        room.getTables().forEach(t -> {
            if(t.getRow() >= 0 && t.getRow() < room.getRows() && t.getCol() >= 0 && t.getCol() < room.getCols()){
                occupied[t.getRow()][t.getCol()] = true;
            }
        });

        for(int r = 0; r < room.getRows(); r++){
            for(int c = 0; c < room.getCols(); c++){
                if(!occupied[r][c]){
                    return new int[]{r,c};
                }
            }
        }
        // se la sala è piena
        return new int[] {0,0};
    }

    private void removeSelected(){
        if(selectedTable == null) return;
        try {
            controller.removeTable(selectedTable.getId());
            loadRestaurantRoom();
        } catch (RestaurantRoomException e) {
            showError(e.getMessage());
        }
    }

    private void removeAllTables(){
        boolean confirm = showConfirmation("Rimozione completa tavoli", "Sei sicuro di voler rimuovere tutti i tavoli? ");
        if(!confirm){
            return;
        }
        try {
            controller.removeAllTables();
            loadRestaurantRoom();
        } catch (RestaurantRoomException e) {
            showError(e.getMessage());
        }
    }

    private void toggleEditMode(){
        editMode = !editMode;
        btnToggleEditMode.setText(editMode ? "DRAG: ON" : "DRAG: OFF");
        refreshDrag();
    }

    private void saveRoom(){
        loadRestaurantRoom();
    }

    private static class Delta {
        double startX;
        double startY;
        int startRow;
        int startCol;
    }

    @FXML 
    void onBackHome(ActionEvent event){
        if(router != null){
            router.showHomeManagerView();
        }
    }
}