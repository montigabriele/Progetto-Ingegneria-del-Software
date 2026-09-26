package it.foodmood.utils;

import it.foodmood.bean.MacronutrientsBean;

public final class NutritionUtils {
    private NutritionUtils(){
        // costruttore vuoto
    }

    public static String formatKcal(MacronutrientsBean macro) {
        if(macro == null) return "-";
        return String.format("%.1f", macro.calculateKcal());
    }

    public static String formatMacros(MacronutrientsBean macro) {
        if(macro == null) return "-";
        double protein = macro.getProtein()       == null ? 0.0 : macro.getProtein();
        double carbohydrate = macro.getCarbohydrates() == null ? 0.0 : macro.getCarbohydrates();
        double fat = macro.getFat()       == null ? 0.0 : macro.getFat();
        return String.format("%.1f / %.1f / %.1f", protein, carbohydrate, fat);
    } 
}
