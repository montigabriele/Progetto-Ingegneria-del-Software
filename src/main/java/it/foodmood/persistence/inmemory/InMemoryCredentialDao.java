package it.foodmood.persistence.inmemory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import it.foodmood.domain.model.Credential;
import it.foodmood.persistence.dao.CredentialDao;

public final class InMemoryCredentialDao implements CredentialDao {

    private final Map<UUID, Credential> storage = new HashMap<>();

    public InMemoryCredentialDao(){
        // costruttore
    }

    @Override
    public void saveCredential(Credential credential){
        storage.put(credential.userId(), credential);
    }

    @Override
    public Credential findByUserId(UUID userId){
        return storage.get(userId);
    }
}
