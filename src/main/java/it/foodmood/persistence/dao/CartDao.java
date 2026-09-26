package it.foodmood.persistence.dao;

import java.util.Optional;
import java.util.UUID;

import it.foodmood.domain.model.Cart;

public interface CartDao extends CrudDao<Cart, UUID> {

    Optional<Cart> findByActorAndTableSession( UUID actorId, UUID tableSessionId);

    void update(Cart cart);
}