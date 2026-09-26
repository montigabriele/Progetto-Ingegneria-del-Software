package it.foodmood.view.ui.gui.customer;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.foodmood.bean.ActorBean;
import it.foodmood.bean.AnswerBean;
import it.foodmood.bean.CartItemBean;
import it.foodmood.bean.DishBean;
import it.foodmood.bean.ResponseBean;
import it.foodmood.bean.OrderFlowBean;
import it.foodmood.controller.CartController;
import it.foodmood.controller.OrderProposalsController;
import it.foodmood.domain.value.Allergen;
import it.foodmood.domain.value.CourseType;
import it.foodmood.domain.value.DietCategory;
import it.foodmood.domain.value.StepType;
import it.foodmood.exception.OrderException;
import it.foodmood.exception.CartException;
import it.foodmood.view.ui.gui.GuiRouter;
import it.foodmood.view.ui.gui.utils.BaseGui;
import it.foodmood.view.ui.gui.utils.GuiCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class GuiCustomerOrder extends BaseGui {
        
    @FXML private GridPane menuGridPane;
    @FXML private Button btnOrderSummary;
    @FXML private Button btnNextFirst;
    @FXML private Button btnNextSecond;
    @FXML private Button btnGenerate;
    @FXML private ToggleButton tbBudgetEconomic;
    @FXML private Label lblBudgetEconomic;
    @FXML private Label lblBudgetBalanced;
    @FXML private Label lblBudgetPremium;
    @FXML private Label lblUserInitials;
    @FXML private ToggleButton tbBudgetBalanced;
    @FXML private ToggleButton tbBudgetPremium;
    @FXML private ToggleButton tbBudgetNoLimit;
    @FXML private Label lblKcalLight;
    @FXML private Label lblKcalModerate;
    @FXML private Label lblKcalComplete;
    @FXML private ToggleButton tbKcalNoLimit;
    @FXML private ToggleButton tbKcalLight;
    @FXML private ToggleButton tbKcalModerate;
    @FXML private ToggleButton tbKcalComplete;
    @FXML private ToggleButton tbBeverage;
    @FXML private ToggleButton tbDessert;
    @FXML private ToggleButton tbFruit;
    @FXML private ToggleButton tbPizza;
    @FXML private ToggleButton tbSideDish;
    @FXML private ToggleButton tbCelery;
    @FXML private ToggleButton tbCrustaceans;
    @FXML private ToggleButton tbEggs;
    @FXML private ToggleButton tbFish;
    @FXML private ToggleButton tbGluten;
    @FXML private ToggleButton tbLupin;
    @FXML private ToggleButton tbMilk;
    @FXML private ToggleButton tbMolluscs;
    @FXML private ToggleButton tbMustard;
    @FXML private ToggleButton tbNuts;
    @FXML private ToggleButton tbPeanuts;
    @FXML private ToggleButton tbSesame;
    @FXML private ToggleButton tbSoy;
    @FXML private ToggleButton tbSulphites;
    @FXML private ToggleButton tbAppetizer;
    @FXML private ToggleButton tbFirstCourse;
    @FXML private ToggleButton tbMainCourse;
    @FXML private Button btnBackFirst;
    @FXML private Button btnBackSecond;
    @FXML private Button btnBackThird;
    @FXML private Button btnBackFourth;
    @FXML private Button btnBackFifth;
    @FXML private Button btnNextThird;
    @FXML private Button btnNextFourth;
    @FXML private Button btnAccount;
    @FXML private Button btnCart;
    @FXML private BorderPane firstQuestionPane;
    @FXML private BorderPane secondQuestionPane;
    @FXML private BorderPane thirdQuestionPane;
    @FXML private BorderPane fourthQuestionPane;
    @FXML private BorderPane fifthQuestionPane;
    @FXML private BorderPane proposePane;
    @FXML private ToggleButton tbGlutenFree;
    @FXML private ToggleButton tbLactoseFree;
    @FXML private ToggleButton tbTraditional;
    @FXML private ToggleButton tbVegan;
    @FXML private ToggleButton tbVegetarian;

    private static final String OPTION = "Seleziona un'opzione per continuare"; 
    private static final String ERROR = "Errore: ";
    private static final String KCAL = " Kcal)";
    private static final String UNTIL = " (fino a ";
    private static final String WITHIN = " (entro i ";
    private static final String EURO = " €)";

    private final OrderProposalsController orderController = new OrderProposalsController();
    private final CartController cartController = new CartController();
    private final OrderFlowBean orderFlowBean = new OrderFlowBean();

    private GuiRouter router;
    private ToggleGroup dietGroup;    
    private ToggleGroup kcalGroup;
    private ToggleGroup budgetGroup;
    private final EnumMap<Allergen, ToggleButton> allergenButtons = new EnumMap<>(Allergen.class);

    public GuiCustomerOrder(){
        // vuoto
    }

    public void setRouter(GuiRouter router) {
        this.router = router;

        updateLabel();
        initializeFlow();
    }

    @FXML
    void onBackToFirstQuestion(ActionEvent event) {
        showFirstQuestion();
    }

    @FXML
    void onBackToSecondQuestion(ActionEvent event){
        showSecondQuestion();
    }

    @FXML
    void onBackToThirdQuestion(ActionEvent event){
        showThirdQuestion();
    }

    @FXML
    void onBackToFourthQuestion(ActionEvent event){
        showFourthQuestion();
    }

    @FXML
    void onBackToHome(ActionEvent event) {
        router.showHomeCustumerView();
    }

    @FXML
    void onBackToHomePage(MouseEvent event) {
        router.showHomeCustumerView();
    }

    @FXML
    private void initialize() {

        setupToggleGroups();
        initAllergenButtons();
    }

    private void updateLabel(){
        if(lblUserInitials != null && router != null){
            lblUserInitials.setText(getUserInitials(router.getActor()));
        }
    }

    private void handleResponse(ResponseBean response){
        if(response == null){
            showError("Errore");
            return;
        }

        StepType nextStep = response.getNextStep();

        switch (nextStep) {
            case COURSE -> showFirstQuestion();
            case DIET   -> showSecondQuestion();
            case ALLERGENS ->{
                updateAllergenButtons(response);
                showThirdQuestion();
            }
            case KCAL -> {
                updateKcalOptions(response);
                showFourthQuestion();
            }
            case BUDGET -> {
                updateBudgetOptions(response);
                showFifthQuestion();
            }
            case GENERATE -> {

                if (displayProposals(response)) {
                    showProposePane();
                }
            }
            default -> showError("Step non riconosciuto: " + nextStep);
        }
    }

    private void showOnly(BorderPane paneToShow){
        hidePane(firstQuestionPane);
        hidePane(secondQuestionPane);
        hidePane(thirdQuestionPane);
        hidePane(fourthQuestionPane);
        hidePane(fifthQuestionPane);
        hidePane(proposePane);

        showPane(paneToShow);
    }

    private void hidePane(BorderPane pane){
        if(pane != null){
            pane.setVisible(false);
            pane.setManaged(false);
        }
    }

    private void showPane(BorderPane pane){
        if(pane != null){
            pane.setVisible(true);
            pane.setManaged(true);
        }
    }

    private void showFirstQuestion(){
        showOnly(firstQuestionPane);
    }

    private void showSecondQuestion(){
        showOnly(secondQuestionPane);
    }

    private void showThirdQuestion(){
        showOnly(thirdQuestionPane);
    }

    private void showFourthQuestion(){
        showOnly(fourthQuestionPane);
    }

    private void showFifthQuestion(){
        showOnly(fifthQuestionPane);
    }

    private void showProposePane(){
        showOnly(proposePane);
    }

    @FXML
    void onAccountClicked(ActionEvent event) {
        ActorBean actor = router.getActor();
        if(!actor.isGuest()){
            router.showCustomerAccountView();
        } else {
            showInfo("Effettua l'accesso per vedere la sezione Account");
        }
    }

    private void setupToggleGroups(){
        dietGroup = singleChoice(List.of(tbTraditional, tbVegan, tbVegetarian));
        multipleChoice(List.of(tbGlutenFree, tbLactoseFree));
        multipleChoice(List.of(tbAppetizer, tbFirstCourse, tbMainCourse, tbSideDish, tbFruit, tbPizza, tbDessert, tbBeverage));
        kcalGroup = singleChoice(List.of(tbKcalNoLimit, tbKcalLight, tbKcalModerate, tbKcalComplete));
        budgetGroup = singleChoice(List.of(tbBudgetNoLimit, tbBudgetEconomic, tbBudgetBalanced, tbBudgetPremium));
    }

    private void initializeFlow(){
        ResponseBean responseBean =
                orderController.start(orderFlowBean);

        handleResponse(responseBean);
    }

    private AnswerBean dietAnswers(){
        Set<String> answers = new HashSet<>();

        if(tbTraditional.isSelected()) answers.add(DietCategory.TRADITIONAL.name());
        if(tbVegan.isSelected()) answers.add(DietCategory.VEGAN.name());
        if(tbVegetarian.isSelected()) answers.add(DietCategory.VEGETARIAN.name());
        if(tbGlutenFree.isSelected()) answers.add(DietCategory.GLUTEN_FREE.name());
        if(tbLactoseFree.isSelected()) answers.add(DietCategory.LACTOSE_FREE.name());

        AnswerBean answerBean = new AnswerBean();
        answerBean.setStepType(StepType.DIET);
        answerBean.setAnswers(answers);
        return answerBean;
    }

    @FXML
    private void onNextSecond(ActionEvent e){

        try {
            AnswerBean answerBean = dietAnswers();
            if(answerBean.getAnswers().isEmpty() && dietGroup.getSelectedToggle() == null){
                showInfo("Seleziona almeno una tipologia dietetica per continuare");
                return;
            }

            ResponseBean responseBean = orderController.submit(orderFlowBean,answerBean);

            handleResponse(responseBean);
        } catch (OrderException ex) {
            showError(ERROR + ex.getMessage());
        }
    }

    private AnswerBean courseAnswers(){
        Set<String> answers = new HashSet<>();

        if(tbAppetizer.isSelected()) answers.add(CourseType.APPETIZER.name());
        if(tbFirstCourse.isSelected()) answers.add(CourseType.FIRST_COURSE.name());
        if(tbMainCourse.isSelected()) answers.add(CourseType.MAIN_COURSE.name());
        if(tbSideDish.isSelected()) answers.add(CourseType.SIDE_DISH.name());
        if(tbFruit.isSelected()) answers.add(CourseType.FRUIT.name());
        if(tbPizza.isSelected()) answers.add(CourseType.PIZZA.name());
        if(tbDessert.isSelected()) answers.add(CourseType.DESSERT.name());
        if(tbBeverage.isSelected()) answers.add(CourseType.BEVERAGE.name());
        
        AnswerBean answerBean = new AnswerBean();
        answerBean.setStepType(StepType.COURSE);
        answerBean.setAnswers(answers);
        return answerBean;
    }

    @FXML
    private void onNextThird(ActionEvent e){
        try {
            AnswerBean answerBean = allergenAnswers();
            ResponseBean responseBean = orderController.submit(orderFlowBean,answerBean);
            handleResponse(responseBean);

        } catch (OrderException ex) {
            showError(ERROR + ex.getMessage());
        }
    }

    @FXML
    private void onNextFourth(ActionEvent e){
        try {
            AnswerBean answerBean = kcalAnswers();

            if(kcalGroup.getSelectedToggle() == null){
                showInfo(OPTION);
                return;
            }

            ResponseBean responseBean = orderController.submit(orderFlowBean,answerBean);

            handleResponse(responseBean);
        } catch (OrderException ex) {
            showError(ERROR + ex.getMessage());
        }
    }

    @FXML
    private void onNextFifth(ActionEvent e) {
        submitBudget();
    }

    @FXML
    private void onNextFirst(ActionEvent e){
        try {
            AnswerBean answerBean = courseAnswers();

            if(answerBean.getAnswers().isEmpty()){
                showInfo("Seleziona almeno una portata per continuare");
                return;
            }

            ResponseBean responseBean = orderController.submit(orderFlowBean,answerBean);

            handleResponse(responseBean);
        } catch (OrderException ex) {
            showError(ERROR + ex.getMessage());
        }
    }

    private AnswerBean kcalAnswers(){
        Integer selectedValue = null;

        Toggle selected = kcalGroup.getSelectedToggle();
        if(selected == tbKcalLight && lblKcalLight != null){
            selectedValue = extractIntFromLabel(lblKcalLight.getText());
        } else if(selected == tbKcalModerate && lblKcalModerate != null){
            selectedValue = extractIntFromLabel(lblKcalModerate.getText());
        } else if(selected == tbKcalComplete && lblKcalComplete != null){
            selectedValue = extractIntFromLabel(lblKcalComplete.getText());
        } else if (selected == tbKcalNoLimit) {
            selectedValue = null;
        }
        
        AnswerBean answerBean = new AnswerBean();
        answerBean.setStepType(StepType.KCAL);
        answerBean.setValue(selectedValue);
        return answerBean;
    }

    private AnswerBean budgetAnswers(){
        Integer selectedValue = null;

        Toggle selected = budgetGroup.getSelectedToggle();
        if(selected == tbBudgetEconomic && lblBudgetEconomic != null){
            selectedValue = extractIntFromLabel(lblBudgetEconomic.getText());
        } else if(selected == tbBudgetBalanced && lblBudgetBalanced != null){
            selectedValue = extractIntFromLabel(lblBudgetBalanced.getText());
        } else if(selected == tbBudgetPremium && lblBudgetPremium != null){
            selectedValue = extractIntFromLabel(lblBudgetPremium.getText());
        } else if (selected == tbBudgetNoLimit) {
            selectedValue = null;
        }
        
        AnswerBean answerBean = new AnswerBean();
        answerBean.setStepType(StepType.BUDGET);
        answerBean.setValue(selectedValue);
        return answerBean;    
    }

    private Integer extractIntFromLabel(String text){
        if(text == null){
            return null;
        }
        try {
            String value = text.replaceAll("\\D", "");
            return value.isEmpty() ? null : Integer.parseInt(value);
        } catch (NumberFormatException _) {
            return null;
        }
    }

    private AnswerBean allergenAnswers(){
        Set<String> answers = new HashSet<>();

        if(tbCelery.isSelected()) answers.add(Allergen.CELERY.name());
        if(tbCrustaceans.isSelected()) answers.add(Allergen.CRUSTACEANS.name());
        if(tbEggs.isSelected()) answers.add(Allergen.EGGS.name());
        if(tbFish.isSelected()) answers.add(Allergen.FISH.name());
        if(tbGluten.isSelected()) answers.add(Allergen.GLUTEN.name());
        if(tbLupin.isSelected()) answers.add(Allergen.LUPIN.name());
        if(tbMilk.isSelected()) answers.add(Allergen.MILK.name());
        if(tbMolluscs.isSelected()) answers.add(Allergen.MOLLUSCS.name());
        if(tbMustard.isSelected()) answers.add(Allergen.MUSTARD.name());
        if(tbNuts.isSelected()) answers.add(Allergen.NUTS.name());
        if(tbPeanuts.isSelected()) answers.add(Allergen.PEANUTS.name());
        if(tbSesame.isSelected()) answers.add(Allergen.SESAME.name());
        if(tbSoy.isSelected()) answers.add(Allergen.SOY.name());
        if(tbSulphites.isSelected()) answers.add(Allergen.SULPHITES.name());
        
        AnswerBean answerBean = new AnswerBean();
        answerBean.setStepType(StepType.ALLERGENS);
        answerBean.setAnswers(answers);
        return answerBean;
    }

    @FXML
    private void onGenerate(ActionEvent e) {
        submitBudget();
    }

    private final ObservableList<DishBean> proposedDishes = FXCollections.observableArrayList();

    private ToggleGroup singleChoice(List<ToggleButton> buttons){
        ToggleGroup group = new ToggleGroup();
        for(ToggleButton b : buttons){
            b.setToggleGroup(group);
        }
        return group;
    }

    private void submitBudget() {

        try {

            AnswerBean answerBean =
                    budgetAnswers();

            if (budgetGroup.getSelectedToggle() == null) {
                showInfo(OPTION);
                return;
            }

            ResponseBean responseBean =
                    orderController.submit(
                            orderFlowBean,
                            answerBean
                    );

            handleResponse(responseBean);

        } catch (OrderException ex) {

            showError(
                    ERROR + ex.getMessage()
            );
        }
    }

    private void multipleChoice(List<ToggleButton> buttons){
        for(ToggleButton b : buttons){
            b.setToggleGroup(null);
        }
    }

    private void updateAllergenButtons(ResponseBean response){
        Set<Allergen> relevantAllergens = response.getAllergens();

        if(relevantAllergens == null || relevantAllergens.isEmpty()){
            return;
        }

        for(ToggleButton b : allergenButtons.values()){
            b.setVisible(false);
            b.setManaged(false);
        }

        for(Allergen allergen : relevantAllergens){
            ToggleButton b = allergenButtons.get(allergen);
            if(b != null){
                b.setVisible(true);
                b.setManaged(true);
            }
        }
    }

    private void initAllergenButtons(){
        allergenButtons.put(Allergen.CELERY, tbCelery);
        allergenButtons.put(Allergen.CRUSTACEANS, tbCrustaceans);
        allergenButtons.put(Allergen.EGGS, tbEggs);
        allergenButtons.put(Allergen.FISH, tbFish);
        allergenButtons.put(Allergen.GLUTEN, tbGluten);
        allergenButtons.put(Allergen.LUPIN, tbLupin);
        allergenButtons.put(Allergen.MILK, tbMilk);
        allergenButtons.put(Allergen.MOLLUSCS, tbMolluscs);
        allergenButtons.put(Allergen.MUSTARD, tbMustard);
        allergenButtons.put(Allergen.NUTS, tbNuts);
        allergenButtons.put(Allergen.PEANUTS, tbPeanuts);
        allergenButtons.put(Allergen.SESAME, tbSesame);
        allergenButtons.put(Allergen.SOY, tbSoy);
        allergenButtons.put(Allergen.SULPHITES, tbSulphites);
    }

    private void updateKcalOptions(ResponseBean response){
        List<Integer> values = response.getValues();
        if(values != null && values.size() >= 3){
            lblKcalLight.setText("Leggero " + UNTIL + values.get(0) + KCAL);
            lblKcalModerate.setText("Moderato " + UNTIL + values.get(1) +  KCAL);
            lblKcalComplete.setText("Abbondante " + UNTIL + values.get(2) +  KCAL);
        }
    }

    private void updateBudgetOptions(ResponseBean response){
        List<Integer> values = response.getValues();
        if(values != null && values.size() >= 3){
            lblBudgetEconomic.setText("Economico " + WITHIN + values.get(0) + EURO);
            lblBudgetBalanced.setText("Equilibrato " + WITHIN + values.get(1) + EURO);
            lblBudgetPremium.setText("Premium " + WITHIN + values.get(2) + EURO);
        }
    }

    private boolean displayProposals(
            ResponseBean responseBean
    ) {

        List<DishBean> dishes =
                responseBean.getDishes();

        if (dishes == null || dishes.isEmpty()) {

            showInfo(
                    "Nessun piatto trovato con i criteri selezionati"
            );

            router.showHomeCustumerView();

            return false;
        }

        proposedDishes.setAll(dishes);
        refreshMenuGrid();

        return true;
    }

    @FXML
    void onCartClicked(ActionEvent event) {
        router.showCustomerRecapOrder();
    }

    @FXML
    void onSummary(ActionEvent event) {
        router.showCustomerRecapOrder();
    }

    private void refreshMenuGrid(){
        menuGridPane.getChildren().clear();

        final int columns = 2;
        int col = 0;
        int row = 0;

        for(DishBean dish: proposedDishes){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/card.fxml"));
                AnchorPane cardNode = loader.load();

                GuiCard cardController = loader.getController();
                
                cardController.setData(dish);
                cardController.setOnAddToOrder(this::addToOrder);

                menuGridPane.add(cardNode, col, row);
                GridPane.setMargin(cardNode, new Insets(5));

                col ++;
                if(col == columns){
                    col = 0;
                    row++;
                }
            } catch (Exception e) {
                showError("Errore nell'inserimento dei prodotti nel menù: " + e.getMessage());
            }
        }
    }

    private void addToOrder(DishBean dishBean){

        if(router == null
                || router.getActor() == null
                || router.getTableSession() == null){
            showError("Contesto ordine non valido");
            return;
        }

        try {

            CartItemBean itemBean =
                    new CartItemBean();

            itemBean.setDishId(
                    dishBean.getId()
            );

            itemBean.setQuantity(1);

            cartController.addToCart(
                    router.getActor().getActorId(),
                    router.getTableSession().getTableSessionId(),
                    itemBean
            );

            showInfo(
                    dishBean.getName()
                            + " aggiunto all'ordine."
            );

        } catch (CartException e) {

            showError(
                    "Errore durante l'aggiunta all'ordine: "
                            + e.getMessage()
            );
        }
    }
}


