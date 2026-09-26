package it.foodmood.bean;

import java.util.UUID;

public class TableSessionBean {
    private UUID tableSessionId;
    private int tableId;

    public TableSessionBean(){
        // Costruttore vuoto
    }

    public UUID getTableSessionId(){
        return tableSessionId;
    }

    public int getTableId(){
        return tableId;
    }

    public void setTableSessionId(UUID tableSessionId){
        if(tableSessionId == null){
            throw new IllegalArgumentException("L'ID della sessione non può essere nullo");
        }
        this.tableSessionId = tableSessionId;
    }

    public void setTableId(int tableId){
        if(tableId <= 0){
            throw new IllegalArgumentException("Il numero del tavolo deve essere maggiore di zero");
        }
        this.tableId = tableId;
    }
}