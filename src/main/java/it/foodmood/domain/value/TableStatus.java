package it.foodmood.domain.value;

import it.foodmood.exception.TableException;

public enum TableStatus {
    FREE("LIBERO"){
        @Override
        public TableStatus occupy(){
            return OCCUPIED;
        }

        @Override
        public TableStatus book(){
            return BOOKED;
        }
    },
    BOOKED("PRENOTATO"){
        @Override
        public TableStatus occupy(){
            return OCCUPIED;
        }

        @Override
        public TableStatus cancelBooking(){
            return FREE;
        }
    },
    OCCUPIED("OCCUPATO"){
        @Override
        public TableStatus free(){
            return FREE;
        }
    };

    private final String description;

    TableStatus(String description){
        this.description = description;
    }

    public String description(){
        return description;
    }

    public TableStatus occupy(){
        throw invalid("occupare", this);
    }

    public TableStatus free(){
        throw invalid("liberare", this);
    }

    public TableStatus book(){
        throw invalid("prenotare", this);
    }

    public TableStatus cancelBooking(){
        throw invalid("cancellare la prenotazione per", this);
    }

    public boolean isFree(){
        return this == FREE;
    }

    @Override
    public String toString(){
        return description;
    }

    private static RuntimeException invalid(String action, TableStatus status){
        return new TableException(action, status);
    }
}
