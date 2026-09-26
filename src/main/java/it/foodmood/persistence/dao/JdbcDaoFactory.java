package it.foodmood.persistence.dao;

import it.foodmood.persistence.cache.CachedIngredientDao;
import it.foodmood.persistence.mysql.JdbcCartDao;
import it.foodmood.persistence.mysql.JdbcCredentialDao;
import it.foodmood.persistence.mysql.JdbcDishDao;
import it.foodmood.persistence.mysql.JdbcIngredientDao;
import it.foodmood.persistence.mysql.JdbcOrderDao;
import it.foodmood.persistence.mysql.JdbcRestaurantRoomDao;
import it.foodmood.persistence.mysql.JdbcTableSessionDao;
import it.foodmood.persistence.mysql.JdbcUserDao;

/*
 * Implementazione concreta della factory per la persistenza su MySQL
 */
public final class JdbcDaoFactory extends DaoFactory {

    private final IngredientDao ingredientDao;
    private final DishDao dishDao;
    private final UserDao userDao;
    private final CredentialDao credentialDao;
    private final RestaurantRoomDao restaurantRoomDao;
    private final OrderDao orderDao;
    private final TableSessionDao tableSessionDao;
    private final CartDao cartDao;

    JdbcDaoFactory() {

        IngredientDao jdbcIngredientDao = new JdbcIngredientDao();

        this.ingredientDao = new CachedIngredientDao(jdbcIngredientDao);

        this.dishDao = new JdbcDishDao(this.ingredientDao);

        this.userDao = new JdbcUserDao();

        this.credentialDao = new JdbcCredentialDao();

        this.restaurantRoomDao = new JdbcRestaurantRoomDao();

        this.orderDao = new JdbcOrderDao();

        this.tableSessionDao = new JdbcTableSessionDao();

        this.cartDao = new JdbcCartDao();
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