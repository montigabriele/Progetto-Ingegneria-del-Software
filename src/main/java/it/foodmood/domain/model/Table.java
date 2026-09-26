package it.foodmood.domain.model;

import java.util.Objects;
import java.util.Set;

import it.foodmood.domain.value.TablePosition;
import it.foodmood.domain.value.TableStatus;

public class Table {
    private final int id;

    private final int seats;
    private TablePosition position;
    private TableStatus status;

    private static final Set<Integer> ALLOWED_SEATS = Set.of(2,4,6,8);

    public Table(int id, int seats, TablePosition position){
        this(id, seats, position, TableStatus.FREE);
    }

    public Table(int id, int seats, TablePosition position, TableStatus status){
        if(id < 0) throw new IllegalArgumentException("Id tavolo non valido");
        this.id = id;
        validateSeats(seats);
        this.seats = seats;
        this.position = Objects.requireNonNull(position, "La posizione del tavolo non può essere nulla");
        this.status = Objects.requireNonNull(status, "Lo stato del tavolo non può essere nullo");
    }

    public int getId(){
        return id;
    }

    public int getSeats(){
        return seats;
    }
        
    public TablePosition getPosition(){
        return position;
    }

    public TableStatus getStatus(){
        return status;
    }

    public void moveTable(TablePosition newTablePosition){
        this.position = Objects.requireNonNull(newTablePosition, "La nuova posizione non può essere nulla");
    }

    public void occupy(){
        status = status.occupy();
    }

    public void free(){
        status = status.free();
    }

    public void book(){
        status = status.book();
    }

    public void cancelBooking(){
        status = status.cancelBooking();
    }

    private static void validateSeats(int seats){
        if(!ALLOWED_SEATS.contains(seats)){
            throw new IllegalArgumentException("Tavolo con '" + seats + "' posti non valido per il dominio");
        }
    }
}
