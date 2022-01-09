package fr.uge.susfighter.mvc;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;


public class MenuController {

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

    private Timeline timeline;

    private int menu = 0;


    @FXML
    void initialize(){
        initPlacement();
        initFont();
        initTimeline();
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
    }


    @FXML
    void campaignMenu() {

    }

    @FXML
    void creditsMenu() {

    }

    @FXML
    void duelMenu() throws IOException {
        StageManager.setScene(StageManager.StageEnum.GAME);
        GameController.startGame();
    }

    @FXML
    void exitGame() {
        System.exit(0);
    }

    @FXML
    void openMenu(MouseEvent event){
        if(menu == 0){
            campaign.setVisible(true);
            duel.setVisible(true);
            credits.setVisible(true);
            exit.setVisible(true);
            click.setVisible(false);
            timeline.stop();
        }
    }
}
