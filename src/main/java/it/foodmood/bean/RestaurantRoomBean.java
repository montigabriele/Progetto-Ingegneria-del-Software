package it.foodmood.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantRoomBean {
    private int rows;
    private int cols;
    private final List<TableBean> tables = new ArrayList<>();

    public RestaurantRoomBean(){
        // costruttore vuoto
    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }

    public List<TableBean> getTables(){
        return Collections.unmodifiableList(tables);
    }

    public void setRows(int rows){
        if(rows <= 0){
            throw new IllegalArgumentException("La sala deve avere almeno una riga");
        }
        this.rows = rows;
    }

    public void setCols(int cols){
        if(cols <= 0){
            throw new IllegalArgumentException("La sala deve avere almeno una colonna");
        }
        this.cols = cols;
    }

    public void setTables(List<TableBean> tableBeans){
        this.tables.clear();
        if(tableBeans != null){
            this.tables.addAll(tableBeans);
        }
    }
}
