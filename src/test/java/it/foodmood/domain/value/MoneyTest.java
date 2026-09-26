package it.foodmood.domain.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MoneyTest {
    @Test
    void of_validAmount(){
        Money m = new Money(14.399);
        assertEquals("14.40 €", m.toString());
    }

    @Test
    void of_invalidAmount(){
        assertThrows(IllegalArgumentException.class, () -> new Money(-1));
    }

    @Test
    void add_returnsSum(){
        Money a = new Money(5.11);
        Money b = new Money(2.89);
        assertEquals("8.00 €", a.add(b).toString());
    }

    @Test
    void subtract_returnsDifference(){
        Money a = new Money(5.);
        Money b = new Money(2.55);
        assertEquals("2.45 €", a.subtract(b).toString());  
    }

    @Test
    void multiply_scaleAmount(){
        Money m = new Money(2.50);
        assertEquals("5.00 €", m.multiply(2).toString());
    }

    @Test
    void divide_retursRoundedResult(){
        Money m = new Money(10.00);
        Money result = m.divide(3);
        assertEquals("3.33 €", result.toString());
    }


    @Test
    void divide_byZero(){
        Money m = new Money(10.00);
        assertThrows(ArithmeticException.class,() -> m.divide(0));
    }

    @Test
    void toString_formatsWithEuro(){
        Money m = new Money(14.7);
        assertTrue(m.toString().endsWith("€"));
    }
}
