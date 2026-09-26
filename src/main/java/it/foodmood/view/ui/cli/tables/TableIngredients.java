package it.foodmood.view.ui.cli.tables;

import java.util.List;
import java.util.stream.IntStream;

import it.foodmood.bean.IngredientBean;
import it.foodmood.bean.MacronutrientsBean;
import it.foodmood.domain.value.Unit;

public final class TableIngredients{
    
    private TableIngredients(){
        // costruttore vuoto
    }

    public static List<String> ingredientTableHeaders(){
        return List.of("N°", "Nome", "Unità", "Proteine", "Carboidrati", "Grassi", "Allergeni");
    }

    public static List<Integer> ingredientColumnWidths(){
        return List.of(3, 20,5,8,11,6,20);
    }

    public static List<List<String>> ingredientRows(List<IngredientBean> ingredients){
        return IntStream.range(0, ingredients.size()).mapToObj(i -> {
            IngredientBean ingredient = ingredients.get(i);
            String index = String.valueOf(i + 1);
            String name = ingredient.getName();
            String unit = ingredient.getUnit() == Unit.GRAM ? "g" : "ml";
            MacronutrientsBean macro = ingredient.getMacronutrients();
            String protein = String.format("%.1f", macro.getProtein());
            String carbs = String.format("%.1f", macro.getCarbohydrates());
            String fat = String.format("%.1f", macro.getFat());

            String allergens = (ingredient.getAllergens().isEmpty()) ? "-" : String.join(", ", ingredient.getAllergens());

            return List.of(index, name, unit, protein, carbs, fat, allergens);
        }).toList();
    }
}
