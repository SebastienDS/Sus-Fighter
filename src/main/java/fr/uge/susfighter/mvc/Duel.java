package fr.uge.susfighter.mvc;

import fr.uge.susfighter.object.*;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class Duel {

    private final List<Player> players;
    private final Field map;
    private Optional<Event> currentEvent;
    private final List<WeaponCase> weapons;
    private long startTime;
    private long endTime;

    public Duel(List<Player> players, Field map, Optional<Event> currentEvent, long time) {
        this.players = List.copyOf(Objects.requireNonNull(players));
        this.map = map;
        this.currentEvent = Objects.requireNonNull(currentEvent);
        weapons = new ArrayList<>();
        startTime = System.currentTimeMillis();
        endTime = time;
    }

    public long timeLeft() {
        return endTime - ((System.currentTimeMillis() - startTime) / 1000);
    }

    public boolean update(KeyEvent keyEvent) {
        var player1 = players.get(0);
        var player2 = players.get(1);

        System.out.println("1: " + player1.getX());

        player1.keyPressed(keyEvent.getCode());
        player2.keyPressed(keyEvent.getCode());

        System.out.println("2: " + player1.getX());

        var needFlip = player1.needFlip(player2);

        player1.update(needFlip, map.gravity(), map.bounds());
        player2.update(needFlip, map.gravity(), map.bounds());

        System.out.println("3: " + player1.getX());

        player1.checkAttack(player2);
        player2.checkAttack(player1);

        return player1.isDead() || player2.isDead();
    }

    public void release(KeyEvent keyEvent) {
        players.forEach(p -> p.keyReleased(keyEvent.getCode()));
    }

    public Player getPlayer(int i) {
        return players.get(i);
    }
}