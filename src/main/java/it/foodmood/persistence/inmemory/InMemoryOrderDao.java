package it.foodmood.persistence.inmemory;

import java.util.UUID;

import it.foodmood.domain.model.Order;
import it.foodmood.persistence.dao.OrderDao;

public final class InMemoryOrderDao extends AbstractInMemoryCrudDao<Order, UUID> implements OrderDao {
    

    public InMemoryOrderDao(){
        // costruttore 
    }

    @Override
    public UUID getId(Order order){
        return order.getId();
    }
}
