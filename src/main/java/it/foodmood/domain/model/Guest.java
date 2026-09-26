package it.foodmood.domain.model;

import java.util.UUID;

public final class Guest {
    
    private final UUID id;

    public Guest(){
        this.id = UUID.randomUUID();
    }

    public Guest(UUID id){
        this.id = id;
    }

    public UUID getId(){
        return id;
    }
}
