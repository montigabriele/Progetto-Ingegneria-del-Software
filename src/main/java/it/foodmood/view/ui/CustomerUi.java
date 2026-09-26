package it.foodmood.view.ui;

import it.foodmood.bean.ActorBean;
import it.foodmood.bean.TableSessionBean;
import it.foodmood.view.ui.cli.pages.HomeCustomerPages;
import it.foodmood.view.ui.cli.pages.MenuCustomerPages;

public interface CustomerUi extends BaseUi {

    ActorBean showGuestView();

    TableSessionBean showTableSession();

    MenuCustomerPages showMenuCustumerView();

    HomeCustomerPages showHomeCustumerView(
            ActorBean actorBean,
            TableSessionBean tableSessionBean
    );

    void showPageNotImplemented();

    void showDigitalMenuCustumerView(
            ActorBean actorBean,
            TableSessionBean tableSessionBean
    );

    void showCustumerRecapOrderView(
            ActorBean actorBean,
            TableSessionBean tableSessionBean
    );

    void showAccountCustumerView(
            ActorBean actorBean
    );

    void showCustumerOrderCustomizationView(
            ActorBean actorBean,
            TableSessionBean tableSessionBean
    );
}