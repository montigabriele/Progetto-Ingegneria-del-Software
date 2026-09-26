package it.foodmood.utils;

import java.sql.Connection;
import java.sql.SQLException;

import it.foodmood.persistence.ConnectionProvider;

public final class ConnectionVerifier {

    private ConnectionVerifier() {}
    
    public static void verify(ConnectionProvider provider) throws SQLException{
        try(Connection conn = provider.getConnection()){
            if(conn == null || !conn.isValid(1)){
                throw new SQLException("Connessione al database non riuscita!");
            }
        }
    }

    public static boolean verifyWithRetry(ConnectionProvider provider, int attemps, int delay){
        for(int i = 1; i <= attemps; i++){
            try {
                verify(provider);
                return true;
            } catch (SQLException _) {
                System.out.println("Verifica connessione al database...\nTentativo numero: " + i);
                if(i < attemps){
                    sleep(delay);
                }
            }
        }
        return false;
    }

    private static void sleep(int seconds){
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }
    }

}
