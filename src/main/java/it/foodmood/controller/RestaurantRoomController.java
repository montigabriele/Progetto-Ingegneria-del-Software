package it.foodmood.controller;

import java.util.List;
import java.util.Optional;

import it.foodmood.bean.RestaurantRoomBean;
import it.foodmood.bean.TableBean;
import it.foodmood.domain.model.RestaurantRoom;
import it.foodmood.domain.model.Table;
import it.foodmood.domain.value.TablePosition;
import it.foodmood.exception.PersistenceException;
import it.foodmood.exception.RestaurantRoomException;
import it.foodmood.persistence.dao.DaoFactory;
import it.foodmood.persistence.dao.RestaurantRoomDao;

public class RestaurantRoomController {
    
    private final RestaurantRoomDao restaurantRoomDao;

    public RestaurantRoomController(){
        DaoFactory factory = DaoFactory.getInstance();
        this.restaurantRoomDao = factory.getRestaurantRoomDao();
    }

    public void createRestaurantRoom(RestaurantRoomBean restaurantRoomBean) throws RestaurantRoomException {

        try {
            int rows = restaurantRoomBean.getRows();
            int cols = restaurantRoomBean.getCols();
            
            RestaurantRoom restaurantRoom = new RestaurantRoom(rows, cols);
            restaurantRoomDao.save(restaurantRoom);

        } catch (IllegalArgumentException e) {
            throw new RestaurantRoomException("Errore durante la creazione della sala: " + e.getMessage());
        } catch (PersistenceException _){
            throw new RestaurantRoomException("Errore tecnico durante il salvataggio della sala.");
        }
    }

    public RestaurantRoomBean loadRestaurantRoom() throws RestaurantRoomException{
        try {
            Optional<RestaurantRoom> opt = restaurantRoomDao.load();

            RestaurantRoom restaurantRoom;
            if(opt.isEmpty()){
                restaurantRoom = new RestaurantRoom(7, 5);
                restaurantRoomDao.save(restaurantRoom);
            } else {
                restaurantRoom = opt.get();
            }

            return toBean(restaurantRoom);

        } catch (PersistenceException _) {
            throw new RestaurantRoomException("Errore tecnico durante il recupero della sala.");
        }

    }

    public RestaurantRoom getRestaurantRoom() throws RestaurantRoomException{
        return restaurantRoomDao.load().orElseThrow(() -> new RestaurantRoomException("Nessuna sala presente"));
    }

    public TableBean addTable(TableBean tableBean) throws RestaurantRoomException {

        try {
            RestaurantRoom restaurantRoom = getRestaurantRoom();

            int seats = tableBean.getSeats();
            int row = tableBean.getRow();
            int col = tableBean.getCol();

            Table table = restaurantRoom.addTable(seats, row, col);

            restaurantRoomDao.save(restaurantRoom);

            return toBean(table);


        } catch (IllegalArgumentException e) {
            throw new RestaurantRoomException("Errore durante l'aggiunta del tavolo: " + e.getMessage());
        }
    }

    public void moveTable(int tableId, int newRow, int newCol) throws RestaurantRoomException{

        try{
            RestaurantRoom restaurantRoom = getRestaurantRoom();

            restaurantRoom.moveTable(tableId, newRow, newCol);

            restaurantRoomDao.save(restaurantRoom);
        } catch (IllegalArgumentException e){
            throw new RestaurantRoomException("Errore durante lo spostamento del tavolo: " + e.getMessage());
        }
    }

    public void removeTable(int tableId) throws RestaurantRoomException {
        RestaurantRoom restaurantRoom = getRestaurantRoom();
        restaurantRoom.removeTable(tableId);
        restaurantRoomDao.save(restaurantRoom);
    }

    public void removeAllTables() throws RestaurantRoomException {
        RestaurantRoom restaurantRoom = getRestaurantRoom();
        restaurantRoom.removeAllTables();
        restaurantRoomDao.save(restaurantRoom);
    }

    private RestaurantRoomBean toBean(RestaurantRoom restaurantRoom){
        RestaurantRoomBean restaurantRoomBean = new RestaurantRoomBean();

        restaurantRoomBean.setRows(restaurantRoom.getRows());
        restaurantRoomBean.setCols(restaurantRoom.getCols());

        List<TableBean> tableBeans = restaurantRoom.getTables().stream().map(this::toBean).toList();

        restaurantRoomBean.setTables(tableBeans);

        return restaurantRoomBean;
    }

    private TableBean toBean(Table table){
        TableBean tableBean = new TableBean();

        tableBean.setId(table.getId());
        tableBean.setSeats(table.getSeats());
        tableBean.setStatus(table.getStatus());

        TablePosition position = table.getPosition();
        tableBean.setRow(position.row());
        tableBean.setCol(position.col());

        return tableBean;
    }
}
