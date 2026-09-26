package it.foodmood.config;

import it.foodmood.persistence.dao.DaoFactory;

public record ApplicationEnvironment(ApplicationConfig config, DaoFactory daoFactory) {}
