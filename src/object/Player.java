package object;

import java.util.Objects;

public class Player {

    private final String name;
    private final Coordinate coordinate;
    private final Statistic statistic;
    private final Element element;

    public Player(String name, Coordinate coordinate, Element element) {
        this.name = name;
        this.coordinate = coordinate;
        this.element = Objects.requireNonNull(element);
        statistic = new Statistic();
    }

}
