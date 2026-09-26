package it.foodmood.persistence.filesystem;

public final class FileSystemPaths {
    private static final String CSV_DIRECTORY = "data/csv/";

    public static final String USERS = CSV_DIRECTORY + "users.csv";
    public static final String CREDENTIALS = CSV_DIRECTORY + "credentials.csv";
    public static final String DISHES = CSV_DIRECTORY + "dishes.csv";
    public static final String INGREDIENTS = CSV_DIRECTORY + "ingredients.csv";
    public static final String ORDERS = CSV_DIRECTORY + "orders.csv";
    public static final String TABLE_SESSION = CSV_DIRECTORY + "table_session.csv";
    public static final String RESTAURANT_ROOM = CSV_DIRECTORY + "restaurant_room.csv";
    public static final String CARTS = CSV_DIRECTORY + "carts.csv";

    private FileSystemPaths(){
        // Costruttore vuoto
    }

}
