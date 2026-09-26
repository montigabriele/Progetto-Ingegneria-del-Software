package it.foodmood.config;

public enum PersistenceMode {
    DEMO("demo","Modalità dimostrativa, con persistenza in memoria volatile."),
    FULL("full","Modalità completa, con persistenza su database MySQL."),
    FILESYSTEM("filesystem", "Modalità completa, con persistenza su file system locale.");

    private final String key;
    private final String description;

    PersistenceMode(String key, String description){
        this.key = key;
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }

    public String getKey(){
        return key;
    }

    public static PersistenceMode fromValue(String value){
        if(value == null){
            return DEMO;
        }

        String normalized = value.toLowerCase().trim();

        for (PersistenceMode mode : values()) {
            if(mode.key.equals(normalized)){
                return mode;
            }
        }

        return DEMO;
    }
}
