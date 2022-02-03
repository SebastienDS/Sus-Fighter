package fr.uge.susfighter;

import fr.uge.susfighter.mvc.Duel;
import fr.uge.susfighter.mvc.DuelManager;
import fr.uge.susfighter.mvc.GameController;
import fr.uge.susfighter.mvc.StageManager;
import fr.uge.susfighter.object.*;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        var screen = Screen.getPrimary().getBounds();
        StageManager.setStage(stage, (int)(screen.getWidth() * 0.8), (int)(screen.getHeight() * 0.8));
        stage.setTitle("Sus-Fighter");
        stage.setResizable(false);
        StageManager.setScene(StageManager.StageEnum.MENU);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}