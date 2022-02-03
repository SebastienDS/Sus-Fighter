package fr.uge.susfighter.mvc;

import fr.uge.susfighter.object.Field;
import fr.uge.susfighter.object.Fighter;
import javafx.scene.input.KeyEvent;

import java.util.List;
import java.util.Objects;

/**
 * This method save all the data of a duel
 */
public class Duel {

    /**
     * List of fighter
     */
    private final List<Fighter> players;
    /**
     * Map of the duel
     */
    private final Field map;
    /**
     * Starting time of the duel
     */
    private long startTime;
    /**
     * Total time before end of duel
     */
    private long endTime;
    /**
     * True if game is paused
     */
    private boolean pause;
    /**
     * level of campaign (if not campaign : -1)
     */
    private final int level;
    /**
     * step of campaign (if not campaign : -1)
     */
    private final int step;


    /**
     * Create an instance of duel with the given parameter
     * @param players list of players of the duel
     * @param map map of the duel
     * @param time starting time
     * @param level level of campaign
     * @param step step of campaign
     */
    public Duel(List<Fighter> players, Field map, long time, int level, int step) {
        this.players = List.copyOf(Objects.requireNonNull(players));
        this.map = map;
        startTime = System.currentTimeMillis();
        endTime = time;
        this.level = level;
        this.step = step;
    }

    /**
     *
     * Create an instance of duel with the given parameter and default value for step and level (-1)
     * @param players list of players of the duel
     * @param map map of the duel
     * @param time starting time
     */
    public Duel(List<Fighter> players, Field map,  long time) {
        this(players, map, time, -1, -1);
    }

    /**
     * This method return the time left before the duel end
     * @return time left before end of duel
     */
    public long timeLeft() {
        return endTime - ((System.currentTimeMillis() - startTime) / 1000);
    }

    /**
     * This method update the date of the game
     */
    public void update() {
        if(pause) return;
        var player1 = players.get(0);
        var player2 = players.get(1);

        player1.update(map.gravity(), map.bounds());
        player2.update(map.gravity(), map.bounds());

        player1.interact(player2);
        player2.interact(player1);
    }

    /**
     * This method manage a key pressed
     * @param keyEvent event of key pressed
     */
    public void pressed(KeyEvent keyEvent){
        if(pause) return;
        players.forEach(player -> player.keyPressed(keyEvent.getCode()));
    }

    /**
     * This method manage a key released
     * @param keyEvent event of key released
     */
    public void release(KeyEvent keyEvent) {
        if(pause) return;
        players.forEach(p -> p.keyReleased(keyEvent.getCode()));
    }

    /**
     * This method return the i-th fighter of the list
     * @param i index of fighter to get
     * @return i-th fighter of the list
     */
    public Fighter getPlayer(int i) {
        return players.get(i);
    }

    /**
     * This method return the fighter who won the duel
     * @return fighter who won
     */
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

    /**
     * This method return the level of the campaign
     * @return the level of the campaign
     */
    public int getLevel() {
        return level;
    }

    /**
     * This method return the step of the campaign
     * @return the step of the campaign
     */
    public int getStep() {
        return step;
    }

    /**
     * This method return the map of the duel
     * @return the map of the duel
     */
    public Field getMap() {
        return map;
    }

    /**
     * This method return true if the duel is paused
     * @return true if the duel is paused
     */
    public boolean isPaused() {
        return pause;
    }

    /**
     * This method turn ON/OFF the pause
     */
    public void swapPause() {
        pause = !pause;
    }

    /**
     * This method change start time in case of pause to pause the chronometer
     * @param startTime start time of pause
     * @param endTime end time of pause
     */
    public void manageTime(long startTime, long endTime) {
        if(pause) return;
        this.startTime += endTime - startTime;
    }
}