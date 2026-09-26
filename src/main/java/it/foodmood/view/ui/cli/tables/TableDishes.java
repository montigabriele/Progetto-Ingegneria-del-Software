package it.foodmood.view.ui.cli.tables;

import java.util.List;
import java.util.stream.IntStream;

import it.foodmood.bean.DishBean;

public final class TableDishes {
    
    private TableDishes(){
        // costruttore vuoto
    }

    public static List<String> dishTableHeaders(){
        return List.of("N°", "Nome", "Prezzo");
    }

    public static List<Integer> dishColumnWidths(){
        return List.of(3, 40, 7);
    }

    public static List<List<String>> dishRows(List<DishBean> dishes){
        return IntStream.range(0, dishes.size()).mapToObj(i -> {
            DishBean dish = dishes.get(i);
            String index = String.valueOf(i + 1);
            String name = dish.getName();
            String price = dish.getPrice().toString();

            return List.of(index, name, price);
        }).toList();
    }
}
