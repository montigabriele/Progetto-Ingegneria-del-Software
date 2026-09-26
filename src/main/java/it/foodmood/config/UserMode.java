package it.foodmood.config;

import it.foodmood.domain.value.Role;

public enum UserMode {
    CUSTOMER(Role.CUSTOMER),
    WAITER(Role.WAITER),
    MANAGER(Role.MANAGER);

    private final Role requireRole;

    UserMode(Role requireRole){
        this.requireRole = requireRole;
    }

    public Role requireRole(){
        return requireRole;
    }

    public static UserMode fromValue(String value){
        if(value == null || value.isBlank()){
            throw new IllegalArgumentException("Modalità di avvio utente mancante.");
        }
        return switch(value.toLowerCase()){
            case "customer" -> CUSTOMER;
            case "waiter" -> WAITER;
            case "manager" -> MANAGER;
            default -> throw new IllegalArgumentException("Modalità di avvio utente non valida: " + value);
        };
    }
}
