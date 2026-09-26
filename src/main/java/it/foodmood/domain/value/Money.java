package it.foodmood.domain.value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {
    private final BigDecimal amount;

    public Money(BigDecimal amount){
        Objects.requireNonNull(amount, "L'importo non può essere nullo");
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("L'importo non può essere negativo");
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    // costruttore per double
    public Money(double amount){
        this(BigDecimal.valueOf(amount));
    }

    public BigDecimal getAmount() { return amount;}

    public Money add(Money other){
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other){
        return new Money(this.amount.subtract(other.amount));
    }

    // Metodo utile per moltiplicare l'importo per un fattore (prezzo x quantità)
    public Money multiply(int factor){
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)));
    }

    public Money divide(double divisor){
        if(divisor == 0){
            throw new ArithmeticException("Attenzione, non puoi dividere per 0!");
        }
        BigDecimal result = this.amount.divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP);
        return new Money(result);
    }

    public boolean isZero(){
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isPositive(){
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public static Money zero(){
        return new Money(BigDecimal.ZERO);
    }

    public double toDouble(){
        return amount.doubleValue();
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Money m)) return false;
        return amount.compareTo(m.amount) == 0;
    }

    @Override
    public int hashCode(){
        return Objects.hash(amount);
    }

    @Override
    public String toString(){
        return amount + " €";
    }
}
