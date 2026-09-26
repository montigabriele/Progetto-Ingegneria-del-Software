
package it.foodmood.view.ui.gui.manager;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import it.foodmood.bean.DishBean;
import it.foodmood.bean.IngredientBean;
import it.foodmood.bean.IngredientPortionBean;
import it.foodmood.bean.MacronutrientsBean;
import it.foodmood.controller.DishController;
import it.foodmood.controller.IngredientController;
import it.foodmood.domain.value.CourseType;
import it.foodmood.domain.value.DietCategory;
import it.foodmood.domain.value.DishState;
import it.foodmood.domain.value.Unit;
import it.foodmood.exception.DishException;
import it.foodmood.utils.UnitUtils;
import it.foodmood.view.ui.gui.utils.BaseGui;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class GuiManagmentDish extends BaseGui {

    @FXML private Button btnAddToTheDish;

    @FXML private Button btnCancel;

    @FXML private Button btnChangeQuantity;

    @FXML private Button btnDeleteDish;

    @FXML private Button btnImport;

    @FXML private Button btnNewDish;

    @FXML private Button btnRemoveImg;

    @FXML private Button btnRemoveIngredient;

    @FXML private Button btnSave;

    @FXML private Button btnUpdateDish;

    @FXML private MenuButton mbDietCategories;

    @FXML private CheckMenuItem miTraditional;

    @FXML private CheckMenuItem miVegetarian;

    @FXML private CheckMenuItem miVegan;

    @FXML private CheckMenuItem miGlutenFree;

    @FXML private CheckMenuItem miLactoseFree;

    @FXML private ComboBox<CourseType> cbProductType;

    @FXML private ComboBox<DishState> cbState;

    @FXML private ComboBox<DishState> cbStateForm;

    @FXML private ComboBox<CourseType> cbType;

    @FXML private TableColumn<IngredientBean, String> colAllergensForm;

    @FXML private TableColumn<IngredientBean, String> colKcalForm;

    @FXML private TableColumn<IngredientBean, String> colMacrosForm;

    @FXML private TableColumn<IngredientBean, String> colNameForm;

    @FXML private TableColumn<DishBean, String> colName;

    @FXML private TableColumn<DishBean, String> colPrice;

    @FXML private TableColumn<DishBean, String> colState;

    @FXML private TableColumn<DishBean, String> colType;

    @FXML private TableColumn<IngredientBean, String> colUnitForm;

    @FXML private ImageView ivImage;

    @FXML private Label lblAllergens;

    @FXML private Label lblTotalCarbs;

    @FXML private Label lblTotalFats;

    @FXML private Label lblTotalKcal;

    @FXML private Label lblTotalProtein;

    @FXML private ListView<IngredientPortionBean> listDishIngredients;

    @FXML private AnchorPane paneForm;

    @FXML private AnchorPane paneList;

    @FXML private TextArea taDishDescriptionForm;

    @FXML private TableView<DishBean> tableDishes;

    @FXML private TableView<IngredientBean> tableIngredientsForm;

    @FXML private TextField tfAdvancedResarch;

    @FXML private TextField tfDishName;

    @FXML private TextField tfDishNameForm;

    @FXML private TextField tfPrice;

    @FXML private TextField tfPriceForm;

    @FXML private TextField tfResarchDish;

    private IngredientController ingredientController;
    private DishController dishController;

    private final ObservableList<DishBean> allDishes = FXCollections.observableArrayList();
    private final ObservableList<IngredientBean> ingredientItems = FXCollections.observableArrayList();
    private final ObservableList<IngredientPortionBean> dishIngredients = FXCollections.observableArrayList();
    private FilteredList<IngredientBean> filteredIngredientForm;

    private String currentImageUri;

    public void setIngredientController(IngredientController ingredientController){
        this.ingredientController = ingredientController;
        loadIngredients();
    }

    public void setDishController(DishController dishController){
        this.dishController = dishController;
        loadDishes();
    }

    @FXML
    void btnChangeQuantity(ActionEvent event) {
        IngredientPortionBean selected = listDishIngredients.getSelectionModel().getSelectedItem();

        if(selected == null){
            showError("Seleziona un ingrediente del piatto dalla lista per modificare la quantità");
            return;
        }

        Double newQuantity = askQuantity(selected.getIngredient(), selected.getQuantity());

        if(newQuantity == null) return;

        try {
            selected.setQuantity(newQuantity);
            listDishIngredients.refresh();
            updateDishSummary();
            showInfo("Quantità modificata correttamente");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void initTableDish(){
        setupColumn(colName, d -> d.getName());
        setupColumn(colPrice, d -> d.getPrice() != null ? d.getPrice().toString() : null);
        setupColumn(colState, d -> d.getState() != null ? d.getState().description() : null);
        setupColumn(colType, d -> d.getCourseType() != null ? d.getCourseType().description() : null);
    }

    private Double askQuantity(IngredientBean ingredient, Double defaultValue) {
        Unit unit = ingredient.getUnit();
        String unitLabel = UnitUtils.toLabel(unit);
        
        TextInputDialog dialog = new TextInputDialog(
            defaultValue != null ? String.format("%.2f", defaultValue) : ""
        );

        dialog.setTitle("Quantità Ingrediente");
        dialog.setHeaderText("Specifica la quantità per: " + ingredient.getName());
        dialog.setContentText("Quantità in " + unitLabel + " :");

        Optional<String> result = dialog.showAndWait();
        if(result.isEmpty()){
            return null;
        }

        try {
            double quantity = Double.parseDouble(result.get().replace(",", "."));
            if(quantity <= 0){
                showError("La quantità deve essere maggiore di 0");
                return null;
            }
            return quantity;
        } catch (NumberFormatException e) {
            showError(e.getMessage());
            return null;
        }
    }

    private EnumSet<DietCategory> selectedDietCategories(){
        EnumSet<DietCategory> set = EnumSet.noneOf(DietCategory.class);

        if(miTraditional.isSelected()) set.add(DietCategory.TRADITIONAL);
        if(miVegetarian.isSelected()) set.add(DietCategory.VEGETARIAN);
        if(miVegan.isSelected()) set.add(DietCategory.VEGAN);
        if(miGlutenFree.isSelected()) set.add(DietCategory.GLUTEN_FREE);
        if(miLactoseFree.isSelected()) set.add(DietCategory.LACTOSE_FREE);

        return set;
    }

    @FXML
    void onAddIngredient(ActionEvent event) {
        ObservableList<IngredientBean> selectedIngredients = tableIngredientsForm.getSelectionModel().getSelectedItems();

        if(selectedIngredients == null || selectedIngredients.isEmpty()){
            showInfo("Seleziona almeno un ingrediente dalla tabella");
            return;
        }

        for(IngredientBean ingredient: selectedIngredients){
            boolean alredyAdded = dishIngredients.stream().anyMatch(i -> 
                i.getIngredient().getName().equals(ingredient.getName()) &&
                i.getUnit().equals(ingredient.getUnit().name()));

            if(alredyAdded){
                showError("L'ingrediente " + ingredient.getName() + " è già presente nel piatto");
            } else {
                Double quantity = askQuantity(ingredient, 0.0);
                if(quantity == null){
                    continue;
                }

                try {
                    IngredientPortionBean portionBean = new IngredientPortionBean();
                    portionBean.setIngredient(ingredient);
                    portionBean.setQuantity(quantity);
                    portionBean.setUnit(ingredient.getUnit().name());

                    dishIngredients.add(portionBean);
                } catch (IllegalArgumentException e) {
                    showError(e.getMessage());
                }
            }
        }

        listDishIngredients.refresh();
        updateDishSummary();

        tableIngredientsForm.getSelectionModel().clearSelection();
    }

    private void updateDishSummary() {
        updateNutritionalSummary();
        updateAllergenSummary();
    }

    private void updateNutritionalSummary() {
        double totalProtein = 0.0;
        double totalCarbhoydrates = 0.0;
        double totalFat = 0.0;

        for(IngredientPortionBean portion: dishIngredients){
            if(portion != null && portion.getIngredient() != null){
                MacronutrientsBean macronutrientsBean = portion.getIngredient().getMacronutrients();

                if(macronutrientsBean != null){
                    double factor = factorPortion(portion);

                    totalProtein += getNutrientValue(macronutrientsBean.getProtein()) * factor;
                    totalCarbhoydrates += getNutrientValue(macronutrientsBean.getCarbohydrates()) * factor;
                    totalFat += getNutrientValue(macronutrientsBean.getFat()) * factor;
                }
            }
        }
        updateLabels(totalProtein, totalCarbhoydrates, totalFat);
    }

    private void updateLabels(double totalProtein, double totalCarbhoydrates, double totalFat){
        MacronutrientsBean totalMacro = new MacronutrientsBean();
        totalMacro.setProtein(totalProtein);
        totalMacro.setCarbohydrates(totalCarbhoydrates);
        totalMacro.setFat(totalFat);

        double totalKcal = totalMacro.calculateKcal();

        lblTotalProtein.setText(String.format(Locale.ROOT, "%.1f", totalProtein));
        lblTotalCarbs.setText(String.format(Locale.ROOT, "%.1f", totalCarbhoydrates));
        lblTotalFats.setText(String.format(Locale.ROOT, "%.1f", totalFat));
        lblTotalKcal.setText(String.format(Locale.ROOT, "%.1f", totalKcal));
    }

    private double factorPortion(IngredientPortionBean portion){
        Double quantityPortion = portion.getQuantity();
        double quantity = (quantityPortion == null) ? 0.0 : quantityPortion;  
        return quantity / 100.0;  
    }

    private double getNutrientValue(Double nutrient){
        return (nutrient == null) ? 0.0 : nutrient;
    }

    private void updateAllergenSummary() {
        Set<String> allergens = new LinkedHashSet<>();

        for(IngredientPortionBean portion : dishIngredients){
            if(portion == null || portion.getIngredient() == null) continue;

            List<String> allergen = portion.getIngredient().getAllergens();
            if(allergen != null) allergens.addAll(allergen);
        }

        if(allergens.isEmpty()){
            lblAllergens.setText("Nessun allergene");
        } else {
            lblAllergens.setText(String.join(", ", allergens));
        }
    }

    private void initSearchIngredientForm(){
        filteredIngredientForm = new FilteredList<>(ingredientItems, ingredient -> true);

        SortedList<IngredientBean> sortedList = new SortedList<>(filteredIngredientForm);
        sortedList.comparatorProperty().bind(tableIngredientsForm.comparatorProperty());

        tableIngredientsForm.setItems(sortedList);

        //listener
        tfAdvancedResarch.textProperty().addListener((obs, oldValue, newValue) -> {
            String filter = (newValue == null) ? "" : newValue.trim().toLowerCase(Locale.ROOT);

            if(filter.isEmpty()){
                filteredIngredientForm.setPredicate(ingredient -> true); // mostro tutti
            } else {
                filteredIngredientForm.setPredicate(ingredient -> {
                    String name = ingredient.getName();
                    return name != null && name.toLowerCase(Locale.ROOT).contains(filter);
                });
            }
        });
    }

    @FXML
    void onAddNewDish(ActionEvent event) {
        clearForm();
        showFormView();
    }

    @FXML
    void onCancel(ActionEvent event) {
        clearForm();
        showListView();
    }

    @FXML
    void onImportImage(ActionEvent event) {
        Window window = ivImage.getScene() != null ? ivImage.getScene().getWindow() : null;
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Seleziona l'immagine");

        File selectedFile = fileChooser.showOpenDialog(window);

        if(selectedFile != null){
            try {
                String uri = selectedFile.toURI().toString();
                currentImageUri = uri;

                Image fxImage = new Image(uri, true); 
                ivImage.setImage(fxImage);
                
            } catch (Exception e) {
                showError("Errore durante il caricamento dell'immagine: " + e.getMessage());
                currentImageUri = null;
                ivImage.setImage(null);
            }
        }

    }

    @FXML
    void onDeleteDish(ActionEvent event) {
        DishBean selected = tableDishes.getSelectionModel().getSelectedItem();

        if(selected == null){
            showError("Seleziona un piatto da eliminare");
            return;
        }

        if(!showConfirmation("Conferma eliminazione", "Vuoi eliminare il piatto:\n" + selected.getName() + " ?")) {
            return;
        }

        try {
            dishController.deleteDish(selected.getId());

            allDishes.remove(selected);
        } catch (Exception e) {
            showError("Errore durante l'eliminazione: " + e.getMessage());
        }
    }

    @FXML
    void onRemoveImage(ActionEvent event) {
        currentImageUri = null;
        ivImage.setImage(null);
    }

    @FXML
    void onRemoveIngredient(ActionEvent event) {
        IngredientPortionBean selected = listDishIngredients.getSelectionModel().getSelectedItem();

        if(selected == null){
            showError("Seleziona un ingrediente dal piatto da rimuovere");
            return;
        }

        dishIngredients.remove(selected);
        listDishIngredients.refresh();
        updateDishSummary();
    }

    @FXML
    void onSaveDish(ActionEvent event) {

        DishBean dishBean = new DishBean();

        try {
            String name = tfDishNameForm.getText();
            dishBean.setName(name);

            // null
            String description = taDishDescriptionForm.getText();
            dishBean.setDescription(description);

            CourseType courseType = cbProductType.getValue(); 
            dishBean.setCourseType(courseType);

            EnumSet<DietCategory> categories = selectedDietCategories();
            dishBean.setDietCategories(categories);

            DishState dishState = cbStateForm.getValue();
            dishBean.setState(dishState);

            String priceText = tfPriceForm.getText();
            BigDecimal price;
            priceText = priceText.replace(",", ".");
            price = new BigDecimal(priceText);
            dishBean.setPrice(price); 

            dishBean.setImageUri(currentImageUri);

            List<IngredientPortionBean> ingredientsList = new ArrayList<>(dishIngredients);
            dishBean.setIngredients(ingredientsList);

            dishController.createDish(dishBean);
            loadDishes();
            showInfo("Piatto creato correttamente");
            showListView();

        } catch (IllegalArgumentException | DishException e) {
            showError(e.getMessage());
        } 
    }

    @FXML
    void onUpdateDish(ActionEvent event) {
        showInfo("Funzionalità non ancora implementata");
    }

    @FXML
    private void initialize(){
        initTableDish();
        initComboBox();
        initTable();
        initDishIngredientsList();
        initSearchIngredientForm();
        showListView();
    }

    private void loadDishes(){
        try {
            List<DishBean> dishes = dishController.getAllDishes();
            allDishes.setAll(dishes);
            tableDishes.setItems(allDishes);
        } catch (Exception e) {
            showError("Errore durante il caricamento dei prodotti: " + e.getMessage());
        }
    }

    protected void initComboBox(){
        setupComboBox(cbProductType, CourseType.values(), CourseType::description);
        setupComboBox(cbType, CourseType.values(), CourseType::description);
        setupComboBox(cbStateForm, DishState.values(), DishState::description);
        setupComboBox(cbState, DishState.values(), DishState::description);
    }

    private void showListView(){
        swap(paneList, paneForm);
    }

    private void showFormView(){
        swap(paneForm, paneList);
    }

    private void initTable(){
        setupColumn(colNameForm, ingredient -> ingredient.getName());

        setupColumn(colUnitForm, ingredient -> { 
            Unit unit = ingredient.getUnit();
            return UnitUtils.toLabel(unit);
        });

        setupColumn(colMacrosForm, ingredient -> {
            MacronutrientsBean macro = ingredient.getMacronutrients();
            if(macro == null){
                return ("-");
            }
            double protein = macro.getProtein();
            double carbo = macro.getCarbohydrates();
            double fat = macro.getFat();
            return String.format("%.1f/ %.1f/ %.1f", carbo, protein, fat);
        });

        setupColumn(colKcalForm, ingredient -> {
            MacronutrientsBean macro = ingredient.getMacronutrients();
            if(macro == null){
                return ("-");
            }
            double kcal = macro.calculateKcal();
            return String.format("%.1f", kcal);
        });

        setupColumn(colAllergensForm, ingredient -> {
            List<String> allergens = ingredient.getAllergens();
            if(allergens == null || allergens.isEmpty()){
                return "ALLERGENI NON PRESENTI";
            }
            return String.join(", ", allergens);
        });

        tableIngredientsForm.setItems(ingredientItems);
    }

    private void initDishIngredientsList(){
        listDishIngredients.setItems(dishIngredients);

        setupListCell(listDishIngredients, item ->{
            String unitLabel = "GRAM".equals(item.getUnit()) ? "g" : "ml";
            return item.getIngredient().getName() + " - " +
                   String.format("%.2f %s", item.getQuantity(), unitLabel);
        });
    }

    private void loadIngredients(){
        ingredientItems.clear();

        try {
            List<IngredientBean> listIngredient = ingredientController.getAllIngredients();

            ingredientItems.addAll(listIngredient);
        } catch (Exception e) {
            showError("Errore durante il caricamento degli ingredienti: " + e.getMessage());
        }
    }

    private void clearForm(){
        tfDishNameForm.clear();
        tfPriceForm.clear();
        taDishDescriptionForm.clear();

        cbProductType.getSelectionModel().clearSelection();
        cbStateForm.getSelectionModel().clearSelection();
        listDishIngredients.getSelectionModel().clearSelection();
        tableIngredientsForm.getSelectionModel().clearSelection();

        currentImageUri = null;
        ivImage.setImage(null);

        dishIngredients.clear();

        lblTotalCarbs.setText("0");
        lblTotalFats.setText("0");
        lblTotalProtein.setText("0");
        lblTotalKcal.setText("0");
        lblAllergens.setText("-");

        tfAdvancedResarch.clear();
        tfDishName.clear();
        tfPrice.clear();
        tfResarchDish.clear();

        miTraditional.setSelected(false);
        miVegetarian.setSelected(false);
        miVegan.setSelected(false);
        miGlutenFree.setSelected(false);
        miLactoseFree.setSelected(false);
    }
}