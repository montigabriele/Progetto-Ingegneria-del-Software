package it.foodmood.view.ui.cli.manager;


import java.util.List;

import it.foodmood.bean.IngredientBean;
import it.foodmood.bean.MacronutrientsBean;
import it.foodmood.controller.IngredientController;
import it.foodmood.domain.value.Unit;
import it.foodmood.exception.BackRequestedException;
import it.foodmood.exception.IngredientException;
import it.foodmood.view.ui.cli.TableConsoleView;

public class CliIngredientMenuView extends TableConsoleView {

    private final IngredientController ingredientController = new IngredientController();
    private static final String TRY_AGAIN = "Premi INVIO per riprovare";

    public void displayPage(){
        boolean back = false;

        while(!back){

            clearScreen();
            showTitle("Gestisci Ingredienti");
            showInfo("0. Indietro");
            showInfo("1. Inserisci ingrediente");
            showInfo("2. Visualizza ingredienti");
            showInfo("3. Modifica ingrediente");
            showInfo("4. Elimina ingrediente");

            String choice = askInput("\nSeleziona un'opzione: ");

            switch(choice){
                case "0" -> back = true;
                case "1" -> createIngredient();
                case "2" -> readAllIngredient();
                case "3" -> updateIngredient();
                case "4" -> deleteIngredient();
                default -> showError("Scelta non valida, riprova.");
            }
        }
    }

    private void deleteIngredient() {
        boolean choice = true;
        while (choice) {
            try {
                choice = deletionIteration();
            } catch (BackRequestedException _) {
                showInfo("Operazione annullata. Ritorno al menù ingredienti");
                choice = false;
                waitForEnter(null);
            } catch (IngredientException e){
                showError(e.getMessage());
                waitForEnter(TRY_AGAIN);
            }
        }
    }

    private boolean deletionIteration() throws BackRequestedException, IngredientException {
        List<IngredientBean> ingredients = ingredientController.getAllIngredients();
        clearScreen();
        showTitle("Elimina Ingrediente");
        tableIngredients();
            
        String input = askInputOrBack("Inserisci il numero dell'ingrediente da eliminare");
        Integer index = parseInteger(input, ingredients.size());
        if(index == null || index == 0){
            return false;
        }

        IngredientBean selected = ingredients.get(index - 1);
        String ingredientName = selected.getName().toUpperCase();
        ingredientController.deleteIngredient(ingredientName);
            
        showSuccess("Ingrediente '" + ingredientName + "' eliminato con successo.");
        boolean choice = askConfirmation("Vuoi eliminare un'altro ingrediente?");
        if(!choice){
            waitForEnter("Premi INVIO per continuare");
        }
        return choice;
    }

    private void updateIngredient() {
        showInfo("Funzionalità non ancora implementata");
        waitForEnter(null);
    }

    private void readAllIngredient() {
        clearScreen();
        showTitle("Lista Ingredienti");
        tableIngredients();
        waitForEnter(null);
    }

    private void createIngredient() {
        try {
            clearScreen();
            while(true){
                showTitle("Inserisci ingrediente");
                showBold("Compila i campi");

                IngredientBean ingredientBean = new IngredientBean();
                MacronutrientsBean macronutrientsBean = new MacronutrientsBean();

                String name = askInputOrBack("Nome");
                ingredientBean.setName(name);

                Unit unit = askUnit();
                ingredientBean.setUnit(unit);

                boolean created = requireMacronutrients(ingredientBean, macronutrientsBean);
                if(created){
                    return;
                }
            }
        } catch (BackRequestedException _) {
            showInfo("Operazione annullata. Ritorno al menù ingredienti");
            waitForEnter(null);
        }
    }

    private Unit askUnit() {
        while(true){
            showBold("\nUnità di misura");
            showInfo("1. Grammi (g)\n2. Milliletri (ml)");

            String choice = askInput("\nSeleziona un'opzione: ");

            switch (choice){
                case "1" : 
                    return Unit.GRAM;
                case "2" : 
                    return Unit.MILLILITER;
                default: 
                    showError("Scelta non valida, riprova.");
                    continue;
            }
        }
    }

    private boolean requireMacronutrients(IngredientBean ingredientBean, MacronutrientsBean macronutrientsBean){
        try {
            showBold("\nInserisci i macronutrienti riferiti a 100 g/ml di prodotto");
            showInfo("Inserisci almeno un macronutriente, (0 != Indietro)");
            double protein = askDouble("Proteine: ");
            double carbohydrates = askDouble("Carboidrati: ");
            double fat = askDouble("Grassi: ");

            macronutrientsBean.setProtein(protein);
            macronutrientsBean.setCarbohydrates(carbohydrates);
            macronutrientsBean.setFat(fat);

            ingredientBean.setMacronutrients(macronutrientsBean);

            ingredientController.createIngredient(ingredientBean);

            showSuccess("Ingrediente inseristo correttamente.");
            waitForEnter(null);
            return true;
        } catch (IngredientException e) {
            showError(e.getMessage());
            waitForEnter(TRY_AGAIN);       
            return false;         
        }
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

}
