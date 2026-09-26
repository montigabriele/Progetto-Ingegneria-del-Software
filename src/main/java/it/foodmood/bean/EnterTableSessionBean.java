package it.foodmood.bean;

public class EnterTableSessionBean {
    private String tableNumber = "";

    public EnterTableSessionBean(){
        // Costruttore vuoto
    }

    public void setTableNumber(String tableNumber){
        this.tableNumber = tableNumber == null ? "" : tableNumber.trim();
    }

    public int getTableId(){
        if(tableNumber.isBlank()){
            throw new IllegalArgumentException("Inserisci un numero di tavolo");
        }

        if(!tableNumber.matches("\\d+")){
            throw new IllegalArgumentException("Inserisci un numero di tavolo valido");
        }

        int tableId = Integer.parseInt(tableNumber);

        if(tableId <= 0){
            throw new IllegalArgumentException("Il numero del tavolo deve essere maggiore di zero");
        }
        
        return tableId;
    }
} 
