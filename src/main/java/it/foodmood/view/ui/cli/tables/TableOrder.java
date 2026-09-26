package it.foodmood.view.ui.cli.tables;

import java.util.List;
import java.util.stream.IntStream;

import it.foodmood.bean.CartItemBean;

public final class TableOrder {
    
    private TableOrder(){
        // costruttore vuoto
    }

    public static List<String> orderTableHeaders(){
        return List.of("N°", "Nome", "Prezzo", "Quantità", "Subtotale");
    }

    public static List<Integer> orderColumnWidths(){
        return List.of(3, 30, 7, 9, 9);
    }

    public static List<List<String>> orderRows(List<CartItemBean> orderLines){
        return IntStream.range(0, orderLines.size()).mapToObj(i -> {
            CartItemBean cartItem = orderLines.get(i);
            String index = String.valueOf(i + 1);
            String name = cartItem.getProductName();
            String price = cartItem.getUnitPrice().toString();
            int quantity = cartItem.getQuantity();
            String subtotale = cartItem.getSubTotal().toString();

            String quantityStr = String.valueOf(quantity);

            return List.of(index, name, price, quantityStr, subtotale);
        }).toList();
    }
}
