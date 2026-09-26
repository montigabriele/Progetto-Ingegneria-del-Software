package it.foodmood.persistence.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T,I> {
    void insert(T entity);
    Optional<T> findById(I id);
    List<T> findAll();
    void deleteById(I id);
}
