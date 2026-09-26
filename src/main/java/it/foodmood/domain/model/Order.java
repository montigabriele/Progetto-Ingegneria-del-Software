package it.foodmood.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import it.foodmood.domain.value.Money;
import it.foodmood.domain.value.OrderStatus;

public class Order {
    
    private final UUID id;
    private final UUID userId;
    private final UUID tableSessionId;
    private final OrderStatus status;
    private final List<OrderLine> orderLines;

    public Order(UUID userId, UUID tableSessionId, List<OrderLine> lines){
        this.id = UUID.randomUUID();
        this.userId = Objects.requireNonNull(userId, "L'ID del cliente non può essere nullo");
        this.tableSessionId = Objects.requireNonNull(tableSessionId, "L'ID della sessione del tavolo non può essere nullo");
        this.status = OrderStatus.OPEN; 
        if(lines == null || lines.isEmpty()){
            throw new IllegalStateException("L'ordine non può essere vuoto");
        }
        this.orderLines = new ArrayList<>(lines);
    }

    public Order(UUID orderId, UUID userId, UUID tableSessionId, OrderStatus status, List<OrderLine> lines){
        this.id = Objects.requireNonNull(orderId, "L'ID dell'ordine non può essere nullo");
        this.userId = Objects.requireNonNull(userId, "L'ID del cliente non può essere nullo");
        this.tableSessionId = Objects.requireNonNull(tableSessionId, "L'ID della sessione del tavolo non può essere nullo");
        this.status = status;
        if(lines == null || lines.isEmpty()){
            throw new IllegalStateException("Dettagli ordine mancanti");
        }
        this.orderLines = new ArrayList<>(lines);
    }

    public UUID getUserId(){
        return userId;
    }

    public UUID getId(){
        return id;
    }

    public UUID getTableSessionId(){
        return tableSessionId;
    }

    public OrderStatus getStatus(){
        return status;
    }

    public List<OrderLine> getOrderLines(){
        return List.copyOf(orderLines);
    }

    public Money getTotal(){
        Money total = Money.zero();
        for(OrderLine line : orderLines){
            total = total.add(line.getSubtotal());
        }
        return total;
    }
}