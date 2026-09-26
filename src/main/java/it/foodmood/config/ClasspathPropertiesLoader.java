package it.foodmood.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ClasspathPropertiesLoader implements PropertiesLoader {

    @Override
    public Properties load(String resource){
        try(InputStream in =Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)){
            if (in == null) {
                throw new IllegalStateException("Il file " + resource + " non trovato.");
            }
            Properties p = new Properties();
            p.load(in);
            return p;
        } catch (IOException _){
            throw new IllegalStateException("Errore durante la lettura del file: " + resource);
        }
    }

}
