package it.foodmood.persistence.inmemory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

import it.foodmood.persistence.dao.CrudDao;

public abstract class AbstractInMemoryCrudDao <T, I> implements CrudDao<T,I> {
    
    protected final Map<I, T> storage = new HashMap<>();

    protected abstract I getId(T entity);

    @Override
    public void insert(T entity){
        storage.put(getId(entity), entity);
    }

    @Override
    public Optional<T> findById(I id){
        if (id == null) return Optional.empty();
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll(){
        return List.copyOf(storage.values());
    }

    @Override
    public void deleteById(I id){
        if (id == null) return;
        storage.remove(id);
    }
}
