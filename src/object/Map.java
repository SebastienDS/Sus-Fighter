package object;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Map {

    private final Path image;
    private final Element element;
    private final List<Event> events;

    public Map(Path image, Element element, List<Event> events) {
        this.image = Objects.requireNonNull(image);
        this.element = Objects.requireNonNull(element);
        this.events = Objects.requireNonNull(events);
    }
}
