package fr.uge.susfighter.mvc;

import fr.uge.susfighter.mvc.ImageManager.ImageKey;
import fr.uge.susfighter.object.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;


public class GameController {
    @FXML
    public AnchorPane anchorpane;

    @FXML
    private ImageView player1;

    @FXML
    private ImageView player2;

    private Timeline timeline;

    @FXML
    public void initialize() {
        setBackGround();
        setPlayers();

        initTimeline();
    }

    private void setBackGround() {
        ImageManager.loadImage(ImageKey.FIELD, "images/map/UwU.jpg");
        ImageView imageView = new ImageView(ImageManager.getImage(ImageKey.FIELD));
        imageView.setFitWidth(StageManager.getWidth());
        imageView.setFitHeight(StageManager.getHeight());
        anchorpane.getChildren().add(imageView);

        toBack(imageView, anchorpane);
    }

    private void setPlayers() {
        var duel = DuelManager.getDuel();
        var p = duel.getPlayer(0);
        AnchorPane.setLeftAnchor(player1, (double) p.getX());
        AnchorPane.setTopAnchor(player1, (double) p.getY());
        player1.setFitWidth(p.getWidth());
        player1.setFitHeight(p.getHeight());
        setPlayer(player1, "purple", 1);

        p = duel.getPlayer(1);
        AnchorPane.setLeftAnchor(player2, (double) p.getX());
        AnchorPane.setTopAnchor(player2, (double) p.getY());
        player2.setFitWidth(p.getWidth());
        player2.setFitHeight(p.getHeight());
        setPlayer(player2, "red", 2);
        player2.setScaleX(-1);
    }

    private void setPlayer(ImageView imageView, String name, int numPlayer) {
        ImageManager.loadImagePlayer(name, numPlayer);

        var player = "PLAYER_" + numPlayer;
        imageView.setImage(ImageManager.getImage(ImageKey.valueOf(player + "_IDLE")));
    }

    private void initTimeline() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.01), e -> {
                    update();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void update() {
        var duel = DuelManager.getDuel();
        var p = duel.getPlayer(0);
        updatePlayer(p, player1);

        p = duel.getPlayer(1);
        updatePlayer(p, player2);
    }

    private void updatePlayer(Player p, ImageView player) {
        AnchorPane.setLeftAnchor(player, (double) p.getX());
        AnchorPane.setTopAnchor(player, (double) p.getY());

        var scaleX = p.isFlipped() ? -1 : 1;
        player.setScaleX(scaleX);
    }

    private static void toBack(Node node, AnchorPane pane) {
        for (var i = 0; i < pane.getChildren().size(); i++) {
            node.toBack();
        }
    }

    private static void move(KeyEvent keyEvent) {
        var duel = DuelManager.getDuel();
        duel.update(keyEvent);
    }

    private static void release(KeyEvent keyEvent) {
        var duel = DuelManager.getDuel();
        duel.release(keyEvent);
    }

    public static void startGame() {
        var scene = StageManager.getScene();
        scene.setOnKeyPressed(GameController::move);
        scene.setOnKeyReleased(GameController::release);
    }


}