package mvc;

import object.*;
import object.Event;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Duel implements Displayable {

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
        var player1 = players.get(0);
        var player2 = players.get(1);
        var needFlip = player1.needFlip(player2);
        player1.update(needFlip, map.getFloorHeight());
        player2.update(needFlip, map.getFloorHeight());
        player1.push(player2);
        player2.push(player1);
    }

    @Override
    public void display(Display d) {
        map.display(d);
        players.stream().forEach(p -> p.display(d));
    }
}