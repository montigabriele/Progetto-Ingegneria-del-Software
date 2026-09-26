package it.foodmood.config;

import it.foodmood.infrastructure.bootstrap.UiMode;

public record StartupEnvironment(UiMode uiMode, PersistenceMode persistenceMode) {
    public StartupEnvironment{
        if(uiMode == null || persistenceMode == null){
            throw new IllegalArgumentException("Interfaccia utente e modalità di persistenza obbligatori.");
        }
    }
}
