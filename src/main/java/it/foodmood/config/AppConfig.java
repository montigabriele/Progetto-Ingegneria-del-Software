package it.foodmood.config;

public interface AppConfig {
    PersistenceMode getPersistenceMode();
    UserMode getUserMode();
    String getDbUrl();
    String getDbUser();
    String getDbPass();
}
