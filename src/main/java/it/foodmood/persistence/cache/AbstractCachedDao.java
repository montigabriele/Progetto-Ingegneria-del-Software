package it.foodmood.persistence.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractCachedDao<T, I> {

    private final Map<I, T> cache = new ConcurrentHashMap<>();

    protected Optional<T> getFromCache(I id) {
        return Optional.ofNullable(cache.get(id));
    }

    protected void putInCache(I id, T entity) {
        cache.put(id, entity);
    }

    protected void removeFromCache(I id) {
        cache.remove(id);
    }

    protected void clearCache() {
        cache.clear();
    }
}