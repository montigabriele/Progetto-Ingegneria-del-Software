package it.foodmood.persistence.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.foodmood.domain.model.RestaurantRoom;
import it.foodmood.domain.model.Table;
import it.foodmood.persistence.dao.RestaurantRoomDao;

public final class InMemoryRestaurantRoomDao implements RestaurantRoomDao {
    
    private RestaurantRoom restaurantRoom;

    public  InMemoryRestaurantRoomDao(){
        initializeDefaultRoom();
    }

    private void initializeDefaultRoom(){
        // Creo una sala 7x5 
        this.restaurantRoom = new RestaurantRoom(7, 5);

        // Creo tavoli di esempio
        this.restaurantRoom.addTable(2, 0, 0);
        this.restaurantRoom.addTable(4, 0, 1);
        this.restaurantRoom.addTable(6, 1, 0);
        this.restaurantRoom.addTable(8, 1, 1);
    }

    @Override
    public Optional<RestaurantRoom> load(){
        if(restaurantRoom == null){
            return Optional.empty();
        }

        List<Table> tables = new ArrayList<>(restaurantRoom.getTables());
        RestaurantRoom room = new RestaurantRoom(restaurantRoom.getRows(), restaurantRoom.getCols(), tables);

        return Optional.of(room);
    }

    @Override
    public void save(RestaurantRoom restaurantRoom){
        if(restaurantRoom == null){
            throw new IllegalArgumentException("La sala del ristorante non può essere nulla");
        }

        List<Table> tables = new ArrayList<>(restaurantRoom.getTables());
        this.restaurantRoom = new RestaurantRoom(restaurantRoom.getRows(), restaurantRoom.getCols(), tables);
    }

    @Override
    public Optional<Table> findById(Integer tableId){
        if(tableId == null || restaurantRoom == null){
            return Optional.empty();
        }

        return restaurantRoom.getTables().stream().filter(table -> tableId.equals(table.getId())).findFirst();
    }

    @Override
    public List<Table> findAll(){
        if(restaurantRoom == null){
            return new ArrayList<>();
        }
        return new ArrayList<>(restaurantRoom.getTables());
    }
}
