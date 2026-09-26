package it.foodmood.config;

import java.util.Properties;

public interface PropertiesLoader {
    Properties load(String resource);
}
