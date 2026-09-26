package it.foodmood.persistence.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;
import java.util.UUID;

import it.foodmood.config.JdbcConnectionManager;
import it.foodmood.domain.model.TableSession;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.TableSessionDao;

public class JdbcTableSessionDao implements TableSessionDao {

    private static final String CALL_ENTER_SESSION = "{CALL enter_table_session(?,?,?)}";
    private static final String CALL_CLOSE_SESSION = "{CALL close_table_session_by_table(?)}";
    private static final String CALL_GET_TABLE_SESSION_BY_ID = "{CALL get_table_session_by_id(?)}";

    public JdbcTableSessionDao(){
        // Costruttore vuoto
    }
    
    @Override
    public UUID enterOrGetOpenSession(TableSession tableSession){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try(CallableStatement cs = conn.prepareCall(CALL_ENTER_SESSION)){
                cs.setInt(1, tableSession.getTableId());
                cs.setString(2, tableSession.getTableSessionId().toString());
                cs.registerOutParameter(3, Types.CHAR);

                cs.execute();

                String sessionId = cs.getString(3);
                return UUID.fromString(sessionId);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void closeSession(int tableId){
        try {
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try(CallableStatement cs = conn.prepareCall(CALL_CLOSE_SESSION)){
                cs.setInt(1, tableId);

                cs.execute();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<TableSession> findById(UUID tableSessionId){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            return executeFindById(conn, tableSessionId);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }    
    }

    private Optional<TableSession> executeFindById(Connection conn, UUID tableSessionId) throws SQLException{
        try (CallableStatement cs = conn.prepareCall(CALL_GET_TABLE_SESSION_BY_ID)){
            cs.setString(1, tableSessionId.toString());
            try(ResultSet rs = cs.executeQuery()){
                if(rs.next()){
                    TableSession tableSession = mapRowToTableSession(rs);

                    return Optional.of(tableSession);
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    private TableSession mapRowToTableSession(ResultSet rs) throws SQLException {
        
        UUID id = UUID.fromString(rs.getString("id"));
        int tableId = rs.getInt("table_id");
        boolean open = rs.getBoolean("is_open");

        return TableSession.fromPersistence(id, tableId, open);
    }
}
