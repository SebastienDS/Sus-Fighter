package fr.uge.susfighter.mvc;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StageManager {

    public enum StageEnum {
        MENU("menu-view.fxml"), GAME("game-view.fxml");

        private final String name;

        StageEnum(String name) {
            this.name = Objects.requireNonNull(name);
        }

        public String getName() {
            return name;
        }
    }

    private static Stage STAGE_SAVE;
    private static int WIDTH;
    private static int HEIGHT;

    public static void setStage(Stage stage, int width, int height) {
        STAGE_SAVE = Objects.requireNonNull(stage);
        WIDTH = width;
        HEIGHT = height;
    }

    public static Stage getStage() {
        return STAGE_SAVE;
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static void setScene(StageEnum stageEnum) throws IOException {
        var loader = new FXMLLoader(StageManager.class.getResource(stageEnum.getName()));
        AnchorPane container = loader.load();
        var scene = new Scene(container);
        System.out.println("SCENE" + scene);
        STAGE_SAVE.setScene(scene);
    }

    public static Scene getScene() {
        return STAGE_SAVE.getScene();
    }

}
