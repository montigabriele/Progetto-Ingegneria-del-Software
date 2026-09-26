package it.foodmood.utils;

import it.foodmood.domain.value.Unit;

public final class UnitUtils {
    
    private UnitUtils(){
        // costruttore vuoto
    }

    public static String toLabel(Unit unit){
        if(unit == null) return "";
        return(unit == Unit.GRAM) ? "g" : "ml";
    }
}
