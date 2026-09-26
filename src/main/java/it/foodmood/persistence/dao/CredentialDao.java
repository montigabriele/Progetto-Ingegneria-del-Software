package it.foodmood.persistence.dao;

import java.util.UUID;

import it.foodmood.domain.model.Credential;

public interface CredentialDao {
    
    // Salvo le credenziali
    void saveCredential(Credential credential);

    // recupero le credenziali associate ad un utente
    Credential findByUserId(UUID userId);
}
