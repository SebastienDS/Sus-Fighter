package mvc;

import object.*;
import object.Event;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Duel implements Displayable {

    private final List<Player> players;
    private final Map map;
    private final World world;
    private Optional<Event> currentEvent;
    private final List<WeaponCase> weapons;
    private long startTime;

    public Duel(List<Player> players, Map map, Optional<Event> currentEvent) {
        this.players = Objects.requireNonNull(players);
        this.map = map;
        this.currentEvent = Objects.requireNonNull(currentEvent);
        weapons = new ArrayList<>();
        startTime = System.currentTimeMillis();
        world = new World(map.gravity());
        Physics.addBodyStatic(world, 0, map.floorHeight(), Display.display().getWidth(), Display.display().getHeight());
        Physics.addDynamicBody(world, players.get(0).getCoordinate().getX(), players.get(0).getCoordinate().getY(), 150, 300, 1f, 0.3f);
        var body = Physics.addDynamicBody(world, players.get(0).getCoordinate().getX(), players.get(0).getCoordinate().getY(), 150, 300, 1f, 0.3f);
    }



    public long time() {
        return System.currentTimeMillis() - startTime;
    }

    public void update() {
        var player1 = players.get(0);
        var player2 = players.get(1);
        var needFlip = player1.needFlip(player2);
        player1.update(needFlip);
        player2.update(needFlip);

    }

    @Override
    public void display(Display d) {
        map.display(d);
        players.stream().forEach(p -> p.display(d));
    }
}