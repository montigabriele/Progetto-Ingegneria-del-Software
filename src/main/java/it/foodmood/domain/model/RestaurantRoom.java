package it.foodmood.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.foodmood.domain.value.TablePosition;

public class RestaurantRoom {
    
    private final int rows;
    private final int cols;
    private final List<Table> tables = new ArrayList<>();
    private int nextId = 1;

    public RestaurantRoom(int rows, int cols){
        if(rows <= 0 || cols <= 0){
            throw new IllegalArgumentException("La sala deve avere almeno una riga e una colonna");
        }
        this.rows = rows;
        this.cols = cols;
    }

    public RestaurantRoom(int rows, int cols, List<Table> existingTables){
        if(rows <= 0 || cols <= 0){
            throw new IllegalArgumentException("La sala deve avere almeno una riga e una colonna");
        }
        this.rows = rows;
        this.cols = cols;

        if(existingTables != null){
            this.tables.addAll(existingTables);
        }

        this.nextId = this.tables.stream().mapToInt(Table::getId).max().orElse(-1) + 1;
    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }

    public Table addTable(int seats, int row, int col){
        TablePosition position = new TablePosition(row, col);

        if(!isInside(position)){
            throw new IllegalArgumentException("Posizione fuori dalla sala");
        }

        if(!isCellAvailable(position)){
            throw new IllegalArgumentException("Posizione già occupata");
        }

        Table table = new Table(nextId++, seats, position);
        tables.add(table);
        return table;
    }

    public void moveTable(int tableId, int newRow, int newCol){
        Table table = findById(tableId);
        TablePosition newPos = new TablePosition(newRow, newCol);

        if(!isInside(newPos)){
            throw new IllegalArgumentException("Posizione fuori dalla sala");
        }

        if(!isCellAvailableForTable(tableId, newPos)){
            throw new IllegalArgumentException("Posizione già occupata");
        }

        table.moveTable(newPos);
    }

    public void removeTable(int id){
        tables.removeIf(t -> t.getId() == id);
    }

    public void removeAllTables(){
        tables.clear();
        nextId = 0;
    }

    public List<Table> getTables(){
        return Collections.unmodifiableList(tables);
    }

    public boolean isCellAvailable(TablePosition pos){
        return tables.stream().noneMatch(t -> t.getPosition().equals(pos));
    }

    private boolean isCellAvailableForTable(int tableId, TablePosition pos){
        return tables.stream().filter(t -> t.getId() != tableId).noneMatch(t -> t.getPosition().equals(pos));
    }

    private boolean isInside(TablePosition pos){
        return pos.row() >= 0 && pos.row() < rows &&
               pos.col() >= 0 && pos.col() < cols;
    }

    public Table findById(int id){
        return tables.stream().filter(t -> t.getId() == id).findFirst().orElseThrow(() -> new IllegalArgumentException("Tavolo " + id + " non trovato."));
    }
}
