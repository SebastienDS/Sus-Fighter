package fr.uge.susfighter.mvc;

import com.gluonhq.charm.glisten.control.ProgressBar;
import fr.uge.susfighter.mvc.ImageManager.ImageKey;
import fr.uge.susfighter.object.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.util.Locale;


public class GameController {
    @FXML
    public AnchorPane anchorpane;

    @FXML
    private ImageView player1;

    @FXML
    private ImageView player2;

    @FXML
    private ImageView profile1;

    @FXML
    private ImageView profile2;

    @FXML
    private HBox hBoxBar1;

    @FXML
    private HBox hBoxBar2;

    @FXML
    private VBox vBoxBar1;

    @FXML
    private VBox vBoxBar2;

    @FXML
    private ProgressBar playerHp1;

    @FXML
    private ProgressBar playerEnergy1;

    @FXML
    private ProgressBar playerHp2;

    @FXML
    private ProgressBar playerEnergy2;

    @FXML
    private VBox vBoxProfile1;

    @FXML
    private VBox vBoxProfile2;

    @FXML
    private Label time;

    @FXML
    private Label name1;

    @FXML
    private Label name2;

    @FXML
    private ImageView fist1;

    @FXML
    private ImageView fist2;

    private static double SPACING_BAR = 50;
    private static double outline = 4;

    private Timeline timeline;

    @FXML
    public void initialize() {
        setBackGround();
        setPlayers();
        initTimeline();
        initTime();
    }

    private void initTime() {
        AnchorPane.setLeftAnchor(time, StageManager.getWidth() / 2 - time.getPrefWidth() / 2);
        AnchorPane.setTopAnchor(time, 0.0);
    }

    private void setBackGround() {
        var imageView = new ImageView(ImageManager.getImage(ImageKey.FIELD));
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
        setPlayer(player1, fist1, name1, p);
        setBarPlayer(hBoxBar1, vBoxProfile1, vBoxBar1, playerHp1, playerEnergy1, profile1, p, 0);

        p = duel.getPlayer(1);
        AnchorPane.setLeftAnchor(player2, (double) p.getX());
        AnchorPane.setTopAnchor(player2, (double) p.getY());
        setPlayer(player2, fist2, name2, p);
        setBarPlayer(hBoxBar2, vBoxProfile2, vBoxBar2, playerHp2, playerEnergy2, profile2, p, 1);
        flip(playerHp2, playerEnergy2, profile2, player2);
    }

    private void flip(ProgressBar playerHp, ProgressBar playerEnergy, ImageView profile, ImageView player) {
        playerHp.setScaleX(-1);
        playerEnergy.setScaleX(-1);
        profile.setScaleX(-1);
        player.setScaleX(-1);
    }

    private void setBarPlayer(HBox hBoxBar, VBox vBoxProfile, VBox vBox, ProgressBar hpBar, ProgressBar energyBar,
                              ImageView profile, Player p, int sens) {
        var halfWidth = StageManager.getWidth() / 2.0;
        hBoxBar.setPrefWidth(halfWidth - SPACING_BAR);
        AnchorPane.setLeftAnchor(vBoxProfile, sens * (halfWidth * 2 - profile.getFitWidth() - outline * 2));
        var modification = (sens - 1) + (1 * sens);
        AnchorPane.setLeftAnchor(hBoxBar, sens * (halfWidth + SPACING_BAR) - modification * (profile.getFitWidth() + outline));
        hpBar.setPrefWidth(halfWidth - profile.getFitWidth() - SPACING_BAR);
        energyBar.setPrefWidth(hpBar.getPrefWidth() / 3);
        vBox.setPrefWidth(halfWidth - SPACING_BAR);
        profile.setImage(ImageManager.getImage(ImageKey.valueOf("PLAYER_" + p.getNumPlayer() + "_HEAD")));
        hpBar.setProgress(p.percentageHpLeft());
        energyBar.setProgress(p.percentageEnergy());
    }

    private void setPlayer(ImageView imageView, ImageView fist, Label name, Player p) {
        ImageManager.loadImagePlayer(p);
        var player = "PLAYER_" + p.getNumPlayer();
        imageView.setImage(ImageManager.getImage(ImageKey.valueOf(player + "_IDLE")));
        imageView.setFitWidth(p.getWidth());
        imageView.setFitHeight(p.getHeight());
        name.setTextFill(getColorFromName(p.getName()));
        name.setText(p.getName().toUpperCase(Locale.ROOT));
        AnchorPane.setLeftAnchor(name, (p.getNumPlayer() - 1) * (StageManager.getWidth() - name.getPrefWidth()));
        fist.setImage(ImageManager.getImage(ImageKey.valueOf(player + "_FIST")));
        fist.setFitWidth(80);
        fist.setFitHeight(80);
        fist.setVisible(false);
    }

    private Paint getColorFromName(String name){
        return Color.valueOf(name.toUpperCase(Locale.ROOT));
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
        time.setText(String.valueOf(duel.timeLeft()));
        duel.update();
        var p = duel.getPlayer(0);
        var p2 = duel.getPlayer(1);
        updatePlayer(p, p2, player1, fist1, playerHp2);

        updatePlayer(p2, p, player2, fist2, playerHp1);
    }

    private void updatePlayer(Player p, Player p2, ImageView player, ImageView fist, ProgressBar p2Hp) {
        AnchorPane.setLeftAnchor(player, (double) p.getX());
        AnchorPane.setTopAnchor(player, (double) p.getY());
        fist.setVisible(false);
        if(p.isAttacking()){
            attack(p, p2, fist, p2Hp);
        }
        var scaleX = p.isFlipped() ? -1 : 1;
        player.setScaleX(scaleX);
    }

    private void attack(Player p, Player p2, ImageView fist, ProgressBar p2Hp) {
        fist.setVisible(true);
        fist.setScaleX(1);
        if(p.isFlipped()) fist.setScaleX(-1);
        AnchorPane.setLeftAnchor(fist, p.getXFist());
        AnchorPane.setTopAnchor(fist, p.getYFist());
        p.checkAttack(p2);
        p2Hp.setProgress(p2.percentageHpLeft());
    }

    private static void toBack(Node node, AnchorPane pane) {
        for (var i = 0; i < pane.getChildren().size(); i++) {
            node.toBack();
        }
    }

    private static void move(KeyEvent keyEvent) {
        var duel = DuelManager.getDuel();
        duel.pressed(keyEvent);
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