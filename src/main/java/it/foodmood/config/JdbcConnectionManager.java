package it.foodmood.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import it.foodmood.persistence.ConnectionProvider;

public final class JdbcConnectionManager implements ConnectionProvider{
    
    private static JdbcConnectionManager instance;
    
    private Connection connection;
    
    private final String url;
    private final String user;
    private final String password;
    
    // Costruttore privato per singleton
    private JdbcConnectionManager(String url, String user, String password){
        this.url = Objects.requireNonNull(url, "L'url non può essere nullo.");
        this.user = Objects.requireNonNull(user, "L'user non può essere nullo.");
        this.password = Objects.requireNonNull(password, "La password non può essere nulla.");

        Runtime.getRuntime().addShutdownHook(new Thread(this::close, "jdbc-shutdown-hook"));
    }

    public static synchronized JdbcConnectionManager init(String url, String user, String password){
        if(instance == null){
            instance = new JdbcConnectionManager(url, user, password);
        } else if(!instance.url.equals(url) || !instance.user.equals(user) || !instance.password.equals(password)) {
            throw new IllegalStateException("DriverManager già inizializzato con credenziali diverse!");
        }
        return instance;
    }

    public static synchronized JdbcConnectionManager getInstance(){
        if(instance == null){
            throw new IllegalStateException("Connessione non inizializzata. Chiama init(url,user,password)");
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException{
        if(connection == null || connection.isClosed()){
            Connection conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(true);
            connection = conn;
        }
        return connection;
    }

    @Override
    public synchronized void close(){
        Connection conn = connection;
        connection = null;
        if(conn != null){
            try{
                if (!conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException _) {
                // da implementare
            }
        }
    }
}
