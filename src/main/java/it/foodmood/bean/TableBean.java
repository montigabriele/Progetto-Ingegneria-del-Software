package it.foodmood.bean;

import it.foodmood.domain.value.TableStatus;

public class TableBean {
    private int id;
    private int seats;
    private int row;
    private int col;
    private TableStatus status;

    public TableBean(){
        // costruttore vuoto
    }

    public int getId(){
        return id;
    }

    public int getSeats(){
        return seats;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public TableStatus getStatus(){
        return status;
    }

    public void setId(int id){
        if(id < 0){
            throw new IllegalArgumentException("L'id del tavolo non può essere negativo");
        }
        this.id = id;
    }

    public void setSeats(int seats){
        this.seats = seats;
    }

    public void setRow(int row){
        if(row < 0){
            throw new IllegalArgumentException("La riga non può essere negativa");
        }
        this.row = row;
    }

    public void setCol(int col){
        if(col < 0){
            throw new IllegalArgumentException("La colonna non può essere negativa");
        }
        this.col = col;
    }

    public void setStatus(TableStatus status){
        if(status == null){
            throw new IllegalArgumentException("Lo stato del tavolo non può essere nullo");
        }
        this.status = status;
    }
}
