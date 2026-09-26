package it.foodmood.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import it.foodmood.domain.model.Cart;
import it.foodmood.domain.model.CartItem;
import it.foodmood.domain.model.Order;
import it.foodmood.domain.model.OrderLine;
import it.foodmood.domain.model.TableSession;
import it.foodmood.exception.OrderException;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.CartDao;
import it.foodmood.persistence.dao.DaoFactory;
import it.foodmood.persistence.dao.OrderDao;
import it.foodmood.persistence.dao.TableSessionDao;

public class CustomerOrderController {

    private final OrderDao orderDao;
    private final CartDao cartDao;
    private final TableSessionDao tableSessionDao;

    public CustomerOrderController() {

        DaoFactory factory = DaoFactory.getInstance();

        this.orderDao = factory.getOrderDao();
        this.cartDao = factory.getCartDao();
        this.tableSessionDao = factory.getTableSessionDao();
    }

    public String createOrder( UUID actorId, UUID tableSessionId) throws OrderException {

        validateContext(actorId,tableSessionId);

        try {

            TableSession tableSession = tableSessionDao.findById(tableSessionId).orElseThrow(() -> new OrderException("Sessione tavolo non valida"));

            if (!tableSession.isOpen()) {
                throw new OrderException("Sessione tavolo chiusa, " + "non è possibile effettuare l'ordine.");
            }

            Cart cart = cartDao.findByActorAndTableSession(actorId,tableSessionId).orElseThrow(() -> new OrderException("Il carrello è vuoto"));

            if (cart.isEmpty()) {
                throw new OrderException("Il carrello è vuoto");
            }

            List<OrderLine> orderLines = new ArrayList<>();

            for (CartItem cartItem : cart.getItems()) {

                orderLines.add(new OrderLine(cartItem.getDishId(),cartItem.getProductName(),cartItem.getUnitPrice(),cartItem.getQuantity()));
            }

            Order order = new Order(actorId,tableSessionId,orderLines);

            orderDao.insert(order);

            cart.clear();
            cartDao.update(cart);

            return order.getId().toString();

        } catch (IllegalArgumentException e) {
            throw new OrderException("Dati ordine non validi",e);
        } catch (PersistenceException e) {
            throw new OrderException("Errore tecnico nell'inserimento " + "dell'ordine. Riprova più tardi",e);
        }
    }

    private void validateContext(UUID actorId,UUID tableSessionId
    ) throws OrderException {
        if (actorId == null) {
            throw new OrderException("Identificativo attore non valido");
        }

        if (tableSessionId == null) {
            throw new OrderException("Sessione tavolo non valida");
        }
    }
}