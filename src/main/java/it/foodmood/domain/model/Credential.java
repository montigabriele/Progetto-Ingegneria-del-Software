package it.foodmood.domain.model;

import java.util.UUID;

public record Credential(UUID userId, String passwordHash) {
}
