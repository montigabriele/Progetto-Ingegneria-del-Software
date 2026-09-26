package it.foodmood.domain.model;

import it.foodmood.domain.value.Allergen;
import it.foodmood.domain.value.CourseType;
import it.foodmood.domain.value.DietCategory;
import it.foodmood.domain.value.DishParams;
import it.foodmood.domain.value.DishState;
import it.foodmood.domain.value.Image;
import it.foodmood.domain.value.IngredientPortion;
import it.foodmood.domain.value.Macronutrients;
import it.foodmood.domain.value.Money;

import java.util.*;

public class Dish {
    private final UUID id;
    private String name;
    private String description;
    private CourseType courseType;
    private EnumSet<DietCategory> dietCategories;
    private List<IngredientPortion> ingredients;
    private DishState state;
    private Image image;
    private Money price;

    private Dish(UUID id, DishParams params) {
        this.id = Objects.requireNonNull(id);

        this.name = Objects.requireNonNull(params.name(), "Il nome non può essere nullo").trim();
        if(this.name.isEmpty()) throw new IllegalArgumentException("Il nome non può essere vuoto");
        
        this.description = params.description(); // opzionale
        this.courseType = Objects.requireNonNull(params.courseType(), "Il tipo della portata non può essere nullo");

        this.dietCategories = normalizeDietCategories(params.dietCategories());

        this.ingredients = new ArrayList<>();
        if(params.ingredients() != null){
            for(IngredientPortion portion : params.ingredients()){
                this.ingredients.add(Objects.requireNonNull(portion, "La porzione dell'ingrediente non può essere nulla"));
            }
        }

        this.state = Objects.requireNonNull(params.state(), "Lo stato non può essere nullo");
        this.image = params.image(); // opzionale

        this.price = Objects.requireNonNull(params.price(), "Il prezzo non può essere nullo");
        if(!this.price.isPositive()) throw new IllegalArgumentException("Il prezzo deve essere maggiore di 0");
    }

    public static Dish create(DishParams params) {
        return new Dish( UUID.randomUUID(), params );
    }

    public static Dish fromPersistence(UUID id, DishParams params) {
        return new Dish(id, params);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public Set<DietCategory> getDietCategories() {
        return Collections.unmodifiableSet(dietCategories);
    }

    public List<IngredientPortion> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    public DishState getState() {
        return state;
    }

    public Image getImage() {
        return image;
    }

    public Money getPrice() {
        return price;
    }

    public void setIngredients(List<IngredientPortion> ingredients) {
        List<IngredientPortion> copy = new ArrayList<>();
        if(ingredients != null){
            for(IngredientPortion portion : ingredients){
                copy.add(Objects.requireNonNull(portion, "L'ingrediente non può essere nullo"));
            }
        }
        this.ingredients = copy;
    }

    private static EnumSet<DietCategory> normalizeDietCategories(Set<DietCategory> dietCategory){
        Objects.requireNonNull(dietCategory,"La categoria dietetica non può essere nulla.");
        if(dietCategory.isEmpty()){
            throw new IllegalArgumentException("Il piatto deve avere almeno una categoria dietetica");
        }

        EnumSet<DietCategory> normalized = EnumSet.copyOf(dietCategory);

        if(normalized.contains(DietCategory.VEGAN)){
            normalized.add(DietCategory.VEGETARIAN);
            normalized.add(DietCategory.LACTOSE_FREE);
        }

        return normalized;
    }

    // Metodi
    public void addIngredient(IngredientPortion portion){
        Objects.requireNonNull(portion, "L'ingrediente non può essere nullo");
        this.ingredients.add(portion);
    }

    public void removeIngredient(IngredientPortion portion){
        if(portion == null) return;
        this.ingredients.remove(portion);
    }

    public void addDietCategory(DietCategory category){
        Objects.requireNonNull(category, "La categoria non può essere nulla");
        this.dietCategories.add(category);

        if(category == DietCategory.VEGAN){
            dietCategories.add(DietCategory.VEGETARIAN);
            dietCategories.add(DietCategory.LACTOSE_FREE);
        }
    }

    public void removeDietCategory(DietCategory category){
        Objects.requireNonNull(category, "La categoria non può essere nulla");

        if(category == DietCategory.VEGETARIAN && dietCategories.contains(DietCategory.VEGAN)){
            throw new IllegalStateException("Un piatto vegano è sempre vegetariano");
        }

        if(category == DietCategory.LACTOSE_FREE && dietCategories.contains(DietCategory.VEGAN)){
            throw new IllegalStateException("Un piatto vegano è sempre senza lattosio");
        }

        this.dietCategories.remove(category);

        if(this.dietCategories.isEmpty()){
            throw new IllegalStateException("Il piatto deve avere almeno una categoria dietetica");
        }
    }

    public Macronutrients totalMacronutrients(){
        Macronutrients total = new Macronutrients(0, 0, 0);

        for(IngredientPortion portion : ingredients){
            Macronutrients macronutrients = portion.ingredient().getMacroFor(portion.quantity());
            total = total.plus(macronutrients);
        }
        return total;
    }

    public void changeName(String newName){
        this.name = Objects.requireNonNull(newName, "Il nuovo nome non può essere nullo").trim();
        if(this.name.isEmpty()) throw new IllegalArgumentException("Il nome non può essere vuoto");
    }

    public void changeState(DishState newState){
        this.state = Objects.requireNonNull(newState, "Il nuovo stato non può essere nullo");
    }

    public void changeImage(Image newImage){
        this.image = newImage;
    }

    public void changePrice(Money newPrice){
        this.price = Objects.requireNonNull(newPrice, "Il nuovo prezzo non può essere nullo");
        if(!this.price.isPositive()) throw new IllegalArgumentException("Il prezzo deve essere maggiore di 0");
    }

    public void changeDescription(String newDescription){
        this.description = newDescription;
    }

    public void changeDietCategories(Set<DietCategory> newCategories){
        this.dietCategories = normalizeDietCategories(newCategories);
    }

    public void changeCourseType(CourseType newCourseType){
        this.courseType = Objects.requireNonNull(newCourseType, "La nuova tipologia della portata non può essere nulla");
    }

    public Set<Allergen> allergens(){

        Set<Allergen> result = new HashSet<>();

        for(IngredientPortion portion : ingredients){
            result.addAll(portion.ingredient().getAllergens());
        }

        return Collections.unmodifiableSet(result);
    }

    public boolean isAllergenic(){
        return !allergens().isEmpty();
    }

    public boolean containsAllergen(Allergen allergen){
        Objects.requireNonNull(allergen, "L'allergene non può essere nullo");
        return allergens().contains(allergen);
    }

    public boolean isAvailable(){
        return state.available();
    }

    public double getKcal(){
        return Math.floor(totalMacronutrients().kcal());
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Dish other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }
}
