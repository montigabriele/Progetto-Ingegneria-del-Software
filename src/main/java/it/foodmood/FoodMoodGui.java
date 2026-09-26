package it.foodmood;

import it.foodmood.config.ApplicationEnvironment;
import it.foodmood.config.UserMode;
import it.foodmood.view.ui.gui.GuiRouter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FoodMoodGui extends Application {

    private static ApplicationEnvironment environment;

    public static void setEnvironment(ApplicationEnvironment env){
        environment = env;
    }

    @Override
    public void start(Stage stage){

        UserMode userMode = environment.config().getUserMode();

        StackPane root = new StackPane();
        Scene scene = new Scene(root, 1440, 810);

        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.show();

        GuiRouter factory = new GuiRouter(scene, userMode);

        factory.showLoginView();
    }

    @Override
    public void stop(){
        System.out.print("\033[H\033[J");
        System.out.println("Grazie per aver utilizzato FoodMood, a presto!\n");
        System.exit(0);
    }
}
