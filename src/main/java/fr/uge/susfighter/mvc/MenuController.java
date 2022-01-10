package fr.uge.susfighter.mvc;

import fr.uge.susfighter.mvc.ImageManager.ImageKey;
import fr.uge.susfighter.object.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


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

        public Astronaut(){
            imageView = new ImageView();
            var image = astronautsImage.remove(random.nextInt(astronautsImage.size()));
            imageView.setFitWidth(image.getWidth());
            imageView.setFitHeight(image.getHeight());
            imageView.setImage(image);
            putInAnchorPane(imageView);
            changeSpeed();
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
                changeSpeed();
                randomPlacement(imageView);
                astronautsImage.add(imageView.getImage());
                var image = astronautsImage.remove(random.nextInt(astronautsImage.size()));
                imageView.setFitWidth(image.getWidth());
                imageView.setFitHeight(image.getHeight());
                imageView.setImage(image);
            }
        }

        private void changeSpeed() {
            speedX = getSpeed(5);
            speedY = getSpeed(5);
            rotate = getSpeed(5 / 2);
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

    @FXML
    private VBox black1;

    @FXML
    private VBox black2;

    @FXML
    private VBox green1;

    @FXML
    private VBox green2;

    @FXML
    private VBox pink1;

    @FXML
    private VBox pink2;

    @FXML
    private VBox purple1;

    @FXML
    private VBox purple2;

    @FXML
    private VBox red1;

    @FXML
    private VBox red2;

    @FXML
    private VBox white1;

    @FXML
    private VBox white2;

    @FXML
    private VBox yellow1;

    @FXML
    private VBox yellow2;

    @FXML
    private VBox select1;

    @FXML
    private VBox select2;

    @FXML
    private Line line;

    @FXML
    private Button confirmPlayer1;

    @FXML
    private Button confirmPlayer2;

    @FXML
    private Label ready1;

    @FXML
    private Label ready2;
    
    @FXML
    private Polygon back;

    @FXML
    private Label creditsTitle;

    @FXML
    private VBox sebastien;

    @FXML
    private VBox quentin;

    @FXML
    private VBox yoan;

    @FXML
    private Label nameSeb;

    @FXML
    private Label nameQuentin;

    @FXML
    private Label nameYoyo;

    @FXML
    private Button addPage1;

    @FXML
    private Button addPage2;

    private final static int NUMBER_CASE = 7;

    private Rectangle rectangle1;

    private Rectangle rectangle2;

    private String nameSelect1;

    private String nameSelect2;

    private Label title;

    private Timeline timeline;

    private Timeline stars;

    private List<Star> starsView;

    private List<Astronaut> astronauts;

    private String mapChosen;

    private Rectangle confirmRectangle1;

    private Rectangle confirmRectangle2;

    private List<VBox> heads1;

    private List<VBox> heads2;

    private List<Image> images;
    private List<Image> astronautsImage;
    private List<Image> heads;
    private int page1 = 0;
    private int page2 = 0;

    private final int widthPlayer = 150;
    private final int heightPlayer = 250;

    private enum Menu{
        CLICK,
        MODE,
        MAP,
        CHARACTER,
        CREDITS,
        CAMPAIGN
    }

    private Menu menu = Menu.CLICK;


    @FXML
    void initialize() throws IOException, URISyntaxException {
        showTitle();
        initPlacement();
        initFont();
        initStars();
        initAstronauts();
        initTimeline();
        initMaps();
        initCharacterMenu();
        initCredits();
    }

    private void initCredits() {
        var font = Font.loadFont(Objects.requireNonNull(this.getClass().getResource("font/font.ttf")).toExternalForm(), 150);
        var font2 = Font.loadFont(Objects.requireNonNull(this.getClass().getResource("font/font.ttf")).toExternalForm(), 35);
        creditsTitle.setFont(font);
        AnchorPane.setLeftAnchor(creditsTitle, StageManager.getWidth() / 2. - creditsTitle.getPrefWidth() / 2);
        AnchorPane.setTopAnchor(creditsTitle, StageManager.getHeight() / 20.);
        double spacingX = (StageManager.getWidth() - sebastien.getPrefWidth() - quentin.getPrefWidth() - yoan.getPrefHeight()) / 4;
        initAvatar(sebastien, nameSeb, spacingX, StageManager.getHeight() /2. - quentin.getPrefHeight(), font2);
        initAvatar(quentin, nameQuentin, 2 * spacingX + sebastien.getPrefWidth(),
                StageManager.getHeight() / 2. - quentin.getPrefHeight() , font2);
        initAvatar(yoan, nameYoyo, 3 * spacingX + sebastien.getPrefWidth() * 2,
                StageManager.getHeight() / 2. - quentin.getPrefHeight() , font2);
    }

    private void initAvatar(VBox vBox, Label name, double x, double y, Font font) {
        AnchorPane.setLeftAnchor(vBox, x);
        AnchorPane.setBottomAnchor(vBox, y);
        name.setFont(font);
    }

    private void initCharacterMenu() {
        initSelectPlayer();
        placeVBoxPlayer(0, black1, white1, pink1, purple1, red1, green1, yellow1, 1);
        placeVBoxPlayer(StageManager.getWidth() / 2, black2, white2, pink2, purple2, red2, green2, yellow2, 2);
        heads1 = List.of(black1, white1, pink1, purple1, red1, green1, yellow1);
        heads2 = List.of(black2, white2, pink2, purple2, red2, green2, yellow2);
        initLine();
        var y = ((StageManager.getHeight() / 2.) - select1.getPrefHeight()) / 2;
        placeVBox(select1, StageManager.getWidth() / 4. - select1.getPrefWidth() / 2., y, images.get(0), "select1");
        placeVBox(select2, 3 * StageManager.getWidth() / 4. - select1.getPrefWidth() / 2., y, images.get(1), "select2");
        nameSelect1 = black1.getId();
        nameSelect2 = white2.getId();
        addDisable(List.of(black2, white1));
        setRectangles();
        initButtonsAndConfirm();
    }

    private void initLine() {
        AnchorPane.setLeftAnchor(line, StageManager.getWidth() / 2. - line.getStrokeWidth() / 2);
        AnchorPane.setTopAnchor(line, 0.);
        line.setStartX(StageManager.getWidth() / 2. - line.getStrokeWidth() / 2);
        line.setStartY(0);
        line.setEndX(StageManager.getWidth() / 2. - line.getStrokeWidth() / 2);
        line.setEndY(StageManager.getHeight());
    }

    private void initButtonsAndConfirm() {
        var font = Font.loadFont(Objects.requireNonNull(this.getClass().getResource("font/font.ttf")).toExternalForm(), 50);
        var font2 = Font.loadFont(Objects.requireNonNull(this.getClass().getResource("font/font.ttf")).toExternalForm(), 30);
        var spacingX = (StageManager.getWidth() / 2.0 - (white1.getPrefWidth() * 3)) / 4;
        var spacingY = (StageManager.getHeight() / 2.0 - (white1.getPrefHeight() * 3)) / 4;
        confirmRectangle1 = initConfirm(0, line.getStrokeWidth(), ready1, font);
        confirmRectangle2 = initConfirm(StageManager.getWidth() / 2., -line.getStrokeWidth(), ready2, font);
        initButton(confirmPlayer1, 0, font2, spacingX, spacingY, black1.getPrefWidth(), black1.getPrefHeight());
        initButton(confirmPlayer2, StageManager.getWidth() / 2, font2, spacingX, spacingY, black1.getPrefWidth(), black1.getPrefHeight());
        initButtonPage(addPage1, font2, 0, spacingX, spacingY);
        initButtonPage(addPage2, font2, StageManager.getWidth() / 2, spacingX, spacingY);
    }

    private void initButtonPage(Button addPage, Font font, int x, double spacingX, double spacingY) {
        addPage.setFont(font);
        AnchorPane.setLeftAnchor(addPage, x + spacingX);
        AnchorPane.setTopAnchor(addPage, StageManager.getHeight() / 2 + spacingY * 3 + black1.getPrefHeight() * 2);
    }

    private Rectangle initConfirm(double x, double stroke, Label ready, Font font) {
        var rectangle = new Rectangle(x + StageManager.getWidth() / 2. - stroke / 2, StageManager.getHeight());
        anchorPane.getChildren().add(rectangle);
        rectangle.setFill(Color.BLACK);
        rectangle.setOpacity(0.4);
        AnchorPane.setLeftAnchor(rectangle, x);
        rectangle.setVisible(false);
        AnchorPane.setLeftAnchor(ready, x + StageManager.getWidth() / 4 - ready.getPrefWidth() / 2);
        AnchorPane.setTopAnchor(ready, StageManager.getHeight() / 2 - ready.getPrefHeight() / 2);
        ready.setFont(font);
        toFront(ready, anchorPane);
        return rectangle;
    }

    private void initButton(Button button, int x, Font font, double spacingX, double spacingY, double width, double height) {
        button.setText("OK");
        button.setFont(font);
        AnchorPane.setLeftAnchor(button, x + spacingX * 3 + width * 2);
        AnchorPane.setTopAnchor(button, StageManager.getHeight() / 2 + spacingY * 3 + height * 2);
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        toFront(button, anchorPane);
    }

    private void setRectangles() {
        var spacingX = (StageManager.getWidth() / 2.0 - (white1.getPrefWidth() * 3)) / 4;
        var spacingY = StageManager.getHeight() / 2 + (StageManager.getHeight() / 2.0 - (white1.getPrefHeight() * 3)) / 4;
        rectangle1 = new Rectangle(black1.getPrefWidth(), black1.getPrefHeight());
        rectangle2 = new Rectangle(white2.getPrefWidth(), white2.getPrefHeight());
        manageRectangle(rectangle1, spacingX, spacingY);
        manageRectangle(rectangle2, StageManager.getWidth() / 2. + 2 * spacingX + white1.getPrefWidth(), spacingY);
    }

    private void manageRectangle(Rectangle rectangle, double x, double y) {
        anchorPane.getChildren().add(rectangle);
        rectangle.setStroke(Color.ORANGE);
        AnchorPane.setLeftAnchor(rectangle, x);
        AnchorPane.setTopAnchor(rectangle, y);
        rectangle.setStrokeWidth(7);
        rectangle.setArcHeight(5);
        rectangle.setArcWidth(5);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setVisible(false);
    }

    private void addDisable(List<Node> nodes) {
        nodes.forEach(node -> node.setDisable(true));
    }

    private void initSelectPlayer() {
        ((ImageView)select1.getChildren().get(0)).setImage(images.get(0));
        ((ImageView)select2.getChildren().get(0)).setImage(images.get(1));
    }

    private void placeVBoxPlayer(int x, VBox black, VBox white, VBox pink, VBox purple, VBox red,
                                 VBox green, VBox yellow, int numPlayer) {
        var width = StageManager.getWidth();
        var widthVBox = green1.getPrefWidth();
        var heightVBox = green1.getPrefHeight();
        var height = StageManager.getHeight();
        var spacingX = (width / 2.0 - (widthVBox * 3)) / 4;
        var spacingY = (height / 2.0 - (heightVBox * 3)) / 4;
        placeVBox(black, x + spacingX, height / 2. + spacingY, heads.get(0), "black" + numPlayer  + 0);
        placeVBox(white, x + spacingX * 2 + widthVBox, height / 2. + spacingY, heads.get(1), "green" + numPlayer + 1);
        placeVBox(pink, x + spacingX * 3 + widthVBox * 2, height / 2. + spacingY, heads.get(2), "pink" + numPlayer + 2);
        placeVBox(purple, x + spacingX, height / 2. + 2 * spacingY + heightVBox, heads.get(3), "purple" + numPlayer + 3);
        placeVBox(red, x + spacingX * 2 + widthVBox, height / 2. + 2 * spacingY + heightVBox, heads.get(4), "red" + numPlayer + 4);
        placeVBox(green, x + spacingX * 3 + widthVBox * 2, height / 2. + 2 * spacingY + heightVBox, heads.get(5), "white" + numPlayer + 5);
        placeVBox(yellow, x + spacingX * 2 + widthVBox, height / 2. + 3 * spacingY + 2 * heightVBox, heads.get(6), "yellow" + numPlayer + 7);
    }

    private void placeVBox(VBox vBox, double x, double y, Image image, String id) {
        AnchorPane.setLeftAnchor(vBox, x);
        AnchorPane.setTopAnchor(vBox, y);
        var imageView = (ImageView)(vBox.getChildren().get(0));
        imageView.setImage(image);
        vBox.setId(id);
    }

    private static void toFront(Node node, AnchorPane pane){
        for (int i = 0; i < pane.getChildren().size(); i++) {
            node.toFront();
        }
    }

    private void initMaps() {
        var font = Font.loadFont(Objects.requireNonNull(
                this.getClass().getResource("font/font.ttf")).toExternalForm(), 50);
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

    private void initAstronauts() throws IOException, URISyntaxException {
        loadAllAvatar();
        astronauts = new ArrayList<>();
        astronauts.add(new Astronaut());
        astronauts.add(new Astronaut());
        astronauts.add(new Astronaut());
        astronauts.add(new Astronaut());
        astronauts.add(new Astronaut());
        astronauts.add(new Astronaut());
        astronauts.add(new Astronaut());
        astronauts.forEach(astronaut -> GameController.toBack(astronaut.imageView, anchorPane));
    }

    private void loadAllAvatar() throws IOException, URISyntaxException {
        images = new ArrayList<>();
        astronautsImage = new ArrayList<>();
        heads = new ArrayList<>();
        var paths = Files.walk(Path.of(Objects.requireNonNull(this.getClass()
                .getResource("images/character")).toURI()), 3)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
        for (var path : paths) {
            if(path.getFileName().toString().equals("IDLE.png")){
                astronautsImage.add(ImageManager.loadImage("file:///" + path));
            }
            if(path.getFileName().toString().equals("HEAD.png")){
                heads.add(ImageManager.loadImage("file:///" + path));
            }
        }
        images = List.copyOf(astronautsImage);
    }

    private void initStars() {
        starsView = new ArrayList<>();
        for (int i = 0; i < 75; i++) {
            starsView.add(new Star(anchorPane));
        }
        starsView.forEach(star -> GameController.toBack(star.star, anchorPane));
    }

    private void initFont() {
        var font = Font.loadFont(Objects.requireNonNull(this.getClass().getResource("font/font.ttf")).toExternalForm(), 45);
        campaign.setFont(font);
        duel.setFont(font);
        credits.setFont(font);
        exit.setFont(font);
        font = Font.loadFont(Objects.requireNonNull(this.getClass().getResource("font/font.ttf")).toExternalForm(), 35);
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
                new KeyFrame(Duration.seconds(1), e -> click.setVisible(!click.isVisible()))
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
        menu = Menu.CREDITS;
        creditsVisible(true);
        back.setVisible(true);
        buttonsMenuVisible(false);
    }

    @FXML
    void duelMenu() {
        menu = Menu.MAP;
        buttonsMenuVisible(false);
        buttonMapVisible(true);
        back.setVisible(true);
    }

    @FXML
    void startGame(MouseEvent event) throws IOException {
        var num = ((Button)event.getSource()).getId();
        num = num.substring(num.length() - 1);
        if(num.equals("1")) {
            confirmRectangle1.setVisible(!confirmRectangle1.isVisible());
            ready1.setVisible(!ready1.isVisible());
        }
        else if(num.equals("2")){
            confirmRectangle2.setVisible(!confirmRectangle2.isVisible());
            ready2.setVisible(!ready2.isVisible());
        }
        if(confirmRectangle1.isVisible() && confirmRectangle2.isVisible()) {
            initDataDuel();
            StageManager.setScene(StageManager.StageEnum.GAME);
            GameController.startGame();
        }
    }

    private void initDataDuel() {
        var players = initPlayers();
        var map = new Field(Element.WATER, new ArrayList<>(), new Vec2(0, 1),
                new Rectangle(0, 0, StageManager.getWidth(), StageManager.getHeight()));
        var duel = new Duel(players, map, Optional.empty(), 99);
        DuelManager.setDuel(duel);
        ImageManager.loadImage(ImageKey.FIELD, getNameMap());
    }

    private String getNameMap() {
        return "images/map/" + mapChosen + ".jpg";
    }

    private void initBackground() {
        anchorPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        stars.play();
        starsView.forEach(star -> star.setVisible(true));
        astronauts.forEach(astronaut -> astronaut.setVisible(true));
    }

    private void showTitle() {
        var fontSize = 150;
        var font = Font.loadFont(Objects.requireNonNull(this.getClass().getResource("font/font.ttf")).toExternalForm(), fontSize);
        title = new Label("SUS FIGHTER");
        title.setPrefWidth(StageManager.getWidth());
        title.setPrefHeight(fontSize);
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-stroke-width: 1px;");
        title.setFont(font);
        title.alignmentProperty().set(Pos.CENTER);
        title.setVisible(false);
        anchorPane.getChildren().add(title);
    }

    @FXML
    void exitGame() {
        System.exit(0);
    }

    @FXML
    void choseMap(MouseEvent mouseEvent){
        mapChosen = ((Node)mouseEvent.getSource()).getParent().getParent().getId();
        buttonMapVisible(false);
        playerMenuVisible(true);
        menu = Menu.CHARACTER;
    }

    @FXML
    void openMenu(){
        if(menu == Menu.CLICK){
            buttonsMenuVisible(true);
            click.setVisible(false);
            timeline.stop();
            menu = Menu.MODE;
            initBackground();
            toFront(back, anchorPane);
        }
    }

    @FXML
    void chosePlayer(MouseEvent event){
        var name = ((Node)event.getSource()).getId();
        var num = name.substring(name.length() - 1);
        name = name.substring(0, name.length() - 1);
        if(name.endsWith("1")) nameSelect1 = changePlayer(select1, nameSelect1, name,
                2, rectangle1, 0, Integer.parseInt(num), page1);
        if(name.endsWith("2")) nameSelect2 = changePlayer(select2, nameSelect2, name,
                1, rectangle2, StageManager.getWidth() / 2, Integer.parseInt(num), page2);
    }

    @FXML
    void backMenu(){
         if(menu ==  Menu.MAP){
             buttonMapVisible(false);
             buttonsMenuVisible(true);
             menu = Menu.MODE;
             back.setVisible(false);
         }
        if(menu ==  Menu.CHARACTER){
            reinitializeCharacter();
            playerMenuVisible(false);
            buttonMapVisible(true);
            menu = Menu.MAP;
        }
        if(menu == Menu.CREDITS){
            creditsVisible(false);
            back.setVisible(false);
            buttonsMenuVisible(true);
        }
    }

    @FXML
    void addPage(MouseEvent event){
        var id = ((Node)(event.getSource())).getId();
        var num = id.substring(id.length() - 1);
        if(num.equals("1")) page1 = pageModification(page1, heads1);
        if(num.equals("2")) page2 = pageModification(page2, heads2);
    }

    private int pageModification(int page, List<VBox> vBox) {
        page++;
        page = (page <= ((heads.size() - 1) / NUMBER_CASE))? page : 0;
        System.out.println(page + " " + heads.size() / NUMBER_CASE);
        for (int i = 0; i < NUMBER_CASE; i++) {
            if(page * NUMBER_CASE + i >= heads.size()){
                vBox.get(i).setVisible(false);
                continue;
            }
            vBox.get(i).setVisible(true);
            ((ImageView)(vBox.get(i).getChildren().get(0))).setImage(heads.get(page * NUMBER_CASE + i));
        }
        return page;
    }

    private void reinitializeCharacter() {
        page1 = 0;
        page2 = 0;
        nameSelect1 = changePlayer(select1, nameSelect1, "black1", 2, rectangle1, 0, 0, page1);
        nameSelect2 = changePlayer(select2, nameSelect2, "green2",1, rectangle2, StageManager.getWidth() / 2, 1, page2);
        white1.setDisable(true);
        black2.setDisable(true);
        confirmRectangle1.setVisible(false);
        confirmRectangle2.setVisible(false);
        ready1.setVisible(false);
        ready2.setVisible(false);
    }

    private String changePlayer(VBox select, String nameSelect, String name,
                                int otherPlayer, Rectangle rectangle, int x_start, int num, int page) {
        var toDisableNode = StageManager.getScene().lookup("#" + name.substring(0, name.length() - 1) + otherPlayer + num);
        toDisableNode.setDisable(true);
        var numBefore = nameSelect.substring(nameSelect.length() - 1);
        var disabledNode2 = StageManager.getScene().
                lookup("#" + nameSelect.substring(0, nameSelect.length() - 2) + otherPlayer + numBefore);
        disabledNode2.setDisable(false);
        ((ImageView)select.getChildren().get(0)).setImage(images.get(((num == 7)? 6: num)  + page * NUMBER_CASE));
        placeRectangle(num, rectangle, x_start);
        return name + num;

    }

    private void placeRectangle(int num, Rectangle rectangle, int x_start) {
        var width = StageManager.getWidth();
        var height = StageManager.getHeight();
        var widthVBox = green1.getPrefWidth();
        var heightVBox = green1.getPrefHeight();
        var spacingX = (width / 2.0 - (widthVBox * 3)) / 4;
        var spacingY = (height / 2.0 - (heightVBox * 3)) / 4;
        var y = height / 2 + ((num / 3) + 1) * spacingY + num / 3 * heightVBox;
        var x = x_start + (((num % 3) + 1) * spacingX + num % 3 * widthVBox);
        AnchorPane.setLeftAnchor(rectangle, x);
        AnchorPane.setTopAnchor(rectangle, y);
    }

    private void buttonsMenuVisible(boolean bool){
        title.setVisible(bool);
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

    private void creditsVisible(boolean b){
        creditsTitle.setVisible(b);
        quentin.setVisible(b);
        sebastien.setVisible(b);
        yoan.setVisible(b);
    }

    private void playerMenuVisible(boolean b) {
        yellow2.setVisible(b);
        green2.setVisible(b);
        red2.setVisible(b);
        purple2.setVisible(b);
        pink2.setVisible(b);
        white2.setVisible(b);
        black2.setVisible(b);
        yellow1.setVisible(b);
        green1.setVisible(b);
        red1.setVisible(b);
        purple1.setVisible(b);
        pink1.setVisible(b);
        white1.setVisible(b);
        black1.setVisible(b);
        line.setVisible(b);
        select2.setVisible(b);
        select1.setVisible(b);
        rectangle1.setVisible(b);
        rectangle2.setVisible(b);
        confirmPlayer1.setVisible(b);
        confirmPlayer2.setVisible(b);
        addPage1.setVisible(b);
        addPage2.setVisible(b);
    }

    private List<Player> initPlayers() {
        var list = new ArrayList<Player>();
        list.add(initPlayer(nameSelect1, StageManager.getWidth() / 3, 2 * StageManager.getHeight() / 3, Command.getDefaultP1(),
                Element.WATER, 1, false));
        list.add(initPlayer(nameSelect2, 2 * StageManager.getWidth() / 3, 2 * StageManager.getHeight() / 3, Command.getDefaultP2(),
                Element.FIRE, 2, true));
        return list;

    }

    private Player initPlayer(String nameSelect, int x, int y, Command command,
                            Element element, int numPlayer, boolean isFlipped) {
        var name = nameSelect.substring(0, nameSelect.length() - 1);
        return new Player(name, new Rectangle(x, y, 150, 250), element, command, numPlayer, isFlipped);

    }

}
