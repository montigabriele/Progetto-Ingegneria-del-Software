package it.foodmood.persistence.dao;

import it.foodmood.persistence.cache.CachedIngredientDao;
import it.foodmood.persistence.filesystem.FileSystemCartDao;
import it.foodmood.persistence.filesystem.FileSystemCredentialDao;
import it.foodmood.persistence.filesystem.FileSystemDishDao;
import it.foodmood.persistence.filesystem.FileSystemIngredientDao;
import it.foodmood.persistence.filesystem.FileSystemOrderDao;
import it.foodmood.persistence.filesystem.FileSystemRestaurantRoomDao;
import it.foodmood.persistence.filesystem.FileSystemTableSessionDao;
import it.foodmood.persistence.filesystem.FileSystemUserDao;

/*
 * Implementazione concreta della factory
 * per la persistenza su file system
 */
public final class FileSystemDaoFactory extends DaoFactory {

    private final IngredientDao ingredientDao;
    private final DishDao dishDao;
    private final UserDao userDao;
    private final CredentialDao credentialDao;
    private final RestaurantRoomDao restaurantRoomDao;
    private final OrderDao orderDao;
    private final TableSessionDao tableSessionDao;
    private final CartDao cartDao;

    FileSystemDaoFactory() {

        this.ingredientDao = new CachedIngredientDao(new FileSystemIngredientDao());

        this.dishDao = new FileSystemDishDao(this.ingredientDao);

        this.userDao = new FileSystemUserDao();

        this.credentialDao = new FileSystemCredentialDao();

        this.restaurantRoomDao = new FileSystemRestaurantRoomDao();

        this.orderDao = new FileSystemOrderDao();

        this.tableSessionDao = new FileSystemTableSessionDao();

        this.cartDao = new FileSystemCartDao();
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