package it.foodmood.domain.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MacronutrientsTest {
    @Test
    void of_validValues(){
        Macronutrients m1 = new Macronutrients(20, 20, 4);
        assertEquals(196.0, m1.kcal(), 1e-9);

        Macronutrients m2 = m1.scale(0.5);
        assertEquals(98.0, m2.kcal(), 1e-9);
    }

    @Test
    void of_negativeValues(){
        assertThrows(IllegalArgumentException.class, () -> new Macronutrients(-1, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Macronutrients(0, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> new Macronutrients(0, 0, -1));

    }

    @Test
    void createZeroMacros(){
        Macronutrients m = new Macronutrients(0,0,0);
        assertEquals(0, m.protein());
        assertEquals(0, m.carbohydrates());
        assertEquals(0, m.fat());
        assertEquals(0, m.kcal());
    }

    @Test
    void invalidRatio(){
        Macronutrients m = new Macronutrients(10,10,10);
        assertThrows(IllegalArgumentException.class, () -> m.scale(-1));
        assertThrows(IllegalArgumentException.class, () -> m.scale(Double.NaN));
    }

    @Test
    void plusMacronutrients(){
        Macronutrients m1 = new Macronutrients(10,10,10);
        Macronutrients m2 = new Macronutrients(10,25,8);
        Macronutrients result = m1.plus(m2);

        assertEquals(20, result.protein());
        assertEquals(35, result.carbohydrates());
        assertEquals(18, result.fat());
    }

    @Test
    void plusNull(){
        Macronutrients m = new Macronutrients(10,10,10);
        assertThrows(NullPointerException.class, () -> m.plus(null));
    }

    @Test
    void equals(){
        Macronutrients m1 = new Macronutrients(7, 60, 1);
        Macronutrients m2 = new Macronutrients(7, 60, 1);
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
        assertTrue(m1.toString().contains("Proteine"));
    }
}
