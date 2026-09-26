package it.foodmood.config;

public record PersistenceSettings(
    PersistenceMode mode,
    String url,
    String user,
    String pass
) {}
