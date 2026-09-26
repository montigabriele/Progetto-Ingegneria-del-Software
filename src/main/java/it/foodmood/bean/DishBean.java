package it.foodmood.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.math.RoundingMode;

import it.foodmood.domain.value.CourseType;
import it.foodmood.domain.value.DietCategory;
import it.foodmood.domain.value.DishState;

public class DishBean {
    
    private String id;
    private String name;
    private String description;
    private CourseType courseType;
    private Set<DietCategory> dietCategories;
    private BigDecimal price;
    private Double kcal;
    private String imageUri;
    private DishState state;
    private List<IngredientPortionBean> ingredients;

    // Costruttore
    public DishBean(){
        this.ingredients = new ArrayList<>();
        this.dietCategories = EnumSet.noneOf(DietCategory.class);
    }

    // Getter
    public String getId() { return id;}

    public String getName(){ return name;}

    public String getDescription(){ return description;}

    public CourseType getCourseType(){ return courseType;}

    public Set<DietCategory> getDietCategories(){ 
        return EnumSet.copyOf(dietCategories);
    }

    public BigDecimal getPrice(){ return price;}

    public Double getKcal(){ return kcal;}

    public String getImageUri(){ return imageUri;}

    public DishState getState(){ return state;}

    public List<IngredientPortionBean> getIngredients(){ return new ArrayList<>(ingredients);}

    // Setter + validazione sintattica

    public void setId(String id){
        if(id == null || id.isBlank()){
            this.id = null;
            return;
        }
        this.id = id.trim();
    }

    public void setName(String name){
        if(!isNotBlank(name)){
            throw new IllegalArgumentException("Il nome del piatto non deve essere vuoto.");
        }
        this.name = name.trim();
    }

    public void setDescription(String description){
        this.description = (description != null && !description.isBlank()) ? description.trim() : null;
    }

    public void setCourseType(CourseType courseType){
        if(courseType == null){
            throw new IllegalArgumentException("Tipologia portata non valida.");
        }
        this.courseType = courseType;
    }

    public void setDietCategories(Set<DietCategory> dietCategories){
        if(dietCategories == null || dietCategories.isEmpty()){
            throw new IllegalArgumentException("Deve esserci almeno una categoria dietetica");
        }
        this.dietCategories = EnumSet.copyOf(dietCategories);
    }

    public void setPrice(BigDecimal price){
        if(!isValidPrice(price)){
            throw new IllegalArgumentException("Il prezzo deve essere > 0");
        }
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public void setImageUri(String imageUri){
        if(imageUri != null && !imageUri.isBlank()){
            this.imageUri = imageUri.trim();
        } else {
            this.imageUri = null;
        }
    }

    public void setKcal(Double kcal){
        if(kcal == null){
            throw new IllegalArgumentException("Kcal del piatto non valide.");
        }
        this.kcal = kcal;
    }

    public void setState(DishState state){
        if(state == null){
            throw new IllegalArgumentException("Lo stato del piatto non è valido.");
        }
        this.state = state;
    }

    public void setIngredients(List<IngredientPortionBean> ingredients){
        if(ingredients == null){
            this.ingredients = new ArrayList<>();
            return;
        }
        for(IngredientPortionBean i : ingredients){
            if(i == null){
                throw new IllegalArgumentException("La lista contiene ingredienti nulli!");
            }
        }
        this.ingredients = new ArrayList<>(ingredients);
    }

    public void addIngredient(IngredientPortionBean ingredient){
        Objects.requireNonNull(ingredient, "L'ingrediente non può essere nullo.");
        this.ingredients.add(ingredient);
    }

    // Metodi privati per la validazione sintattica

    private boolean isNotBlank(String value){
        return value != null && !value.isBlank();
    }

    private boolean isValidPrice(BigDecimal price){
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }
}
