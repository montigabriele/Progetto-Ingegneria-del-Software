package it.foodmood.domain.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class QuantityTest {
    @Test
    void of_invalidAmount(){
        assertThrows(IllegalArgumentException.class, () -> new Quantity(0, Unit.GRAM));
        assertThrows(IllegalArgumentException.class, () -> new Quantity(-5, Unit.MILLILITER));
    }

    @Test
    void of_nullUnit(){
        assertThrows(NullPointerException.class, () -> new Quantity(10, null));
    }

    @Test
    void ratioToBase_returnsFraction(){
        Quantity q = new Quantity(50, Unit.GRAM);
        assertEquals(0.5, q.ratioToBase(), 1e-9);
    }

    @Test
    void createGramQuantity(){
        Quantity q = new Quantity(150, Unit.GRAM);
        assertEquals(150, q.amount());
        assertEquals(Unit.GRAM, q.unit());
    }

    @Test
    void createMilliliterQuantity(){
        Quantity q = new Quantity(250, Unit.MILLILITER);
        assertEquals(250, q.amount());
        assertEquals(Unit.MILLILITER, q.unit());
    }

    @Test
    void multiplyAmount(){
        Quantity q = new Quantity(100, Unit.GRAM);
        Quantity scaled = q.scale(2.5);
        assertEquals(250, scaled.amount());
    }

    @Test
    void invalidFactor(){
        Quantity q = new Quantity(100, Unit.GRAM);
        assertThrows(IllegalArgumentException.class, () -> q.scale(0));
        assertThrows(IllegalArgumentException.class, () -> q.scale(-1));
        assertThrows(IllegalArgumentException.class, () -> q.scale(Double.NaN));
    }

    @Test
    void addsQuantities(){
        Quantity q1 = new Quantity(100, Unit.GRAM);
        Quantity q2 = new Quantity(50, Unit.GRAM);
        Quantity result = q1.plus(q2);
        assertEquals(150, result.amount());
    }

    @Test
    void subtractsQuantities(){
        Quantity q1 = new Quantity(100, Unit.GRAM);
        Quantity q2 = new Quantity(50, Unit.GRAM);
        Quantity result = q1.minus(q2);
        assertEquals(50, result.amount());
    }

    @Test
    void plus_incompatibleUnits(){
        Quantity q1 = new Quantity(100, Unit.GRAM);
        Quantity q2 = new Quantity(50, Unit.MILLILITER);
        assertThrows(IllegalArgumentException.class, () -> q1.plus(q2));
    }

    @Test
    void minus_incompatibleUnits(){
        Quantity q1 = new Quantity(100, Unit.GRAM);
        Quantity q2 = new Quantity(50, Unit.MILLILITER);
        assertThrows(IllegalArgumentException.class, () -> q1.minus(q2));
    }

    @Test
    void resultLessThanZero(){
        Quantity q1 = new Quantity(50, Unit.GRAM);
        Quantity q2 = new Quantity(100, Unit.GRAM);
        assertThrows(IllegalArgumentException.class, () -> q1.minus(q2));
    }

    @Test
    void compareTo_comparesAmount(){
        Quantity q1 = new Quantity(50, Unit.GRAM);
        Quantity q2 = new Quantity(150, Unit.GRAM);
        Quantity q3 = new Quantity(50, Unit.GRAM);

        assertTrue(q1.compareTo(q2) < 0);
        assertTrue(q2.compareTo(q1) > 0);
        assertEquals(0, q1.compareTo(q3));
    }

    @Test
    void compareTo_incompatibleUnit(){
        Quantity q1 = new Quantity(100, Unit.GRAM);
        Quantity q2 = new Quantity(50, Unit.MILLILITER);
        assertThrows(IllegalArgumentException.class, () -> q1.compareTo(q2));
    }

    @Test
    void equals(){
        Quantity q1 = new Quantity(200, Unit.MILLILITER);
        Quantity q2 = new Quantity(200, Unit.MILLILITER);
        assertEquals(q1, q2);
        assertEquals(q1.hashCode(), q2.hashCode());
        assertTrue(q1.toString().contains("ml"));
    }
}
