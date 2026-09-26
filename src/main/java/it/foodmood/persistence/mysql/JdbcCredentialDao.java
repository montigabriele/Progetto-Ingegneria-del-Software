package it.foodmood.persistence.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import it.foodmood.config.JdbcConnectionManager;
import it.foodmood.domain.model.Credential;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.CredentialDao;

public class JdbcCredentialDao implements CredentialDao {
    
    private static final String CALL_SAVE_CREDENTIAL = "{CALL save_credential(?,?)}";
    private static final String CALL_CREDENTIAL_BY_USER_ID = "{CALL get_credential_by_user_id(?)}";

    public JdbcCredentialDao(){
        // costruttore
    }

    @Override
    public void saveCredential(Credential credential){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try(CallableStatement cs = conn.prepareCall(CALL_SAVE_CREDENTIAL)){
                cs.setString(1, credential.userId().toString());
                cs.setString(2, credential.passwordHash());
                cs.execute();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Credential findByUserId(UUID userId){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try(CallableStatement cs = conn.prepareCall(CALL_CREDENTIAL_BY_USER_ID)){
                cs.setString(1, userId.toString());
                try(ResultSet rs = cs.executeQuery()){
                    if(rs.next()){
                        String passwordHash = rs.getString("password_hash");
                        return new Credential(userId, passwordHash);
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
