package fr.uge.susfighter.mvc;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;

public class WinnerController {

    @FXML
    BorderPane borderpane;

    @FXML
    Label label;

    @FXML
    Button quit;

    @FXML
    Button retry;

    @FXML
    void initialize() {
        var font = Font.loadFont(Objects.requireNonNull(this.getClass().getResource("font/font.ttf")).toExternalForm(), 45);
        var duel = DuelManager.getDuel();
        var winner = duel.getWinner();
        label.setText(winner.getName() + " a gagne le duel !");
        label.setTextFill(Color.WHITE);
        label.setFont(font);
        borderpane.setBackground(new Background(new BackgroundFill(Color.DEEPPINK, new CornerRadii(0), Insets.EMPTY)));

        quit.setFont(font);
        retry.setFont(font);
    }
}
