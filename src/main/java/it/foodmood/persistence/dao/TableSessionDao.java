package it.foodmood.persistence.dao;

import java.util.Optional;
import java.util.UUID;

import it.foodmood.domain.model.TableSession;

public interface TableSessionDao {
    UUID enterOrGetOpenSession(TableSession tableSession);
    void closeSession(int tableId);

    Optional<TableSession> findById(UUID tableSessionId);
}
