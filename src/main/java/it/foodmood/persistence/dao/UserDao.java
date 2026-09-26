package it.foodmood.persistence.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import it.foodmood.domain.model.User;
import it.foodmood.domain.value.Email;
import it.foodmood.domain.value.Role;

public interface UserDao extends CrudDao<User, UUID>{
    
    Optional<User> findByEmail(Email email);

    List<User> findByRole(Role role);
}