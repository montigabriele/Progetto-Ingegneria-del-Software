package it.foodmood.persistence.inmemory;

import it.foodmood.domain.model.Ingredient;
import it.foodmood.persistence.dao.IngredientDao;

public class InMemoryIngredientDao extends AbstractInMemoryCrudDao<Ingredient, String> implements IngredientDao {

    public InMemoryIngredientDao(){
        // costruttore per il singleton
    }

    @Override
    protected String getId(Ingredient ingredient){
        return ingredient.getName();
    }

}
