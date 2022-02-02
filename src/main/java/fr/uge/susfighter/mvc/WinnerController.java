package fr.uge.susfighter.mvc;

import com.google.gson.reflect.TypeToken;
import fr.uge.susfighter.mvc.ImageManager.ImageKey;
import fr.uge.susfighter.object.Bot;
import fr.uge.susfighter.object.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static fr.uge.susfighter.mvc.MenuController.getFromJson;

public class WinnerController {

    @FXML
    private AnchorPane pane;

    @FXML
    private Button quit;

    @FXML
    private Button retry;

    @FXML
    private Button next;

    @FXML
    private ImageView player;

    @FXML
    void initialize() {
        var duel = DuelManager.getDuel();
        var winner = ImageManager.getImage(ImageKey.valueOf("PLAYER_" + duel.getWinner().getNumPlayer() + "_IDLE"));
        player.setImage(winner);
        AnchorPane.setTopAnchor(player, 2 * StageManager.getHeight() / 3 - winner.getHeight() / 2);
        AnchorPane.setLeftAnchor(player, StageManager.getWidth() / 2 - winner.getWidth() / 2);
        initButtons(duel.getWinner().getNumPlayer(), duel.getStep());
        initBackgroundAndSound();
    }

    private void initBackgroundAndSound() {
        var name = "defeat.png";
        var sound = "defeat.mp3";
        if(DuelManager.getDuel().getWinner().getNumPlayer() == 1 || DuelManager.getDuel().getStep() == -1){
            name = "winner.png";
            sound = "winner.mp3";
        }
        var img = ImageManager.loadImage(
                Objects.requireNonNull(this.getClass().getResource("images/background/" + name)).toExternalForm()
        );
        var bImg = new BackgroundImage(img,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        var bGround = new Background(bImg);
        MediaPlayerManager.setSound(new Media(
                Objects.requireNonNull(this.getClass().getResource("sounds/" + sound)).toExternalForm()), false);
        pane.setBackground(bGround);
    }

    private void initButtons(int numPlayer, int step) {
        var font = Font.loadFont(Objects.requireNonNull(this.getClass().getResource("font/font.ttf")).toExternalForm(), 30);
        quit.setFont(font);
        var button = (numPlayer == 1 && step != -1)? next : retry;
        button.setFont(font);
        button.setVisible(true);
        placeButton(quit, StageManager.getWidth() / 5. - quit.getPrefWidth() / 2, 4 * StageManager.getHeight() / 5.);
        placeButton(button, 4 * StageManager.getWidth() / 5. - retry.getPrefWidth() / 2, 4 * StageManager.getHeight() / 5.);

    }

    private void placeButton(Button quit, double x, double y) {
        AnchorPane.setLeftAnchor(quit, x);
        AnchorPane.setTopAnchor(quit, y);
    }

    @FXML
    void leave() throws IOException {
        StageManager.setScene(StageManager.StageEnum.MENU);
    }

    @FXML
    void retry() throws IOException, URISyntaxException {
        var duel = DuelManager.getDuel();
        if(duel.getStep() == -1) startPvP(duel);
        else startPvBot(duel);

    }

    private void startPvBot(Duel duel) throws IOException, URISyntaxException {
        MenuController.MapData mapData = getFromJson("Maps/" + duel.getMap().name() + ".json", new TypeToken<MenuController.MapData>(){}.getType());

        var posPlayer1 = mapData.startPosition.get(0);
        var posPlayer2 = mapData.startPosition.get(1);

        var x = (int)(StageManager.getWidth() * posPlayer1.getX() / 100.0);
        var y = (int)(StageManager.getHeight() * posPlayer1.getY() / 100.0);
        var p1 = new Player((Player)duel.getPlayer(0), x, y, false, 1);
        var map = duel.getMap();

        x = (int)(StageManager.getWidth() * posPlayer2.getX() / 100.0);
        y = (int)(StageManager.getHeight() * posPlayer2.getY() / 100.0);

        var IA = new Bot((Bot)duel.getPlayer(1), p1, x, y);
        MediaPlayerManager.setSound(new Media(
                Objects.requireNonNull(this.getClass().getResource("sounds/" + mapData.sound)).toExternalForm()
        ));
        DuelManager.setDuel(new Duel(List.of(p1, IA), map, 99, duel.getLevel(), duel.getStep()));
        StageManager.setScene(StageManager.StageEnum.GAME);
        GameController.startGame();
    }

    private void startPvP(Duel duel) throws IOException, URISyntaxException {
        MenuController.MapData mapData = getFromJson("Maps/" + duel.getMap().name() + ".json", new TypeToken<MenuController.MapData>(){}.getType());

        var posPlayer1 = mapData.startPosition.get(0);
        var posPlayer2 = mapData.startPosition.get(1);

        var x = (int)(StageManager.getWidth() * posPlayer1.getX() / 100.0);
        var y = (int)(StageManager.getHeight() * posPlayer1.getY() / 100.0);
        var p1 = new Player((Player)duel.getPlayer(0), x, y, false, 1);
        var map = duel.getMap();

        x = (int)(StageManager.getWidth() * posPlayer2.getX() / 100.0);
        y = (int)(StageManager.getHeight() * posPlayer2.getY() / 100.0);

        var p2 = new Player((Player)duel.getPlayer(1), x, y, true, 2);
        MediaPlayerManager.setSound(new Media(
                Objects.requireNonNull(this.getClass().getResource("sounds/" + mapData.sound)).toExternalForm()
        ));
        DuelManager.setDuel(new Duel(List.of(p1, p2), map, 99, duel.getLevel(), duel.getStep()));
        StageManager.setScene(StageManager.StageEnum.GAME);
        GameController.startGame();
    }

    @FXML
    void nextLevel() throws IOException, URISyntaxException {
        var duel = DuelManager.getDuel();
        var step = (duel.getStep() + 1) % 3;
        var level = (step == 0)? duel.getLevel() + 1: duel.getLevel();

        MenuController.MapData mapData = getFromJson("Maps/" + duel.getMap().name() + ".json", new TypeToken<MenuController.MapData>(){}.getType());
        var posPlayer1 = mapData.startPosition.get(0);
        var posPlayer2 = mapData.startPosition.get(1);

        var x = (int)(StageManager.getWidth() * posPlayer1.getX() / 100.0);
        var y = (int)(StageManager.getHeight() * posPlayer1.getY() / 100.0);
        var p1 = new Player((Player)duel.getPlayer(0), x, y, false, 1);

        x = (int)(StageManager.getWidth() * posPlayer2.getX() / 100.0);
        y = (int)(StageManager.getHeight() * posPlayer2.getY() / 100.0);
        var map = MenuController.getMap(level, step);
        MediaPlayerManager.setSound(new Media(
                Objects.requireNonNull(this.getClass().getResource("sounds/" + mapData.sound)).toExternalForm()
        ));
        var IA = MenuController.initBot(level, step, p1, x, y);
        DuelManager.setDuel(new Duel(List.of(p1, IA), map, 99, level, step));
        StageManager.setScene(StageManager.StageEnum.GAME);
        GameController.startGame();
    }
}
