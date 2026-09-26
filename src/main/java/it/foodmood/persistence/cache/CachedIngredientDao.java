package it.foodmood.persistence.cache;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.foodmood.domain.model.Ingredient;
import it.foodmood.persistence.dao.IngredientDao;

public final class CachedIngredientDao extends AbstractCachedDao<Ingredient, String> implements IngredientDao {

    private final IngredientDao delegate;

    public CachedIngredientDao(IngredientDao delegate) {
        this.delegate = Objects.requireNonNull(
            delegate,
            "IngredientDao delegate non può essere null"
        );
    }

    @Override
    public Optional<Ingredient> findById(String name) {

        if (name == null || name.isBlank()) {
            return Optional.empty();
        }

        Optional<Ingredient> cached = getFromCache(name);

        if (cached.isPresent()) {
            return cached;
        }

        Optional<Ingredient> result = delegate.findById(name);

        result.ifPresent(
            ingredient -> putInCache(ingredient.getName(), ingredient)
        );

        return result;
    }

    @Override
    public List<Ingredient> findAll() {

        List<Ingredient> ingredients = delegate.findAll();

        for (Ingredient ingredient : ingredients) {
            putInCache(ingredient.getName(), ingredient);
        }

        return ingredients;
    }

    @Override
    public void insert(Ingredient ingredient) {

        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient non può essere null");
        }

        delegate.insert(ingredient);

        putInCache(ingredient.getName(), ingredient);
    }

    @Override
    public void deleteById(String name) {

        if (name == null || name.isBlank()) {
            return;
        }

        delegate.deleteById(name);
        removeFromCache(name);
    }
}