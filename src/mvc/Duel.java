package mvc;

import object.Event;
import object.Map;
import object.Player;
import object.WeaponCase;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Duel {

    private final List<Player> players;
    private final Map map;
    private Optional<Event> currentEvent;
    private final List<WeaponCase> weapons;
    private long startTime;

    public Duel(List<Player> players, Map map, Optional<Event> currentEvent) {
        this.players = Objects.requireNonNull(players);
        this.map = map;
        this.currentEvent = Objects.requireNonNull(currentEvent);
        weapons = new ArrayList<>();

        startTime = System.currentTimeMillis();
    }

    public long time() {
        return System.currentTimeMillis() - startTime;
    }

    public void update() {
        players.stream().forEach(p -> p.update());
    }

    public void display(Graphics g) {
        players.stream().forEach(p -> p.display(g));
    }
}