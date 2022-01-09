package fr.uge.susfighter.mvc;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

import fr.uge.susfighter.mvc.ImageManager.ImageKey;


public class MenuController {

    private final Random random = new Random();

    private class Star{
        private final int speed;
        private final Circle star;

        public Star(AnchorPane anchorPane){
            speed = random.nextInt(10) + 1;
            star = new Circle();
            star.setFill(Color.WHITE);
            star.setRadius(random.nextInt() % 5 + 1);
            anchorPane.getChildren().add(star);
            AnchorPane.setLeftAnchor(star, random.nextDouble() * StageManager.getWidth());
            AnchorPane.setTopAnchor(star, random.nextDouble() * StageManager.getHeight());
            star.setVisible(false);
        }

        public void setVisible(boolean bool){
            star.setVisible(bool);
        }

        public void move() {
            AnchorPane.setLeftAnchor(star, AnchorPane.getLeftAnchor(star) + speed / 75.);
            if(AnchorPane.getLeftAnchor(star) > StageManager.getWidth()){
                AnchorPane.setLeftAnchor(star, 0.);
                AnchorPane.setTopAnchor(star, random.nextDouble() * StageManager.getHeight());
            }
        }
    }

    private class Astronaut{
        private int speedX;
        private int speedY;
        private int rotate;
        private final ImageView imageView;

        public Astronaut(String name, ImageKey imageKey){
            imageView = new ImageView();
            ImageManager.loadImage(imageKey, name, widthPlayer, heightPlayer);
            imageView.setImage(ImageManager.getImage(imageKey));
            putInAnchorPane(imageView);
            changeSpeed(5);
        }

        private int getSpeed(int max) {
            var speed = random.nextInt(max * 2) - max;
            return  (speed < 2 && speed > - 2)? max : speed;
        }

        private void putInAnchorPane(ImageView image) {
            anchorPane.getChildren().add(image);
            randomPlacement(image);
            image.setVisible(false);
        }

        private void randomPlacement(ImageView image) {
            var totalWidth = StageManager.getWidth();
            var totalHeight = StageManager.getHeight();
            var randomNumber = random.nextInt(2);
            var x = (randomNumber == 0) ? (speedX < 0)? totalWidth : - widthPlayer :
                    (speedX < 0) ? getPlacement(totalWidth / 2.) : getPlacement(0.);
            var y = (randomNumber == 1) ? (speedY < 0)? totalHeight : - heightPlayer :
                    (speedY < 0) ? getPlacement(totalHeight /2.) : getPlacement(0.);
            AnchorPane.setLeftAnchor(image, x);
            AnchorPane.setTopAnchor(image, y);
        }

        private double getPlacement(double start) {
            return start + (StageManager.getWidth() / 2. * random.nextDouble());
        }

        private void move() {
            AnchorPane.setLeftAnchor(imageView, AnchorPane.getLeftAnchor(imageView) + speedX / 75.);
            AnchorPane.setTopAnchor(imageView, AnchorPane.getTopAnchor(imageView) + speedY / 75.);
            imageView.setRotate(imageView.getRotate() + rotate / 75.);
            if(AnchorPane.getLeftAnchor(imageView) > StageManager.getWidth()
                    || AnchorPane.getLeftAnchor(imageView) < -widthPlayer
                    || AnchorPane.getTopAnchor(imageView) > StageManager.getHeight()
                    || AnchorPane.getTopAnchor(imageView) < - heightPlayer) {
                changeSpeed(5);
                randomPlacement(imageView);
            }
        }

        private void changeSpeed(int max) {
            speedX = getSpeed(max);
            speedY = getSpeed(max);
            rotate = getSpeed(max / 2);
        }

        public void setVisible(boolean b) {
            imageView.setVisible(b);
        }
    }

    @FXML
    private Button campaign;

    @FXML
    private Button credits;

    @FXML
    private Button duel;

    @FXML
    private Button exit;

    @FXML
    private Label click;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private VBox map1;

    @FXML
    private VBox map2;

    @FXML
    private VBox map3;

    @FXML
    private VBox map4;

    @FXML
    private Label name1;

    @FXML
    private Label name2;

    @FXML
    private Label name3;

    @FXML
    private Label name4;

    private Label title;

    private Timeline timeline;

    private Timeline stars;

    private List<Star> starsView;

    private List<Astronaut> astronauts;

    private String mapChosen;

    private int widthPlayer = 150;
    private int heightPlayer = 250;

    private enum Menu{
        CLICK,
        MODE,
        DUEL
    }

    private Menu menu = Menu.CLICK;


    @FXML
    void initialize(){
        initPlacement();
        initFont();
        initStars();
        initAstronauts();
        initTimeline();
        initMaps();
    }

    private void initMaps() {
        var font = Font.loadFont(this.getClass().getResource( "font/font.ttf").toExternalForm(), 50);
        var width = StageManager.getWidth();
        var height = StageManager.getHeight();
        var spacing = (width - map1.getPrefWidth() * 2) / 3;
        initMap(spacing, height / 6., map1, name1, "UwU", font);
        initMap(2 * spacing + map1.getPrefWidth(), height / 6., map2, name2, "Cerisier", font);
        initMap(spacing, 2 * height / 3., map3, name3, "Volcan", font);
        initMap(2 * spacing + map3.getPrefWidth(), 2 * height / 3., map4, name4, "Glacier", font);
    }

    private void initMap(double x, double y, VBox map, Label labelName, String name, Font font) {
        labelName.setText(name);
        labelName.setFont(font);
        AnchorPane.setLeftAnchor(map, x);
        AnchorPane.setTopAnchor(map, y);
    }

    private void initAstronauts() {
        astronauts = new ArrayList<>();
        astronauts.add(new Astronaut("images/character/purple/IDLE.png", ImageKey.PURPLE));
        astronauts.add(new Astronaut("images/character/red/IDLE.png", ImageKey.RED));
        astronauts.add(new Astronaut("images/character/black/IDLE.png", ImageKey.BLACK));
        astronauts.add(new Astronaut("images/character/green/IDLE.png", ImageKey.GREEN));
        astronauts.add(new Astronaut("images/character/yellow/IDLE.png", ImageKey.YELLOW));
        astronauts.add(new Astronaut("images/character/white/IDLE.png", ImageKey.WHITE));
        astronauts.add(new Astronaut("images/character/pink/IDLE.png", ImageKey.PINK));
    }

    private void initStars() {
        starsView = new ArrayList<>();
        for (int i = 0; i < 75; i++) {
            starsView.add(new Star(anchorPane));
        }
    }

    private void initFont() {
        var font = Font.loadFont(this.getClass().getResource( "font/font.ttf").toExternalForm(), 45);
        campaign.setFont(font);
        duel.setFont(font);
        credits.setFont(font);
        exit.setFont(font);
        font = Font.loadFont(this.getClass().getResource( "font/font.ttf").toExternalForm(), 35);
        click.setFont(font);
    }

    private void placeAtCenter(Button button, int y){
        AnchorPane.setLeftAnchor(button, StageManager.getWidth() / 2 - button.getPrefWidth() / 2);
        AnchorPane.setTopAnchor(button, (double)y);
    }

    private void initPlacement() {
        placeAtCenter(campaign, StageManager.getHeight() /3);
        placeAtCenter(duel, StageManager.getHeight() /2);
        placeAtCenter(credits, 2 * StageManager.getHeight() /3);
        placeAtCenter(exit, 5 * StageManager.getHeight() /6);
        AnchorPane.setLeftAnchor(click, StageManager.getWidth() / 2 - click.getPrefWidth() / 2);
        AnchorPane.setTopAnchor(click, StageManager.getHeight() - click.getPrefHeight());
    }

    private void initTimeline() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    click.setVisible(!click.isVisible());
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        stars = new Timeline(
                new KeyFrame(Duration.seconds(0.001), e -> {
                    starsView.forEach(Star::move);
                    astronauts.forEach(Astronaut::move);
                })
        );
        stars.setCycleCount(Timeline.INDEFINITE);
    }


    @FXML
    void campaignMenu() {

    }

    @FXML
    void creditsMenu() {

    }

    @FXML
    void duelMenu() throws IOException {
        //StageManager.setScene(StageManager.StageEnum.GAME);
        //GameController.startGame();
        menu = Menu.DUEL;
        title.setVisible(false);
        buttonsMenuVisible(false);
        buttonMapVisible(true);
        buttonsMapToFront();
    }

    private void initBackground() {
        showTitle();
        anchorPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        stars.play();
        starsView.forEach(star -> star.setVisible(true));
        astronauts.forEach(astronaut -> astronaut.setVisible(true));
    }

    private void showTitle() {
        var fontSize = 150;
        var font = Font.loadFont(this.getClass().getResource( "font/font.ttf").toExternalForm(), fontSize);
        title = new Label("SUS FIGHTER");
        title.setPrefWidth(StageManager.getWidth());
        title.setPrefHeight(fontSize);
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-stroke-width: 1px;");
        title.setFont(font);
        title.alignmentProperty().set(Pos.CENTER);
        anchorPane.getChildren().add(title);
    }

    @FXML
    void exitGame() {
        System.exit(0);
    }

    @FXML
    void choseMap(MouseEvent mouseEvent){
        mapChosen = ((Node)mouseEvent.getSource()).getParent().getId();
        buttonMapVisible(false);
    }

    @FXML
    void openMenu(MouseEvent event){
        if(menu == Menu.CLICK){
            buttonsMenuToFront();
            buttonsMenuVisible(true);
            click.setVisible(false);
            timeline.stop();
            menu = Menu.MODE;
            initBackground();

        }
    }

    private void buttonsMenuToFront() {
        var size = anchorPane.getChildren().size();
        for (int i = 0; i < size; i++) {
            exit.toFront();
            credits.toFront();
            duel.toFront();
            campaign.toFront();
        }
    }

    private void buttonsMenuVisible(boolean bool){
        campaign.setVisible(bool);
        duel.setVisible(bool);
        credits.setVisible(bool);
        exit.setVisible(bool);
    }

    private void buttonMapVisible(boolean b) {
        map1.setVisible(b);
        map2.setVisible(b);
        map3.setVisible(b);
        map4.setVisible(b);
    }

    private void buttonsMapToFront() {
        var size = anchorPane.getChildren().size();
        for (int i = 0; i < size; i++) {
            map1.toFront();
            map2.toFront();
            map3.toFront();
            map4.toFront();
        }
    }
}
