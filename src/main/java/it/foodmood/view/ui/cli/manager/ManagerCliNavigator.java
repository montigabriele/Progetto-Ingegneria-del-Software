package it.foodmood.view.ui.cli.manager;

import it.foodmood.bean.ActorBean;
import it.foodmood.view.ui.ManagerUi;
import it.foodmood.view.ui.cli.TableConsoleView;
import it.foodmood.view.ui.cli.navigator.CliNavigator;
import it.foodmood.view.ui.cli.pages.ManagerPages;

public class ManagerCliNavigator extends TableConsoleView implements CliNavigator {

    private final ManagerUi ui;

    private ActorBean actor;

    public ManagerCliNavigator(ManagerUi managerUi) {
        this.ui = managerUi;
    }

    @Override
    public void start() {

        boolean exitApp = false;

        while (!exitApp) {

            actor = ui.showLoginView();

            if (actor == null || !actor.isLogged()) {
                exitApp = true;
                continue;
            }

            exitApp = runManagerMenu();
        }
    }

    private boolean runManagerMenu() {

        while (true) {

            CliManagerMenuView menuView =
                    new CliManagerMenuView();

            ManagerPages page =
                    menuView.displayPage(actor);

            switch (page) {

                case MANAGMENT_INGREDIENTS ->
                        ui.showIngredientManagmentView();

                case MANAGMENT_DISH ->
                        ui.showDishManagmentView();

                case LOGOUT -> {

                    boolean logoutConfirmed =
                            ui.showLogoutView();

                    if (logoutConfirmed) {
                        actor = null;
                        return false;
                    }
                }

                case EXIT -> {
                    actor = null;
                    return true;
                }
            }
        }
    }
}