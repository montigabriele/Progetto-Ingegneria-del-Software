package it.foodmood.domain.value;

public record TablePosition (int row, int col) {

    public TablePosition {
        if(row < 0 || col < 0){
            throw new IllegalArgumentException("Riga e colonna non possono essere negative");
        }
    }
}
