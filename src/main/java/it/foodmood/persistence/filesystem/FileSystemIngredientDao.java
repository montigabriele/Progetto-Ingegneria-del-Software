package it.foodmood.persistence.filesystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import it.foodmood.domain.model.Ingredient;
import it.foodmood.domain.value.Allergen;
import it.foodmood.domain.value.Unit;
import it.foodmood.exception.PersistenceException;
import it.foodmood.domain.value.Macronutrients;
import it.foodmood.persistence.dao.IngredientDao;

public final class FileSystemIngredientDao extends AbstractCsvDao implements IngredientDao {

    private static final String SEPARATOR = ";";
    private static final String ALLERGEN_SEPARATOR = ",";

    public FileSystemIngredientDao() {
        super(FileSystemPaths.INGREDIENTS);
    }
    
    @Override
    public void insert(Ingredient ingredient){
        String line = toCsv(ingredient);
        appendLine(line);
    }

    @Override
    public Optional<Ingredient> findById(String name){
        if(name == null || name.isBlank()){
            return Optional.empty();
        }
        return findAll().stream().filter(ingredient -> ingredient.getName().equals(name)).findFirst();
    }

    @Override
    public List<Ingredient> findAll(){
        List<String> lines = readAllLines();
        List<Ingredient> ingredients = new ArrayList<>();
        for(String line: lines){
            ingredients.add(fromCsv(line));
        }
        return ingredients;
    }

    @Override
    public void deleteById(String name){
        if(name == null || name.isBlank()){
            return;
        }
        List<Ingredient> all = findAll();
        boolean removed = all.removeIf(ingredient -> ingredient.getName().equals(name));
        if(!removed){
            return;
        }

        List<String> lines = new ArrayList<>();
        for(Ingredient ingredient : all){
            lines.add(toCsv(ingredient));
        }
        overwriteAllLines(lines);
    }

    private String allergensToString(Ingredient ingredient){
        StringBuilder stringBuilder = new StringBuilder();
        for(Allergen allergen: ingredient.getAllergens()){
            if(!stringBuilder.isEmpty()){
                stringBuilder.append(ALLERGEN_SEPARATOR);
            }
            stringBuilder.append(allergen.name());
        }
        return stringBuilder.toString();
    }

    private Set<Allergen> parseAllergens(String field, String line){
        Set<Allergen> allergens = new HashSet<>();

        if(field.isBlank()){
            return allergens;
        }

        for(String token: field.split(ALLERGEN_SEPARATOR)){
            try {
                allergens.add(Allergen.valueOf(token.trim()));
            } catch (Exception _) {
                throw new PersistenceException("Allergene: " + token + " non valido\nRiga: "+ line);
            }
        }
        return allergens;
    }

    private String toCsv(Ingredient ingredient){

        Macronutrients macronutrients = ingredient.getMacro();

        String allergens = allergensToString(ingredient);

        String unit = ingredient.getUnit().name();

        return ingredient.getName() + SEPARATOR + macronutrients.protein() + SEPARATOR + macronutrients.carbohydrates() + SEPARATOR + 
               macronutrients.fat() + SEPARATOR + unit + SEPARATOR + allergens;
    }

    private Ingredient fromCsv(String line){
        String[] token = line.split(SEPARATOR, -1);
        if(token.length != 6){
            throw new PersistenceException("Riga ingrediente malformata: " + line);
        }

        String name = token[0];
        double protein;
        double carbs;
        double fat;

        try {
            protein = Double.parseDouble(token[1]);
            carbs = Double.parseDouble(token[2]);
            fat = Double.parseDouble(token[3]);
        } catch (NumberFormatException _) {
            throw new PersistenceException("Valori nutrizionali non validi: " + line);
        }

        Macronutrients macronutrients = new Macronutrients(protein, carbs, fat);

        String unitStr = token[4];
        Unit unit;
        try {
            unit = Unit.valueOf(unitStr); 
        } catch (Exception _) {
            throw new PersistenceException("Unità di misura non valida: " + unitStr);
        }

        Set<Allergen> allergens = parseAllergens(token[5], line);

        return new Ingredient(name, macronutrients, unit, allergens);
    }
}
