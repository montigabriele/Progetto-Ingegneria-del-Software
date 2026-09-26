package it.foodmood.persistence.dao;

import it.foodmood.persistence.inmemory.InMemoryCartDao;
import it.foodmood.persistence.inmemory.InMemoryCredentialDao;
import it.foodmood.persistence.inmemory.InMemoryDishDao;
import it.foodmood.persistence.inmemory.InMemoryIngredientDao;
import it.foodmood.persistence.inmemory.InMemoryOrderDao;
import it.foodmood.persistence.inmemory.InMemoryRestaurantRoomDao;
import it.foodmood.persistence.inmemory.InMemoryTableSessionDao;
import it.foodmood.persistence.inmemory.InMemoryUserDao;

/*
 * Implementazione concreta della factory
 * per la persistenza in memoria volatile
 */
public final class InMemoryDaoFactory extends DaoFactory {

    private final IngredientDao ingredientDao;
    private final DishDao dishDao;
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final CredentialDao credentialDao;
    private final RestaurantRoomDao restaurantRoomDao;
    private final TableSessionDao tableSessionDao;
    private final CartDao cartDao;

    InMemoryDaoFactory() {

        this.ingredientDao = new InMemoryIngredientDao();
        this.dishDao = new InMemoryDishDao();
        this.orderDao = new InMemoryOrderDao();
        this.userDao = new InMemoryUserDao();
        this.credentialDao = new InMemoryCredentialDao();
        this.restaurantRoomDao = new InMemoryRestaurantRoomDao();
        this.tableSessionDao = new InMemoryTableSessionDao();
        this.cartDao = new InMemoryCartDao();
    }

    @Override
    public DishDao getDishDao() {
        return dishDao;
    }

    @Override
    public UserDao getUserDao() {
        return userDao;
    }

    @Override
    public CredentialDao getCredentialDao() {
        return credentialDao;
    }

    @Override
    public IngredientDao getIngredientDao() {
        return ingredientDao;
    }

    @Override
    public RestaurantRoomDao getRestaurantRoomDao() {
        return restaurantRoomDao;
    }

    @Override
    public OrderDao getOrderDao() {
        return orderDao;
    }

    @Override
    public TableSessionDao getTableSessionDao() {
        return tableSessionDao;
    }

    @Override
    public CartDao getCartDao() {
        return cartDao;
    }
}