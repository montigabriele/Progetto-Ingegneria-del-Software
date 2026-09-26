package it.foodmood.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import it.foodmood.bean.DishBean;
import it.foodmood.bean.IngredientPortionBean;
import it.foodmood.controller.mapper.DishMapper;
import it.foodmood.domain.model.Dish;
import it.foodmood.domain.model.Ingredient;
import it.foodmood.domain.validation.DietCategoryValidator;
import it.foodmood.domain.value.CourseType;
import it.foodmood.domain.value.DietCategory;
import it.foodmood.domain.value.DishParams;
import it.foodmood.domain.value.DishState;
import it.foodmood.domain.value.Image;
import it.foodmood.domain.value.IngredientPortion;
import it.foodmood.domain.value.Money;
import it.foodmood.domain.value.Quantity;
import it.foodmood.domain.value.Unit;
import it.foodmood.exception.DishException;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.DaoFactory;
import it.foodmood.persistence.dao.DishDao;
import it.foodmood.persistence.dao.IngredientDao;

public class DishController {

    private final DishDao dishDao;
    private final IngredientDao ingredientDao;

    public DishController(){
        DaoFactory factory = DaoFactory.getInstance();
        this.dishDao = factory.getDishDao();
        this.ingredientDao = factory.getIngredientDao();
    }

    public void createDish(DishBean dishBean) throws DishException{

        if(dishBean == null) {
            throw new DishException("Il piatto non può essere nullo.");
        }

        try {
            String name = dishBean.getName();
            String description = dishBean.getDescription();
            BigDecimal priceValue = dishBean.getPrice();
            CourseType courseType = dishBean.getCourseType();
            Set<DietCategory> dietCategories = dishBean.getDietCategories();
            String imageUri = dishBean.getImageUri();
            DishState dishState = dishBean.getState();

            // controllo se esiste già un piatto con questo nome
            if(dishDao.findByName(name).isPresent()){ 
                throw new DishException("Esiste già un piatto con il nome: " + name);
            }

            if(priceValue == null){ 
                throw new DishException("Il prezzo non può essere nullo");
            }

            if(dishBean.getIngredients() == null || dishBean.getIngredients().isEmpty()){
                throw new DishException("Il piatto deve avere almeno un ingrediente.");
            }

            Money price = new Money(priceValue);

            Image image = (imageUri != null && !imageUri.isBlank()) ? new Image(URI.create(imageUri)) : null;

            List<IngredientPortion> ingredientPortions = toDomainIngredientPortions(dishBean.getIngredients());

            DishParams params = new DishParams(
                name,
                description,
                courseType,
                dietCategories,
                ingredientPortions,
                dishState,
                image,
                price
            );

            Dish dish = Dish.create(params);

            DietCategoryValidator.validate(dish);

            dishDao.insert(dish);

        } catch (IllegalArgumentException e){
            throw new DishException("Errore durante l'inserimento del piatto: " + e.getMessage(), e);
        } catch (PersistenceException _){
            throw new DishException("Spiacenti si è verificato un errore tecnico durante l'inserimento del piatto, riprovare in seguito.");
        }
    }

    public List<DishBean> getAllDishes() throws DishException{
        try {
            DishMapper dishMapper = new DishMapper();
            return dishMapper.toBeans(dishDao.findAll());
        } catch (PersistenceException e) {
            throw new DishException("Spiacenti si è verificato un errore tecnico durante il recupero dei piatti, riprovare in seguito.", e);
        }
    }

    public List<DishBean> getDishesByCourseType(CourseType courseType) throws DishException{
        try {
            DishMapper dishMapper = new DishMapper();
            return dishMapper.toBeans(dishDao.findByCourseType(courseType));
        } catch (PersistenceException e) {
            throw new DishException("Spiacenti si è verificato un errore tecnico, riprovare in seguito.", e);
        }
    }

    private IngredientPortion toDomainIngredientPortion(IngredientPortionBean ingredientPortionBean){
        if(ingredientPortionBean == null){
            throw new IllegalArgumentException("La porzione dell'ingrediente non può essere nulla.");
        }
        
        if(ingredientPortionBean.getIngredient() == null){
            throw new IllegalArgumentException("L'ingrediente non può essere nullo.");
        }

        String ingredientName = ingredientPortionBean.getIngredient().getName();

        Ingredient ingredient = ingredientDao.findById(ingredientName).orElseThrow(() -> new IllegalArgumentException("Ingrediente non trovato: " + ingredientName));

        double amount = ingredientPortionBean.getQuantity();
        String unitName = ingredientPortionBean.getUnit();

        Unit unit = Unit.valueOf(unitName);

        Quantity quantity = new Quantity(amount, unit);

        return new IngredientPortion(ingredient, quantity);
    }

    private List<IngredientPortion> toDomainIngredientPortions(List<IngredientPortionBean> portionBeans) {
        if(portionBeans == null || portionBeans.isEmpty()){
            throw new IllegalArgumentException("La lista delle porzioni di ingrediente non può essere vuota.");
        }
        return portionBeans.stream().map(this::toDomainIngredientPortion).toList();
    }

    public void deleteDish(String id) throws DishException{
        if(id == null || id.isBlank()){
            throw new DishException("L'id del piatto non può essere vuoto.");
        }
        
        try {
            UUID dishId = UUID.fromString(id);

            if(dishDao.findById(dishId).isEmpty()){
                throw new DishException("Nessun piatto trovato con id: " + id);
            }

            dishDao.deleteById(dishId);

        } catch (IllegalArgumentException e) {
            throw new DishException("Formato id non valido: " + id, e);
        } catch (PersistenceException _){
            throw new DishException("Errore tecnico durante l'eliminazione del piatto. Riprova più tardi.");
        }
    }
}
