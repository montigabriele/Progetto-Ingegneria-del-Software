package it.foodmood.persistence.inmemory;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import it.foodmood.domain.model.Cart;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.CartDao;

public class InMemoryCartDao extends AbstractInMemoryCrudDao<Cart, UUID> implements CartDao {

    public  InMemoryCartDao() {
        // Costruttore
    }

    @Override
    protected UUID getId(Cart cart) {
        return cart.getId();
    }

    @Override
    public Optional<Cart> findByActorAndTableSession(UUID actorId,UUID tableSessionId) {
        if (actorId == null || tableSessionId == null) {
            return Optional.empty();
        }

        return storage.values().stream().filter(cart -> cart.getActorId().equals(actorId) && cart.getTableSessionId().equals(tableSessionId)).findFirst();
    }

    @Override
    public void update(Cart cart) {

        Objects.requireNonNull( cart, "Il carrello non può essere nullo");

        if (!storage.containsKey(cart.getId())) {
            throw new PersistenceException( "Carrello non trovato: " + cart.getId());
        }

        storage.put(cart.getId(),cart);
    }
}