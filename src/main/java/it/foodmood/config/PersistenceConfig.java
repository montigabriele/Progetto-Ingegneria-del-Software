package it.foodmood.config;

import java.util.Objects;

import it.foodmood.persistence.ConnectionProvider;

public final class PersistenceConfig {

    private final PersistenceSettings settings;
    private final ConnectionProvider provider;

    public PersistenceConfig(PersistenceSettings settings){
        this.settings = Objects.requireNonNull(settings, "Settings non può essere nullo.");

        if(settings.mode() == PersistenceMode.FULL){
            JdbcConnectionManager.init(settings.url(), settings.user(), settings.pass());
            this.provider = JdbcConnectionManager.getInstance();
        } else {
            this.provider = null;
        }
    }

    public PersistenceSettings getSettings(){
        return settings;
    }

    public ConnectionProvider getProvider(){
        if(provider == null){
            throw new IllegalStateException("Provider non inizializzato.");
        }
        return provider;
    }
}
