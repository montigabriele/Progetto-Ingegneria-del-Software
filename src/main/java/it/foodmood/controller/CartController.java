package it.foodmood.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import it.foodmood.bean.CartItemBean;
import it.foodmood.domain.model.Cart;
import it.foodmood.domain.model.CartItem;
import it.foodmood.domain.model.Dish;
import it.foodmood.exception.CartException;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.CartDao;
import it.foodmood.persistence.dao.DaoFactory;
import it.foodmood.persistence.dao.DishDao;

public class CartController {

    private final DishDao dishDao;
    private final CartDao cartDao;

    public CartController() {
        DaoFactory daoFactory = DaoFactory.getInstance();

        this.dishDao = daoFactory.getDishDao();
        this.cartDao = daoFactory.getCartDao();
    }

    public void addToCart(UUID actorId,UUID tableSessionId,CartItemBean cartItem) throws CartException {

        validateContext(actorId, tableSessionId);

        if (cartItem == null) {
            throw new CartException("Dati dell'articolo non validi");
        }

        String dishId = cartItem.getDishId();
        int quantity = cartItem.getQuantity();

        if (dishId == null || dishId.isBlank()) {
                throw new CartException("ID articolo non valido");
        }

        if (quantity <= 0) {
            throw new CartException("Quantità non valida");
        }

        final UUID id;

        try {
            id = UUID.fromString(dishId);

        } catch (IllegalArgumentException e) {

            throw new CartException("ID articolo non valido",e);
        }

        try {

            Dish dish = dishDao.findById(id).orElseThrow(() -> new CartException("Articolo non disponibile"));

            Optional<Cart> existingCart = cartDao.findByActorAndTableSession(actorId,tableSessionId);

            if (existingCart.isPresent()) {

                Cart cart = existingCart.get();

                cart.addLine(dish.getId(),dish.getName(),dish.getPrice(),quantity);

                cartDao.update(cart);

            } else {

                Cart cart = Cart.create(actorId,tableSessionId);

                cart.addLine(dish.getId(),dish.getName(),dish.getPrice(),quantity);

                cartDao.insert(cart);
            }

        } catch (PersistenceException e) {
            throw new CartException("Errore tecnico: impossibile aggiungere " + "l'articolo al carrello. Riprova più tardi",e);
        }
    }

    public List<CartItemBean> getCartItems(UUID actorId, UUID tableSessionId) throws CartException {
        validateContext(actorId, tableSessionId);
        try {
            Optional<Cart> cart =
                    cartDao.findByActorAndTableSession(actorId,tableSessionId);

            if (cart.isEmpty()) {
                return List.of();
            }

            return cart.get().getItems().stream().map(this::toItemBean).toList();

        } catch (PersistenceException e) {
            throw new CartException("Errore tecnico: impossibile recuperare il carrello",e);
        }
    }

    public void removeFromCart(UUID actorId,UUID tableSessionId,String dishId) throws CartException {

        validateContext(actorId, tableSessionId);

        if (dishId == null || dishId.isBlank()) {
            throw new CartException("ID articolo non valido");
        }

        final UUID id;

        try {
            id = UUID.fromString(dishId);
        } catch (IllegalArgumentException e) {
            throw new CartException("ID articolo non valido",e);
        }

        try {

            Optional<Cart> cart = cartDao.findByActorAndTableSession(actorId,tableSessionId);
            if (cart.isEmpty()) {
                return;
            }

            Cart existingCart = cart.get();

            existingCart.removeLine(id);

            cartDao.update(existingCart);

        } catch (PersistenceException e) {
            throw new CartException("Errore tecnico: impossibile rimuovere " + "l'articolo dal carrello",e);
        }
    }

    public BigDecimal getTotal(UUID actorId,UUID tableSessionId) throws CartException {
        validateContext(actorId, tableSessionId);
        try {
            Optional<Cart> cart = cartDao.findByActorAndTableSession( actorId,tableSessionId);

            if (cart.isEmpty()) {
                return BigDecimal.ZERO;
            }

            return cart.get().getTotal();

        } catch (PersistenceException e) {

            throw new CartException( "Errore tecnico: impossibile calcolare " + "il totale del carrello",e);
        }
    }

    private void validateContext(UUID actorId,UUID tableSessionId) throws CartException {
        if (actorId == null) {
            throw new CartException(
                    "Identificativo attore non valido"
            );
        }

        if (tableSessionId == null) {
            throw new CartException(
                    "Sessione tavolo non valida"
            );
        }
    }

    private CartItemBean toItemBean( CartItem cartItem) {

        CartItemBean cartItemBean = new CartItemBean();

        cartItemBean.setDishId( cartItem.getDishId().toString());

        cartItemBean.setProductName( cartItem.getProductName());

        cartItemBean.setUnitPrice(cartItem.getUnitPrice().getAmount());

        cartItemBean.setQuantity(cartItem.getQuantity());

        return cartItemBean;
    }
}