package it.foodmood.utils;

import it.foodmood.domain.value.Role;

public final class RoleConverter {

    private RoleConverter(){}


    public static Role fromString(String role){
        if(role == null) {
            throw new IllegalArgumentException("Il ruolo non può essere nullo.");
        }
        String key = role.trim().toUpperCase();
        return switch (key) {
            case "WAITER" -> Role.WAITER;
            case "MANAGER" -> Role.MANAGER;
            case "CUSTOMER" -> Role.CUSTOMER;
            default -> throw new IllegalArgumentException("Ruolo non valido: " + role);
        };
    }
}
