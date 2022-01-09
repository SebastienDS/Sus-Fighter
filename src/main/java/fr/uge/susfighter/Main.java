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

        var players = initPlayers();
        var map = new Field(Element.WATER, new ArrayList<>(), new Vec2(0, 1),
                new Rectangle(0, 0, StageManager.getWidth(), StageManager.getHeight()));

        var duel = new Duel(players, map, Optional.empty(), 999);
        DuelManager.setDuel(duel);

        stage.setTitle("Sus-Fighter");

        StageManager.setScene(StageManager.StageEnum.MENU);
        stage.show();

        GameController.startGame();
    }

    public static void main(String[] args) {
        launch();
    }

    private static List<Player> initPlayers() {
        var command1 = new Command();
        command1.addKeyCode(Command.Key.LEFT, KeyCode.Q)
                .addKeyCode(Command.Key.RIGHT, KeyCode.D)
                .addKeyCode(Command.Key.UP, KeyCode.Z)
                .addKeyCode(Command.Key.DOWN, KeyCode.S)
                .addKeyCode(Command.Key.ATTACK, KeyCode.T);

        var command2 = new Command();
        command2.addKeyCode(Command.Key.LEFT, KeyCode.LEFT)
                .addKeyCode(Command.Key.RIGHT, KeyCode.RIGHT)
                .addKeyCode(Command.Key.UP, KeyCode.UP)
                .addKeyCode(Command.Key.DOWN, KeyCode.DOWN)
                .addKeyCode(Command.Key.ATTACK, KeyCode.NUMPAD1);

        return List.of(
                new Player("purple", new Rectangle(150, 150, 150, 250),
                        Element.WATER, command1, 1, false),
                new Player("red",  new Rectangle(600, 150, 150, 250),
                        Element.FIRE, command2, 2, true)
        );
    }
}