package it.foodmood.persistence.dao;

import java.util.UUID;

import it.foodmood.domain.model.Order;

public interface OrderDao extends CrudDao<Order, UUID> {

}
