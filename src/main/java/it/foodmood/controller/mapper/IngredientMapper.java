package it.foodmood.controller.mapper;

import it.foodmood.bean.IngredientBean;
import it.foodmood.bean.MacronutrientsBean;
import it.foodmood.domain.model.Ingredient;
import it.foodmood.domain.value.Allergen;

public final class IngredientMapper {
    
    private IngredientMapper(){
        // costruttore vuoto
    }

    public static IngredientBean toBean(Ingredient ingredient){
        IngredientBean ingredientBean = new IngredientBean();
        ingredientBean.setName(ingredient.getName());

        MacronutrientsBean macronutrientsBean = new MacronutrientsBean();
        macronutrientsBean.setProtein(ingredient.getMacro().protein());
        macronutrientsBean.setCarbohydrates(ingredient.getMacro().carbohydrates());
        macronutrientsBean.setFat(ingredient.getMacro().fat());

        ingredientBean.setMacronutrients(macronutrientsBean);

        ingredientBean.setUnit(ingredient.getUnit());

        ingredientBean.setAllergens(ingredient.getAllergens().stream().map(Allergen::description).toList());

        return ingredientBean;
    }
}
