package it.foodmood.domain.model;

import java.util.Objects;
import java.util.UUID;

public class TableSession {
    
    private final UUID id; // ID sessione
    private final int tableId; // Numero del tavolo
    private boolean open; // Stato della sessione

    // Creo la sessione assegnando un UUID randomico, il numero del tavolo, stato Aperto
    public static TableSession create(int tableId){
        return new TableSession(UUID.randomUUID(), tableId, true);
    }

    public static TableSession fromPersistence(UUID id, int tableId, boolean open){
        return new TableSession(id, tableId, open);
    }

    private TableSession(UUID id, int tableId, boolean open){
        this.id = Objects.requireNonNull(id, "L'ID della sessione non può essere nullo");
        if(tableId <= 0) throw new IllegalArgumentException("Il numero del tavolo deve essere maggiore di zero");
        this.tableId = tableId;
        this.open = open;
    }

    public UUID getTableSessionId(){
        return id;
    }

    public int getTableId(){
        return tableId;
    }

    public boolean isOpen(){
        return open;
    }

    public void close(){
        if(!open) throw new IllegalStateException("Sessione tavolo già chiusa");
        open = false;
    }
}
