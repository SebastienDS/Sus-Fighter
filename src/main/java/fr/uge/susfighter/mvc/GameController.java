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

/**
 * This method manage all control over element in the duel
 */
public class GameController {
    /**
     * Container of all elements
     */
    @FXML
    public AnchorPane anchorpane;

    /**
     * Image view of the first player
     */
    @FXML
    private ImageView player1;

    /**
     * Image view of the second player
     */
    @FXML
    private ImageView player2;

    /**
     * Image view of the head of the first player
     */
    @FXML
    private ImageView profile1;

    /**
     * Image viw of the head of the second player
     */
    @FXML
    private ImageView profile2;

    /**
     * HBox containing profile of player 1 and his bar (mana and hp)
     */
    @FXML
    private HBox hBoxBar1;

    /**
     * HBox containing profile of player 2 and his bar (mana and hp)
     */
    @FXML
    private HBox hBoxBar2;

    /**
     * VBox containing mana and health bar of player 1
     */
    @FXML
    private VBox vBoxBar1;

    /**
     * VBox containing mana and health bar of player 2
     */
    @FXML
    private VBox vBoxBar2;

    /**
     * Progress bar representing player 1 hp
     */
    @FXML
    private ProgressBar playerHp1;

    /**
     * Progress bar representing player 1 energy
     */
    @FXML
    private ProgressBar playerEnergy1;

    /**
     * Progress bar representing player 2 hp
     */
    @FXML
    private ProgressBar playerHp2;

    /**
     * Progress bar representing player 2 energy
     */
    @FXML
    private ProgressBar playerEnergy2;

    /**
     * VBox containing profile of player 1 to add an outline
     */
    @FXML
    private VBox vBoxProfile1;

    /**
     * VBox containing profile of player 2 to add an outline
     */
    @FXML
    private VBox vBoxProfile2;

    /**
     * Label to show time left of duel
     */
    @FXML
    private Label time;

    /**
     * Label to show name of player 1
     */
    @FXML
    private Label name1;

    /**
     * Label to show name of player 2
     */
    @FXML
    private Label name2;

    /**
     * ImageView of the fist of the player 1
     */
    @FXML
    private ImageView fist1;

    /**
     * ImageView of the fist of the player 2
     */
    @FXML
    private ImageView fist2;

    /**
     * ImageView of the ultimate of the player 1
     */
    @FXML
    private ImageView ultimate1;

    /**
     * ImageView of the ultimate of the player 2
     */
    @FXML
    private ImageView ultimate2;

    /**
     * VBox containing all button of escape menu (resume, music, leave)
     */
    @FXML
    private VBox menu;

    /**
     * Button to turn ON/OFF the music
     */
    @FXML
    private Button musicLabel;

    /**
     * Constant for spacing of bar
     */
    private static final double SPACING_BAR = 50;
    /**
     * Constant for outline of bar
     */
    private static final double outline = 4;

    /**
     * Timeline to see if game ended
     */
    private Timeline timeline;

    /**
     * Time when pause started
     */
    private static long START_ESCAPE;

    /**
     * Initialise all elements of game graphics
     */
    @FXML
    public void initialize() {
        setBackGround();
        setPlayers();
        initTimeline();
        initTime();
        initMenu();
    }

    /**
     * Resume game when game was paused
     */
    @FXML
    private void resumeGame(){
        resume(DuelManager.getDuel());
    }

    /**
     * Leave game by returning to starting menu
     * @throws IOException if an I/O error occur
     */
    @FXML
    private void leaveGame() throws IOException {
        StageManager.setScene(StageManager.StageEnum.MENU);
        MediaPlayerManager.setSound(new Media(
                Objects.requireNonNull(this.getClass().getResource("sounds/menuMusic.mp3")).toExternalForm()
        ));
    }

    /**
     * Turn ON/OFF music
     */
    @FXML
    private void musicVolume(){
        MediaPlayerManager.swapVolume();
        var mode = (MediaPlayerManager.isMuted())? "OFF": "ON";
        musicLabel.setText("MUSIC: " + mode);
    }

    /**
     * Initialise all element of escape menu (placement and text)
     */
    private void initMenu() {
        AnchorPane.setLeftAnchor(menu, StageManager.getWidth() /2. - menu.getPrefWidth() / 2);
        AnchorPane.setTopAnchor(menu, StageManager.getStage().getHeight() /2. - menu.getPrefHeight() / 2);
        var name = (MediaPlayerManager.isMuted())? "OFF" : "ON";
        musicLabel.setText("MUSIC: " + name);
    }

    /**
     * Initialise placement of time
     */
    private void initTime() {
        AnchorPane.setLeftAnchor(time, StageManager.getWidth() / 2 - time.getPrefWidth() / 2);
        AnchorPane.setTopAnchor(time, 0.0);
    }

    /**
     * Initialise background of map
     */
    private void setBackGround() {
        var imageView = new ImageView(ImageManager.getImage(ImageKey.FIELD));
        imageView.setFitWidth(StageManager.getWidth());
        imageView.setFitHeight(StageManager.getHeight());
        anchorpane.getChildren().add(imageView);
        toBack(imageView, anchorpane);
    }

    /**
     * Initialise all element of players (position of image view of player, bar and profile and flip of image)
     */
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

    /**
     * flip all elements in parameter
     * @param playerHp ProgressBar of hp of player
     * @param playerEnergy ProgressBar of energy of player
     * @param profile profile of player
     * @param player image of player
     */
    private void flip(ProgressBar playerHp, ProgressBar playerEnergy, ImageView profile, ImageView player) {
        playerHp.setScaleX(-1);
        playerEnergy.setScaleX(-1);
        profile.setScaleX(-1);
        player.setScaleX(-1);
    }

    /**
     * Initialise all information of player (hp bar, energy bar, profile, name).
     * @param hBoxBar HBox containing all bar of player
     * @param vBoxProfile vbox of profile picture of player
     * @param vBox vbox containing all bar
     * @param hpBar Progress bar of hp of player
     * @param energyBar Progress bar of energy of player
     * @param profile ImageView of profile of player
     * @param p Fighter of player
     * @param sens (for flip (0 no flip / 1 flip))
     */
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

    /**
     * Set all images and names of player
     * @param imageView image of the player
     * @param fist image of the fist of the player
     * @param ultimate image of the ultimate of the player
     * @param name Label of the name of the player
     * @param p fighter of player
     */
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

    /**
     * Get color to draw for player name
     * @param p Fighter of player
     * @return color of name of player
     */
    private Paint getColor(Fighter p) {
        var element = p.getElement();
        if(element == Element.WATER) return Color.BLUE;
        if(element == Element.DARK) return Color.PURPLE;
        if(element == Element.EARTH) return Color.BROWN;
        if(element == Element.FIRE) return Color.RED;
        if(element == Element.LIGHT) return Color.YELLOW;
        return Color.GREEN;
    }

    /**
     * set image of fist with width and height given and make it invisible.
     * @param fist fist of the player
     * @param player name of player
     * @param width width of fist
     * @param height height of fist
     */
    private void setFist(ImageView fist, String player, double width, double height) {
        fist.setImage(ImageManager.getImage(ImageKey.valueOf(player + "_FIST")));
        fist.setFitWidth(width);
        fist.setFitHeight(height);
        fist.setVisible(false);
    }

    /**
     * Initialise timeline that update data and make a verification if game is finish.
     */
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

    /**
     * Update the data and placement of all images. Change progress bar in function of data and return true if game is
     * finished
     * @return
     */
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

    /**
     * Update all information of player
     * @param p Fighter of current player
     * @param p2 Fighter of other player
     * @param player Image of player
     * @param fist fist of player
     * @param ultimate ultimate of player
     * @param pHp progress bar of hp of player
     * @param pEnergy progress bar of energy of player
     */
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

    /**
     * Set the fist in the good direction to make a blocking effect
     * @param p Fighter of player
     * @param fist fist of player
     */
    private void block(Fighter p, ImageView fist) {
        var flip = p.isFlipped() ? -1 : 1;
        fist.setVisible(true);
        fist.setScaleX(flip);

        var blockRect = p.getBlockHitBox();
        AnchorPane.setLeftAnchor(fist, blockRect.getX());
        AnchorPane.setTopAnchor(fist, blockRect.getY());

        fist.setRotate(blockRect.getRotate());
    }

    /**
     * Manage attack of player by making the fist visible and placing it.
     * @param p fighter of player
     * @param p2 fighter of other player
     * @param fist fist of player
     */
    private void attack(Fighter p, Fighter p2, ImageView fist) {
        fist.setVisible(true);
        fist.setScaleX(1);
        if(p.isFlipped()) fist.setScaleX(-1);

        var attackRect = p.getAttack();
        AnchorPane.setLeftAnchor(fist, attackRect.getX());
        AnchorPane.setTopAnchor(fist, attackRect.getY());
        p.interact(p2);
    }

    /**
     * Make the node go in the first position to show so every node will be above it
     * @param node to put in the first position
     * @param pane container of the data
     */
    public static void toBack(Node node, AnchorPane pane) {
        for (var i = 0; i < pane.getChildren().size(); i++) {
            node.toBack();
        }
    }

    /**
     * Static method to manage key pressed in duel mode
     * @param keyEvent event of the key pressed
     */
    private static void keyPressedHandler(KeyEvent keyEvent) {
        var duel = DuelManager.getDuel();
        var haveChanged = false;
        if(keyEvent.getCode() == KeyCode.ESCAPE) haveChanged = true;
        if(haveChanged){
            resume(duel);
        }
        duel.pressed(keyEvent);
    }

    /**
     * Resume activity of duel (end pause)
     * @param duel current duel
     */
    private static void resume(Duel duel) {
        duel.swapPause();
        duel.manageTime(START_ESCAPE, System.currentTimeMillis());
        START_ESCAPE = System.currentTimeMillis();
    }

    /**
     * Static method to manage key released in duel mode
     * @param keyEvent event of the key released
     */
    private static void release(KeyEvent keyEvent) {
        var duel = DuelManager.getDuel();
        duel.release(keyEvent);
    }

    /**
     * start the game by setting up the player event handler
     */
    public static void startGame() {
        var scene = StageManager.getScene();
        scene.setOnKeyPressed(GameController::keyPressedHandler);
        scene.setOnKeyReleased(GameController::release);
    }


}