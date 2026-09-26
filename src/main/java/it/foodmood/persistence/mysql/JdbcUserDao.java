package it.foodmood.persistence.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import it.foodmood.config.JdbcConnectionManager;
import it.foodmood.domain.model.Customer;
import it.foodmood.domain.model.Manager;
import it.foodmood.domain.model.User;
import it.foodmood.domain.model.Waiter;
import it.foodmood.domain.value.Email;
import it.foodmood.domain.value.Person;
import it.foodmood.domain.value.Role;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.UserDao;
import it.foodmood.utils.RoleConverter;

public class JdbcUserDao implements UserDao {
            
    private static final String CALL_INSERT_USER = "{CALL insert_user(?,?,?,?,?)}";
    private static final String CALL_GET_USER_BY_ID = "{CALL get_user_by_id(?)}";
    private static final String CALL_GET_ALL_USERS = "{CALL get_all_users()}";
    private static final String CALL_GET_USERS_BY_ROLE = "{CALL get_users_by_role(?)}";
    private static final String CALL_GET_USER_BY_EMAIL = "{CALL get_user_by_email(?)}";
    private static final String CALL_DELETE_USER_BY_ID = "{CALL delete_user_by_id(?)}";

    public JdbcUserDao(){
        // costruttore privato
    }

    @Override
    public void insert(User user){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try(CallableStatement cs = conn.prepareCall(CALL_INSERT_USER)){
                cs.setString(1, user.getId().toString());
                cs.setString(2, user.getPerson().firstName());
                cs.setString(3, user.getPerson().lastName());
                cs.setString(4, user.getEmail().email());
                cs.setString(5, user.getRole().name());
                cs.execute();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<User> findById(UUID id){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try (CallableStatement cs = conn.prepareCall(CALL_GET_USER_BY_ID)){
                cs.setString(1, id.toString());
                try(ResultSet rs = cs.executeQuery()) {
                    if(rs.next()){
                        return Optional.of(mapUser(rs));
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<User> findAll(){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try (CallableStatement cs = conn.prepareCall(CALL_GET_ALL_USERS);
                ResultSet rs = cs.executeQuery()){
                List<User> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(mapUser(rs));
                }
                return out;
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void deleteById(UUID id){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try (CallableStatement cs = conn.prepareCall(CALL_DELETE_USER_BY_ID)){
                cs.setString(1, id.toString());
                cs.execute();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<User> findByRole(Role role){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try (CallableStatement cs = conn.prepareCall(CALL_GET_USERS_BY_ROLE)){
                cs.setString(1, role.name());
                try(ResultSet rs = cs.executeQuery()){
                    List<User> out = new ArrayList<>();
                    while(rs.next()){
                        out.add(mapUser(rs));
                    }
                    return out;
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(Email email){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try(CallableStatement cs = conn.prepareCall(CALL_GET_USER_BY_EMAIL)){
                cs.setString(1, email.email());
                try(ResultSet rs = cs.executeQuery()){
                    if(rs.next()){
                        return Optional.of(mapUser(rs));
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }


    // da rivedere 
    private User mapUser(ResultSet rs) throws SQLException{
        UUID id = UUID.fromString(rs.getString("id_user"));
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String emailStr = rs.getString("email");
        String roleStr = rs.getString("role");

        Person person = new Person(name, surname);
        Email email = new Email(emailStr);
        Role role = RoleConverter.fromString(roleStr);

        return switch(role){
            case CUSTOMER -> new Customer(id, person, email);
            case WAITER -> new Waiter(id, person, email);
            case MANAGER -> new Manager(id, person, email);
        };
    }
}