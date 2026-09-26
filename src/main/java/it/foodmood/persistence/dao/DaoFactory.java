package it.foodmood.persistence.dao;

import it.foodmood.config.PersistenceMode;

public abstract class DaoFactory {

    private static DaoFactory instance;

    DaoFactory(){}

    public static synchronized void init(PersistenceMode mode){

        if (mode == null) {
            throw new IllegalArgumentException(
                "PersistenceMode non può essere null"
            );
        }

        if(instance != null) {
            throw new IllegalStateException("DaoFactory già inizializzata");
        }

        instance = switch(mode){
            case FULL -> new JdbcDaoFactory();
            case DEMO -> new InMemoryDaoFactory();
            case FILESYSTEM -> new FileSystemDaoFactory();
        };
    }

    public static synchronized DaoFactory getInstance(){
        if(instance == null){
            throw new IllegalStateException("DaoFactory non inizializzata. Chiama DaoFactory.init(mode)");
        }
        return instance;
    }
    
    public abstract DishDao getDishDao();
    
    public abstract UserDao getUserDao();

    public abstract CredentialDao getCredentialDao();

    public abstract IngredientDao getIngredientDao();

    public abstract RestaurantRoomDao getRestaurantRoomDao();

    public abstract OrderDao getOrderDao();

    public abstract TableSessionDao getTableSessionDao();

    public abstract CartDao getCartDao();
}
