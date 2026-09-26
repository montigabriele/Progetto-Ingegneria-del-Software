package it.foodmood.view.ui.cli.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import it.foodmood.bean.DishBean;
import it.foodmood.bean.IngredientBean;
import it.foodmood.bean.IngredientPortionBean;
import it.foodmood.controller.DishController;
import it.foodmood.controller.IngredientController;
import it.foodmood.domain.value.CourseType;
import it.foodmood.domain.value.DietCategory;
import it.foodmood.domain.value.DishState;
import it.foodmood.domain.value.Unit;
import it.foodmood.exception.BackRequestedException;
import it.foodmood.exception.DishException;
import it.foodmood.exception.IngredientException;
import it.foodmood.utils.UnitUtils;
import it.foodmood.view.ui.cli.TableConsoleView;

public class CliDishMenuView extends TableConsoleView {
    private final DishController dishController = new DishController();
    private final IngredientController ingredientController = new IngredientController();
    
    private static final String INVALID_CHOICE = "Scelta non valida, riprova";
    private static final String BACK = "Premi INVIO per tornare indietro";

    public CliDishMenuView(){
        // vuoto
    }

    public void displayPage(){
        boolean back = false;

        while(!back){

            clearScreen();
            showTitle("Gestisci Piatti");
            showInfo("0. Indietro");
            showInfo("1. Inserisci piatto");
            showInfo("2. Visualizza piatti");
            showInfo("3. Modifica piatto");
            showInfo("4. Elimina piatto");

            String choice = askInput("\nSeleziona un'opzione: ");

            switch(choice){
                case "0" -> back = true;
                case "1" -> createDish();
                case "2" -> readAllDishes();
                case "3" -> updateDish();
                case "4" -> deleteDish();
                default -> showError(INVALID_CHOICE);
            }
        }
    }

    private void createDish() {
        try {
            clearScreen();
            showTitle("Inserisci Piatto");
            showBold("Compila i campi");

            DishBean dishBean = new DishBean();

            String name = askInputOrBack("Nome");
            dishBean.setName(name);

            String description = askInputOrNull("Descrizione piatto (opzionale): ");
            dishBean.setDescription(description);

            CourseType courseType = askCourseType();
            dishBean.setCourseType(courseType);

            dishBean.setDietCategories(askDietCategories());

            BigDecimal price = askBigDecimal("Prezzo: ");
            dishBean.setPrice(price);

            dishBean.setImageUri(null);

            DishState state = askDishState();
            dishBean.setState(state);

            requireIngredients(dishBean);

            dishController.createDish(dishBean);

            showSuccess("Piatto inserito correttamente.");
            waitForEnter(null);
        } catch (DishException e) {
            showError(e.getMessage());
            waitForEnter(BACK);
        } catch (IllegalArgumentException e){
            showError(e.getMessage());
        }
    }


    private void requireIngredients(DishBean dishBean) {
        boolean done = false;
        while(!done){
            showIngredients(dishBean);
            done = ingredientMenuChoice(dishBean);
        }
    }

    private void showIngredients(DishBean dishBean){
        clearScreen();
        showTitle("Ingrediente del piatto: " + dishBean.getName());

        List<IngredientPortionBean> selected = dishBean.getIngredients();
        if(selected == null || selected.isEmpty()){
            showWarning("Nessun ingrediente aggiunto");
        } else {
            showBold("Ingredienti presenti: ");
            showIngredientList(selected);
        }

        showInfo("\n0. Conferma");
        showInfo("1. Aggiungi ingrediente");
        showInfo("2. Rimuovi ultimo ingrediente");
    }

    private void showIngredientList(List<IngredientPortionBean> selected){
        for(int i = 0; i < selected.size(); i++){
            IngredientPortionBean ingredient = selected.get(i);
            String unitLabel = ingredient.getUnit().equals("GRAM") ? "g" : "ml";
            showInfo((i + 1) + ". " + ingredient.getIngredient().getName() + " - " + String.format("%.2f %s", ingredient.getQuantity(), unitLabel));
        }
    }

    private boolean ingredientMenuChoice(DishBean dishBean){
        String choice = askInput("Seleziona un'opzione: ");

        switch (choice) {
            case "0" :
                return confirmChoice(dishBean);
            case "1" : 
                addIngredientToDish(dishBean);
                return false;
            case "2": 
                removeLastIngredient(dishBean);
                return false;
            default:
                showError(INVALID_CHOICE);
                waitForEnter(null);
                return false;
        }
    }

    private boolean confirmChoice(DishBean dishBean){
        if(dishBean.getIngredients().isEmpty()){
            showWarning("Il piatto deve avere almeno un ingrediente");
            waitForEnter(null);
            return false;
        } 
        return true;
    }

    private void removeLastIngredient(DishBean dishBean){
        List<IngredientPortionBean> list = new ArrayList<>(dishBean.getIngredients());
        if(!list.isEmpty()){
            list.remove(list.size() - 1);
            dishBean.setIngredients(list);
        } else {
            showWarning("Nessun ingrediente da rimuovere");
            waitForEnter(null);
        }  
    }

    private void addIngredientToDish(DishBean dishBean){
        final List<IngredientBean> ingredients;
        int index;

        clearScreen();
        showTitle("Seleziona un ingrediente da aggiungere");

        tableIngredients();
        showInfo("0. Annulla");

        String input = askInput("\nInserisci il numero dell'ingrediente da aggiungere: ");

        try {
            ingredients = ingredientController.getAllIngredients();
            index = Integer.parseInt(input);
        } catch (IngredientException e) {
            showError(e.getMessage());
            waitForEnter(null);
            return;
        } catch (NumberFormatException _) {
            showError("Inserisci un numero valido.");
            waitForEnter(input);
            return;
        }

        if(index == 0) return;

        if(index < 1 || index > ingredients.size()){
            showError("Indice non valido.");
            waitForEnter(null);
            return;
        }

        IngredientBean selected = ingredients.get(index - 1);

        Unit unit = selected.getUnit();

        String unitLabel = UnitUtils.toLabel(unit);

        double quantity = askDouble("Quantità in " + unitLabel + ": ");

        IngredientPortionBean portionBean = new IngredientPortionBean();
        portionBean.setIngredient(selected);
        portionBean.setQuantity(quantity);
        portionBean.setUnit(unit.name());

        dishBean.addIngredient(portionBean);

        showSuccess("Ingrediente aggiunto correttamente.");
        waitForEnter(null);
    }

    private void tableIngredients(){
        try {

            List<IngredientBean> ingredients = ingredientController.getAllIngredients();
            if(ingredients == null || ingredients.isEmpty()){
                showWarning("Nessun ingrediente disponibile. Aggiungi prima degli ingredienti.");
                waitForEnter(null);
                return;
            }

            showIngredientTable(ingredients);
        } catch (IngredientException e) {
            showError(e.getMessage());
            waitForEnter(null);
        }
    }

    private DishState askDishState() {
        while(true){
            showBold("Stato del piatto: ");
            showInfo("1. Disponibile");
            showInfo("2. Non disponibile");

            String choice = askInput("\nSeleziona un'opzione: ");

            switch (choice){
                case "1" : 
                    return DishState.AVAILABLE;
                case "2" : 
                    return DishState.UNAVAILABLE;
                default: 
                    showError(INVALID_CHOICE);
                    continue;
            }
        }
    }

    private CourseType askCourseType(){
        while (true) {
            showBold("Tipologia portata: ");
            CourseType[] values = CourseType.values();
            for(int i = 0; i < values.length; i++){
                showInfo((i + 1) + ". " + values[i].description());
            }

            String choice = askInput("Seleziona una tipologia: ");

            try {
                int index = Integer.parseInt(choice) - 1;
                if(index >= 0 && index < values.length){
                    return values[index];
                }
            } catch (NumberFormatException _) {
                showError(INVALID_CHOICE);
            }
        }
    }

    private DietCategory askDietCategory(){
        while (true) {
            showBold("Categoria dietetica: ");
            DietCategory[] values = DietCategory.values();
            for(int i = 0; i < values.length; i++){
                showInfo((i + 1) + ". " + values[i].description());
            }

            String choice = askInput("Seleziona una categoria: ");

            try {
                int index = Integer.parseInt(choice) - 1;
                if(index >= 0 && index < values.length){
                    return values[index];
                }
            } catch (NumberFormatException _) {
                showError(INVALID_CHOICE);
            }
        }
    }

    private Set<DietCategory> askDietCategories(){
        EnumSet<DietCategory> categories = EnumSet.noneOf(DietCategory.class);
        while (true) {
            DietCategory selected = askDietCategory();
            categories.add(selected);

            boolean again = askConfirmation("Vuoi aggiungere un'altra categoria?");
            if(!again){
                if(categories.isEmpty()){
                    showWarning("Devi selezionare almeno una categoria");
                    continue;
                }
                return categories;
            }
        }
    }

    private void readAllDishes() {
        clearScreen();
        showTitle("Lista Piatti");
        tableDishes();
        waitForEnter(null);
    }

    private void updateDish() {
        showInfo("Funzionalità non ancora implementata");
        waitForEnter(null);
    }

    private void deleteDish() {
        boolean choice = true;
        while (choice) {
            try {
                choice = deleteSingleDish();
            } catch (BackRequestedException _) {
                showInfo("Operazione annullata. Ritorno al menù ingredienti");
                choice = false;
                waitForEnter(null);
            } catch (DishException e){
                showError(e.getMessage());
                waitForEnter(BACK);
            }
        }
    }

    private boolean deleteSingleDish() throws BackRequestedException, DishException{
        List<DishBean> dishes = dishController.getAllDishes();
        clearScreen();
        showTitle("Elimina Piatto");

        if(dishes == null || dishes.isEmpty()){
            showWarning("Nessun piatto presente");
            waitForEnter(BACK);
            return false;
        }

        tableDishes();

        String input = askInputOrBack("Inserisci il numero del piatto da eliminare");

        Integer index = parseInteger(input, dishes.size());

        if(index == null || index == 0){
            return false;
        }

        DishBean selected = dishes.get(index - 1);
        String dishId = selected.getId();
        String dishName = selected.getName();
        dishController.deleteDish(dishId);
            
        showSuccess("Piatto '" + dishName + "' eliminato con successo.");

        List<DishBean> anotherElimination = dishController.getAllDishes();

        boolean again = anotherElimination != null && !anotherElimination.isEmpty() && askConfirmation("Vuoi eliminare un'altro piatto?");

        if(!again){
            waitForEnter("Premi INVIO per continuare");
        }
        
        return again;
    }

    private void tableDishes(){
        try {
            List<DishBean> dishes = dishController.getAllDishes();

            if(dishes == null || dishes.isEmpty()){
                showWarning("Nessun piatto presente.");
                return;
            }

            List<String> headers = List.of("N°", "Nome", "Prezzo", "Stato", "Tipologia");

            List<List<String>> rows = IntStream.range(0, dishes.size()).mapToObj( i -> {
                DishBean dish = dishes.get(i);

                String index = String.valueOf(i + 1);
                String name = dish.getName();
                String price = String.format("%.2f", dish.getPrice());
                String state = dish.getState().description();
                String courseType = dish.getCourseType().description();

                return List.of(index, name, price, state, courseType);
            }).toList();

            List<Integer> columnWidths = List.of(2,25,7,15,15);

            displayTable(headers, rows, columnWidths);

        } catch (DishException e) {
            showWarning(e.getMessage());
        }
    }
}
