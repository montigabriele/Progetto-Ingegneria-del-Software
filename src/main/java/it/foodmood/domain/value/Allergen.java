package it.foodmood.domain.value;

/*
 * Elenco ufficiale dei 14 allergeni da dichiarare
 * -GRASP: Information Expert
 */

public enum Allergen {
    GLUTEN("Glutine"),
    CRUSTACEANS("Crostacei"),
    EGGS("Uova"),
    FISH("Pesce"),
    PEANUTS("Arachidi"),
    SOY("Soia"),
    MILK("Latte"),
    NUTS("Frutta a guscio"),
    CELERY("Sedano"),
    MUSTARD("Senape"),
    SESAME("Semi di sesamo "),
    SULPHITES("Anidrite solforosa"),
    LUPIN("Lupini"),
    MOLLUSCS("Molluschi");

    private final String description;

    Allergen(String description){
        this.description = description;
    }

    public String description(){
        return description;
    }

    public static Allergen fromName(String value){
        if(value == null || value.isBlank()){
            throw new IllegalArgumentException("Allergene nullo o vuoto");
        }
        return Allergen.valueOf(value.trim());
    }

    @Override
    public String toString(){
        return name() + " - " + description;
    }
}
