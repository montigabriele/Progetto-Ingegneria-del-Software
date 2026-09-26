package it.foodmood.view.ui.gui.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.foodmood.bean.IngredientBean;
import it.foodmood.bean.MacronutrientsBean;
import it.foodmood.controller.IngredientController;
import it.foodmood.domain.value.Unit;
import it.foodmood.exception.IngredientException;
import it.foodmood.utils.NutritionUtils;
import it.foodmood.utils.UnitUtils;
import it.foodmood.view.ui.gui.utils.BaseGui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.layout.AnchorPane;

public class GuiManagmentIngredients extends BaseGui {

    @FXML private Button btnCancel;

    @FXML private Button btnDeleteIngredient;

    @FXML private Button btnModifyIngredient;

    @FXML private Button btnNewIngredient;

    @FXML private Button btnSave;

    @FXML private CheckBox cbCelery;

    @FXML private CheckBox cbCrustaceans;

    @FXML private CheckBox cbEggs;

    @FXML private CheckBox cbFish;

    @FXML private CheckBox cbGluten;

    @FXML private CheckBox cbLupin;

    @FXML private CheckBox cbMilk;

    @FXML private CheckBox cbMolluscs;

    @FXML private CheckBox cbMustard;

    @FXML private CheckBox cbNuts;

    @FXML private CheckBox cbPeanuts;

    @FXML private CheckBox cbSesame;

    @FXML private CheckBox cbSoy;

    @FXML private CheckBox cbSulphites;

    @FXML private ComboBox<Unit> cbUnit;

    @FXML private TableColumn<IngredientBean, String> colAllergens;

    @FXML private TableColumn<IngredientBean, String> colKcal;

    @FXML private TableColumn<IngredientBean, String> colMacros;

    @FXML private TableColumn<IngredientBean, String> colName;

    @FXML private TableColumn<IngredientBean, String> colUnit;

    @FXML private Label lblAllergen;

    @FXML private Label lblTotalCarbohydrates;

    @FXML private Label lblTotalFats;

    @FXML private Label lblTotalKcal;

    @FXML private Label lblTotalProteins;

    @FXML private AnchorPane paneForm;

    @FXML private AnchorPane paneList;

    @FXML private TableView<IngredientBean> tableIngredients;

    @FXML private TextField tfCarbohydrates;

    @FXML private TextField tfFat;

    @FXML private TextField tfIngredientName;

    @FXML private TextField tfProtein;

    @FXML private TextField tfSearchIngredient;

    private IngredientController ingredientController;
    private final ObservableList<IngredientBean> allIngredients = FXCollections.observableArrayList();
    private FilteredList<IngredientBean> filteredIngredient;

    public void setIngredientController(IngredientController ingredientController){
        this.ingredientController = ingredientController;
        loadIngredients();
    }

    @FXML
    private void initialize(){
        initUnitComboBox();
        initTable();
        initSearchIngredient();
        initLivePreview();
        showListView();
    }
        
    private void initUnitComboBox(){
        cbUnit.getItems().clear();
        cbUnit.getItems().addAll(Unit.values());
        cbUnit.getSelectionModel().select(Unit.GRAM); // default
    }

    private void initTable(){

        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        colUnit.setCellValueFactory(cellData -> {
            Unit unit = cellData.getValue().getUnit();
            String unitLabel = UnitUtils.toLabel(unit);
            return new SimpleStringProperty(unitLabel);
        });

        colKcal.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(NutritionUtils.formatKcal(cellData.getValue().getMacronutrients()));
        });

        colMacros.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(NutritionUtils.formatMacros(cellData.getValue().getMacronutrients()));
        });

        colAllergens.setCellValueFactory(cellData -> {
            List<String> allergens = cellData.getValue().getAllergens();
            String allergenStr = allergens.isEmpty() ? "ALLERGENI NON PRESENTI" : String.join(", " , allergens);
            return new SimpleStringProperty(allergenStr);
        });
    }

    private void initSearchIngredient(){
        filteredIngredient = new FilteredList<>(allIngredients, ingredient -> true);

        SortedList<IngredientBean> sortedList = new SortedList<>(filteredIngredient);
        sortedList.comparatorProperty().bind(tableIngredients.comparatorProperty());

        tableIngredients.setItems(sortedList);

        //listener
        tfSearchIngredient.textProperty().addListener((obs, oldValue, newValue) -> {
            String filter = (newValue == null) ? "" : newValue.trim().toLowerCase(Locale.ROOT);

            if(filter.isEmpty()){
                filteredIngredient.setPredicate(ingredient -> true); // mostro tutti
            } else {
                filteredIngredient.setPredicate(ingredient -> {
                    String name = ingredient.getName();
                    return name != null && name.toLowerCase(Locale.ROOT).contains(filter);
                });
            }
        });
    }


    private void loadIngredients(){
        allIngredients.clear();

        try {
            List<IngredientBean> listIngredient = ingredientController.getAllIngredients();

            allIngredients.addAll(listIngredient);
        } catch (Exception e) {
            showError("Errore durante il caricamento degli ingredienti: " + e.getMessage());
        }
    }

    @FXML
    void onAddNewIngredient(ActionEvent event) {
        showFormView();
    }

    private Double parse(String text){
        if(text == null || text.isBlank()){
            return null;
        }
        try {
            return Double.valueOf(text.trim());
        } catch (Exception _) {
            showError("Valore non valido: " + text);
            return null;
        }
    }

    private Double parseForPreview(String text){
        if(text == null || text.isBlank()){
            return null;
        }
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException _) {
            return null;
        }
    }

    private void updateNutritionalSummary(){
        MacronutrientsBean macro = new MacronutrientsBean();
        macro.setProtein(parseForPreview(tfProtein.getText()));
        macro.setCarbohydrates(parseForPreview(tfCarbohydrates.getText()));
        macro.setFat(parseForPreview(tfFat.getText()));
        Double protein = macro.getProtein();
        Double carbohydrate = macro.getCarbohydrates();
        Double fat = macro.getFat();
        double p = (protein == null) ? 0.0 : protein;
        double c = (carbohydrate == null) ? 0.0 : carbohydrate;
        double f = (fat == null) ? 0.0 : fat;
        double kcal = macro.calculateKcal();
        lblTotalProteins.setText(String.format(Locale.ROOT, "%.1f", p));
        lblTotalCarbohydrates.setText(String.format(Locale.ROOT, "%.1f", c));
        lblTotalFats.setText(String.format(Locale.ROOT, "%.1f", f));
        lblTotalKcal.setText(String.format(Locale.ROOT, "%.1f", kcal));
    }

    private void updateAllergenSummary(){
        List<String> allergens = getSelectedAllergens();

        if(allergens.isEmpty()){
            lblAllergen.setText("Nessun allergene selezionato");
        } else {
            lblAllergen.setText(String.join(", ", allergens));
        }
    }


    private void initLivePreview(){

        ChangeListener<String> macronutrientsListener = (obs, oldVal, newVal) -> updateNutritionalSummary();

        tfProtein.textProperty().addListener(macronutrientsListener);
        tfCarbohydrates.textProperty().addListener(macronutrientsListener);
        tfFat.textProperty().addListener(macronutrientsListener);

        updateNutritionalSummary();

        ChangeListener<Boolean> allergenListener = (obs, oldVal, newVal) -> updateAllergenSummary();

        cbCelery.selectedProperty().addListener(allergenListener);
        cbCrustaceans.selectedProperty().addListener(allergenListener);
        cbEggs.selectedProperty().addListener(allergenListener);
        cbFish.selectedProperty().addListener(allergenListener);
        cbGluten.selectedProperty().addListener(allergenListener);
        cbLupin.selectedProperty().addListener(allergenListener);
        cbMilk.selectedProperty().addListener(allergenListener);
        cbMolluscs.selectedProperty().addListener(allergenListener);
        cbMustard.selectedProperty().addListener(allergenListener);
        cbNuts.selectedProperty().addListener(allergenListener);
        cbPeanuts.selectedProperty().addListener(allergenListener);
        cbSesame.selectedProperty().addListener(allergenListener);
        cbSoy.selectedProperty().addListener(allergenListener);
        cbSulphites.selectedProperty().addListener(allergenListener);

        updateAllergenSummary();
    }

    private List<String> getSelectedAllergens(){
        List<String> list = new ArrayList<>();

        if(cbCelery.isSelected()){ list.add("CELERY"); }
        if(cbCrustaceans.isSelected()){ list.add("CRUSTACEANS"); }
        if(cbEggs.isSelected()){ list.add("EGGS"); }        
        if(cbFish.isSelected()){ list.add("FISH"); }        
        if(cbGluten.isSelected()){ list.add("GLUTEN"); }        
        if(cbLupin.isSelected()){ list.add("LUPIN"); }        
        if(cbMilk.isSelected()){ list.add("MILK"); }        
        if(cbMolluscs.isSelected()){ list.add("MOLLUSCS"); }        
        if(cbMustard.isSelected()){ list.add("MUSTARD"); }        
        if(cbNuts.isSelected()){ list.add("NUTS"); }        
        if(cbPeanuts.isSelected()){ list.add("PEANUTS"); }        
        if(cbSesame.isSelected()){ list.add("SESAME"); }        
        if(cbSoy.isSelected()){ list.add("SOY"); }        
        if(cbSulphites.isSelected()){ list.add("SULPHITES"); }

        return list;
    }

    @FXML
    void onCancelIngredient(ActionEvent event) {
        showListView();
    }

    @FXML
    void onSaveIngredient(ActionEvent event) {

        IngredientBean ingredientBean = new IngredientBean();

        try {
            String name = tfIngredientName.getText();
            ingredientBean.setName(name);

            Unit unit = cbUnit.getValue();
            ingredientBean.setUnit(unit);

            MacronutrientsBean macronutrientsBean = new MacronutrientsBean();
            macronutrientsBean.setProtein(parse(tfProtein.getText()));
            macronutrientsBean.setCarbohydrates(parse(tfCarbohydrates.getText()));
            macronutrientsBean.setFat(parse(tfFat.getText()));
            ingredientBean.setMacronutrients(macronutrientsBean);

            ingredientBean.setAllergens(getSelectedAllergens());
            ingredientController.createIngredient(ingredientBean);
            loadIngredients();
            showInfo("Ingrediente creato correttamente");
            showListView();
        } catch (IllegalArgumentException | IngredientException e) {
            showError(e.getMessage());
        }  
    }

    private void showListView(){
        swap(paneList, paneForm);
    }

    private void showFormView(){
        swap(paneForm, paneList);
    }

    @FXML
    void onDeleteIngredient(ActionEvent event) {
        IngredientBean selected = tableIngredients.getSelectionModel().getSelectedItem();

        if(selected == null){
            showError("Seleziona un ingrediente da eliminare");
            return;
        }

        if(!showConfirmation("Conferma eliminazione", "Vuoi eliminare l'ingrediente:\n" + selected.getName() + " ?")) {
            return;
        }

        try {
            ingredientController.deleteIngredient(selected.getName());

            allIngredients.remove(selected);
            showInfo("Ingrediente eliminato correttamente.");
        } catch (Exception e) {
            showError("Errore durante l'eliminazione: " + e.getMessage());
        }
    }

    @FXML
    void onModifyIngredient(ActionEvent event) {
        showInfo("Funzionalità non ancora implementata");
    }

    @FXML
    void onSearchIngredient(ActionEvent event) {
        // Funzionalità non implementata
    }
}