package fr.uge.susfighter.mvc;

import com.gluonhq.charm.glisten.control.ProgressBar;
import fr.uge.susfighter.mvc.ImageManager.ImageKey;
import fr.uge.susfighter.object.Element;
import fr.uge.susfighter.object.Fighter;
import fr.uge.susfighter.object.Player;
import fr.uge.susfighter.object.Save;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;


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

    @FXML
    private ImageView ultimate1;

    @FXML
    private ImageView ultimate2;

    @FXML
    private VBox menu;

    @FXML
    private Button musicLabel;

    private static final double SPACING_BAR = 50;
    private static final double outline = 4;

    private Timeline timeline;

    private static long START_ESCAPE;

    @FXML
    public void initialize() {
        setBackGround();
        setPlayers();
        initTimeline();
        initTime();
        initMenu();
    }

    @FXML
    private void resumeGame(){
        resume(DuelManager.getDuel());
    }

    @FXML
    private void leaveGame() throws IOException {
        StageManager.setScene(StageManager.StageEnum.MENU);
        MediaPlayerManager.setSound(new Media(
                Objects.requireNonNull(this.getClass().getResource("sounds/menuMusic.mp3")).toExternalForm()
        ));
    }

    @FXML
    private void musicVolume(){
        MediaPlayerManager.swapVolume();
        var mode = (MediaPlayerManager.isMuted())? "OFF": "ON";
        musicLabel.setText("MUSIC: " + mode);
    }

    private void initMenu() {
        AnchorPane.setLeftAnchor(menu, StageManager.getWidth() /2. - menu.getPrefWidth() / 2);
        AnchorPane.setTopAnchor(menu, StageManager.getStage().getHeight() /2. - menu.getPrefHeight() / 2);
        var name = (MediaPlayerManager.isMuted())? "OFF" : "ON";
        musicLabel.setText("MUSIC: " + name);
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
        AnchorPane.setLeftAnchor(player1, p.getBody().getX());
        AnchorPane.setTopAnchor(player1, p.getBody().getY());
        setPlayer(player1, fist1, ultimate1, name1, p);
        setBarPlayer(hBoxBar1, vBoxProfile1, vBoxBar1, playerHp1, playerEnergy1, profile1, p, 0);

        p = duel.getPlayer(1);
        AnchorPane.setLeftAnchor(player2, p.getBody().getX());
        AnchorPane.setTopAnchor(player2, p.getBody().getY());
        setPlayer(player2, fist2, ultimate2, name2, p);
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
                              ImageView profile, Fighter p, int sens) {
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

    private void setPlayer(ImageView imageView, ImageView fist, ImageView ultimate, Label name, Fighter p) {
        ImageManager.loadImagePlayer(p);
        var player = "PLAYER_" + p.getNumPlayer();
        imageView.setImage(ImageManager.getImage(ImageKey.valueOf(player + "_IDLE")));
        imageView.setFitWidth(p.getBody().getWidth());
        imageView.setFitHeight(p.getBody().getHeight());
        name.setTextFill(getColor(p));
        name.setText(p.getName().toUpperCase(Locale.ROOT));
        AnchorPane.setLeftAnchor(name, (p.getNumPlayer() - 1) * (StageManager.getWidth() - name.getPrefWidth()));

        var attackRect = p.getAttack();
        var ultimateRect = p.getUltimate();
        setFist(fist, player, attackRect.getWidth(), attackRect.getHeight());
        setFist(ultimate, player, ultimateRect.getWidth(), ultimateRect.getHeight());
        ultimate.setVisible(true);
    }

    private Paint getColor(Fighter p) {
        var element = p.getElement();
        if(element == Element.WATER) return Color.BLUE;
        if(element == Element.DARK) return Color.PURPLE;
        if(element == Element.EARTH) return Color.BROWN;
        if(element == Element.FIRE) return Color.RED;
        if(element == Element.LIGHT) return Color.YELLOW;
        return Color.GREEN;
    }

    private void setFist(ImageView fist, String player, double width, double height) {
        fist.setImage(ImageManager.getImage(ImageKey.valueOf(player + "_FIST")));
        fist.setFitWidth(width);
        fist.setFitHeight(height);
        fist.setVisible(false);
    }

    private void initTimeline() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.01), e -> {
                    var stop = update();

                    if (stop) {
                        timeline.stop();
                        try {
                            StageManager.setScene(StageManager.StageEnum.WINNER);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private boolean update() {
        var duel = DuelManager.getDuel();
        if(duel.isPaused()){
            menu.setVisible(true);
            return false;
        }
        menu.setVisible(false);
        time.setText(String.valueOf(duel.timeLeft()));
        duel.update();
        var p = duel.getPlayer(0);
        var p2 = duel.getPlayer(1);
        updatePlayer(p, p2, player1, fist1, ultimate1, playerHp1, playerEnergy1);
        updatePlayer(p2, p, player2, fist2, ultimate2, playerHp2, playerEnergy2);

        var winner = duel.getWinner();
        if (winner == null) return false;

        if (winner.equals(p)) {
            var level = duel.getLevel();
            var step = duel.getStep();

            if (level != -1 && Save.getCampaignLevel() == level && Save.getCampaignStep() == step) {
                if (step < 2) {
                    Save.setCampaignStep(step + 1);
                } else {
                    Save.setCampaignLevel(level + 1);
                    Save.setCampaignStep(0);
                    Save.unlockCharacter(p2.getName());
                }
            }
        }
        return true;
    }

    private void updatePlayer(Fighter p, Fighter p2, ImageView player, ImageView fist, ImageView ultimate,
                              ProgressBar pHp, ProgressBar pEnergy) {
        AnchorPane.setLeftAnchor(player, p.getBody().getX());
        AnchorPane.setTopAnchor(player, p.getBody().getY());
        fist.setVisible(false);
        var flip = p.isFlipped() ? -1 : 1;

        if (p.isAttacking()) {
            fist.setRotate(p.getAttack().getRotate() * flip);
            attack(p, p2, fist);
        }
        else if (p.isBlocking()) {
            block(p, fist);
        }

        player.setScaleX(flip);

        pHp.setProgress(p.percentageHpLeft());
        pEnergy.setProgress(p.percentageEnergy());

        var box = p.getUltimate();
        AnchorPane.setLeftAnchor(ultimate, box.getX());
        AnchorPane.setTopAnchor(ultimate, box.getY());
        ultimate.setScaleX(box.getScaleX());
    }

    private void block(Fighter p, ImageView fist) {
        var flip = p.isFlipped() ? -1 : 1;
        fist.setVisible(true);
        fist.setScaleX(flip);

        var blockRect = p.getBlockHitBox();
        AnchorPane.setLeftAnchor(fist, blockRect.getX());
        AnchorPane.setTopAnchor(fist, blockRect.getY());

        fist.setRotate(blockRect.getRotate());
    }

    private void attack(Fighter p, Fighter p2, ImageView fist) {
        fist.setVisible(true);
        fist.setScaleX(1);
        if(p.isFlipped()) fist.setScaleX(-1);

        var attackRect = p.getAttack();
        AnchorPane.setLeftAnchor(fist, attackRect.getX());
        AnchorPane.setTopAnchor(fist, attackRect.getY());
        p.interact(p2);
    }

    public static void toBack(Node node, AnchorPane pane) {
        for (var i = 0; i < pane.getChildren().size(); i++) {
            node.toBack();
        }
    }

    private static void keyPressedHandler(KeyEvent keyEvent) {
        var duel = DuelManager.getDuel();
        var haveChanged = false;
        if(keyEvent.getCode() == KeyCode.ESCAPE) haveChanged = true;
        if(haveChanged){
            resume(duel);
        }
        duel.pressed(keyEvent);
    }

    private static void resume(Duel duel) {
        duel.swapPause();
        duel.manageTime(START_ESCAPE, System.currentTimeMillis());
        START_ESCAPE = System.currentTimeMillis();
    }

    private static void release(KeyEvent keyEvent) {
        var duel = DuelManager.getDuel();
        duel.release(keyEvent);
    }

    public static void startGame() {
        var scene = StageManager.getScene();
        scene.setOnKeyPressed(GameController::keyPressedHandler);
        scene.setOnKeyReleased(GameController::release);
    }


}