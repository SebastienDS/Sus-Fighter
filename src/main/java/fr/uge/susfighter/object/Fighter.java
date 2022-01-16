package fr.uge.susfighter.object;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

public interface Fighter {

    void keyPressed(KeyCode code);
    void keyReleased(KeyCode code);

    void update(Vec2 gravity, Rectangle bounds);
    void interact(Fighter player2);

    boolean isDead();

    Rectangle getBody();
    Rectangle getHitBox();
    Rectangle getAttack();
    Rectangle getUltimate();
    Rectangle getBlockHitBox();

    Statistic getStatistic();

    Element getElement();

    void project(Vec2 velocity);

    double percentageHpLeft();
    double percentageEnergy();

    int getNumPlayer();
    boolean isFlipped();
    String getName();
    boolean isAttacking();
    boolean isBlocking();
    String getType();
}
