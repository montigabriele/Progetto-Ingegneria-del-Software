package it.foodmood.persistence.dao;

import java.util.List;
import java.util.Optional;

import it.foodmood.domain.model.RestaurantRoom;
import it.foodmood.domain.model.Table;

public interface RestaurantRoomDao {
    Optional<RestaurantRoom> load();
    void save(RestaurantRoom restaurantRoom);
    Optional<Table> findById(Integer id);
    List<Table> findAll();
}
