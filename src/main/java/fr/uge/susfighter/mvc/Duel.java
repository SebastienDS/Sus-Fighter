package fr.uge.susfighter.mvc;

import fr.uge.susfighter.object.Field;
import fr.uge.susfighter.object.Fighter;
import javafx.scene.input.KeyEvent;

import java.util.List;
import java.util.Objects;

public class Duel {

    private final List<Fighter> players;
    private final Field map;
    private long startTime;
    private long endTime;
    private boolean pause;
    private final int level;
    private final int step;


    public Duel(List<Fighter> players, Field map, long time, int level, int step) {
        this.players = List.copyOf(Objects.requireNonNull(players));
        this.map = map;
        startTime = System.currentTimeMillis();
        endTime = time;
        this.level = level;
        this.step = step;
    }

    public Duel(List<Fighter> players, Field map,  long time) {
        this(players, map, time, -1, -1);
    }

    public long timeLeft() {
        return endTime - ((System.currentTimeMillis() - startTime) / 1000);
    }

    public void update() {
        if(pause) return;
        var player1 = players.get(0);
        var player2 = players.get(1);

        player1.update(map.gravity(), map.bounds());
        player2.update(map.gravity(), map.bounds());

        player1.interact(player2);
        player2.interact(player1);
    }

    public void pressed(KeyEvent keyEvent){
        if(pause) return;
        players.forEach(player -> player.keyPressed(keyEvent.getCode()));
    }

    public void release(KeyEvent keyEvent) {
        if(pause) return;
        players.forEach(p -> p.keyReleased(keyEvent.getCode()));
    }

    public Fighter getPlayer(int i) {
        return players.get(i);
    }

    public Fighter getWinner() {
        var player1 = players.get(0);
        var player2 = players.get(1);
        if (player1.isDead()) return player2;
        if (player2.isDead()) return player1;

        if (timeLeft() <= 0) {
            if (player1.getStatistic().percentageHpLeft() < player2.getStatistic().percentageHpLeft()) return player2;
            else return player1;
        }
        return null;
    }

    public int getLevel() {
        return level;
    }

    public int getStep() {
        return step;
    }

    public Field getMap() {
        return map;
    }

    public boolean isPaused() {
        return pause;
    }

    public void swapPause() {
        pause = !pause;
    }

    public void manageTime(long startTime, long endTime) {
        if(pause) return;
        this.startTime += endTime - startTime;
    }
}