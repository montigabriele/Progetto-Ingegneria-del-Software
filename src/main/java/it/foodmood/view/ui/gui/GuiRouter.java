package it.foodmood.view.ui.gui;

import it.foodmood.bean.ActorBean;
import it.foodmood.bean.TableSessionBean;
import it.foodmood.config.UserMode;
import it.foodmood.view.ui.gui.customer.GuiCustomerAccount;
import it.foodmood.view.ui.gui.customer.GuiCustomerCart;
import it.foodmood.view.ui.gui.customer.GuiCustomerDigitalMenu;
import it.foodmood.view.ui.gui.customer.GuiCustomerOrder;
import it.foodmood.view.ui.gui.customer.GuiHomeCustomer;
import it.foodmood.view.ui.gui.customer.GuiRegistrationView;
import it.foodmood.view.ui.gui.customer.GuiTableSession;
import it.foodmood.view.ui.gui.manager.GuiHomeManager;
import it.foodmood.view.ui.gui.waiter.GuiHomeWaiter;
import javafx.scene.Scene;

public final class GuiRouter{

    private final UserMode userMode;
    private final GuiNavigator navigator;
    private TableSessionBean currentTableSession;
    private ActorBean actorBean;

    public GuiRouter(Scene scene, UserMode userMode){
        this.navigator = new GuiNavigator(scene);
        this.userMode = userMode;
        this.actorBean = new ActorBean();
    }

    public void showHomeView(){
        switch (userMode) {
            case CUSTOMER -> showHomeCustumerView();
            case WAITER -> showHomeWaiterView();
            case MANAGER -> showHomeManagerView();
        }
    }

    public void showLoginView(){
        showLoginView(userMode != UserMode.CUSTOMER);
    }  

    public void showLoginView(boolean start){
        GuiLoginView controller = navigator.goTo(GuiPages.LOGIN);
        controller.setRouter(this);
        controller.setUserMode(userMode);
        controller.setStartOnLogin(start);
    } 

    public void showRegistrationView(){
        GuiRegistrationView controller = navigator.goTo(GuiPages.REGISTRATION);
        controller.setRouter(this);
    }

    public void showHomeCustumerView(TableSessionBean tableSessionBean){
        this.currentTableSession = tableSessionBean;
        showHomeCustumerView();
    }

    public void showHomeCustumerView(){
        GuiHomeCustomer controller = navigator.goTo(GuiPages.HOME_CUSTOMER);
        controller.setRouter(this);
    }

    public void showHomeManagerView(){
        GuiHomeManager controller = navigator.goTo(GuiPages.HOME_MANAGER);
        controller.setRouter(this);
        controller.openDefaultPage();
    }

    public void showHomeWaiterView() {
        GuiHomeWaiter controller = navigator.goTo(GuiPages.HOME_WAITER);
        controller.setRouter(this);
    }

    public void showCustomerAccountView(){
        GuiCustomerAccount controller = navigator.goTo(GuiPages.CUSTOMER_ACCOUNT);
        controller.setRouter(this);
    }

    public void showCustomerDigitalMenu(){
        GuiCustomerDigitalMenu controller = navigator.goTo(GuiPages.CUSTOMER_DIGITAL_MENU);
        controller.setRouter(this);
    }

    public void showCustomerOrderView(){
        GuiCustomerOrder controller = navigator.goTo(GuiPages.CUSTOMER_ORDER);
        controller.setRouter(this);
    }

    public void showCustomerRecapOrder(){
        GuiCustomerCart controller = navigator.goTo(GuiPages.CUSTOMER_RECAP_ORDER);
        controller.setRouter(this);
    }

    public void showSessionTableView(){
        GuiTableSession controller = navigator.goTo(GuiPages.TABLE_SESSION);
        controller.setRouter(this);
    }

    public void setActor(ActorBean actorBean){
        this.actorBean = actorBean;
    }

    public ActorBean getActor(){
        return actorBean;
    }

    public int getTableNumber(){
        return currentTableSession.getTableId();
    }

    public TableSessionBean getTableSession(){
        return currentTableSession;
    }

    public void clearContext() {
        this.actorBean = null;
        this.currentTableSession = null;
    }
}
