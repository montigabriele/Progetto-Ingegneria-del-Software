package it.foodmood.infrastructure.bootstrap;

public enum UiMode {
    CLI, GUI;

    public static UiMode parse(String mode, UiMode fallback){
        if(mode == null || mode.isBlank()) return fallback;
        String view = mode.trim().toLowerCase();
        return switch(view){
            case "cli" -> CLI;
            case "gui" -> GUI;
            default -> fallback;
        };
    }
}
