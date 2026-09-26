package it.foodmood.view.ui.cli;

import java.util.List;

import it.foodmood.bean.CartItemBean;
import it.foodmood.bean.DishBean;
import it.foodmood.bean.IngredientBean;
import it.foodmood.view.ui.cli.tables.TableDishes;
import it.foodmood.view.ui.cli.tables.TableIngredients;
import it.foodmood.view.ui.cli.tables.TableOrder;
import it.foodmood.view.ui.cli.tables.TablePropose;

public abstract class TableConsoleView extends ConsoleView{

    protected void showIngredientTable(List<IngredientBean> ingredients){

        if(ingredients == null || ingredients.isEmpty()){
            showWarning("Nessun ingrediente presente.");
            waitForEnter(null);
            return;
        }

        List<String> headers = TableIngredients.ingredientTableHeaders();
        List<List<String>> rows = TableIngredients.ingredientRows(ingredients);
        List<Integer> columnWidths = TableIngredients.ingredientColumnWidths();
        displayTable(headers, rows, columnWidths);

    }

    protected void showDishTable(List<DishBean> dishes){
        if(dishes == null || dishes.isEmpty()){
            showWarning("Nessun piatto presente.");
            waitForEnter(null);
            return;
        }
        List<String> headers = TableDishes.dishTableHeaders();
        List<List<String>> rows = TableDishes.dishRows(dishes);
        List<Integer> columnWidths = TableDishes.dishColumnWidths();
        displayTable(headers, rows, columnWidths);
    }

    protected void showProposeTable(List<DishBean> dishes){
        if(dishes == null || dishes.isEmpty()){
            showWarning("Nessun piatto presente.");
            waitForEnter(null);
            return;
        }
        List<String> headers = TablePropose.dishTableHeaders();
        List<List<String>> rows = TablePropose.dishRows(dishes);
        List<Integer> columnWidths = TablePropose.dishColumnWidths();
        displayTable(headers, rows, columnWidths);
    }

    protected void showOrderRecapTable(List<CartItemBean> lines){
        if(lines == null || lines.isEmpty()){
            showWarning("Nessun articolo presente.");
            waitForEnter(null);
            return;
        }
        List<String> headers = TableOrder.orderTableHeaders();
        List<List<String>> rows = TableOrder.orderRows(lines);
        List<Integer> columnWidths = TableOrder.orderColumnWidths();
        displayTable(headers, rows, columnWidths);
    }

}

