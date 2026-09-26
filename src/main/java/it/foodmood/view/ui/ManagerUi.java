package it.foodmood.view.ui;

import it.foodmood.bean.ActorBean;

public interface ManagerUi extends BaseUi {
    void showHomeManagerView(ActorBean actorBean);
    void showIngredientManagmentView();
    void showDishManagmentView();
}
