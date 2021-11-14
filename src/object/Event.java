package object;

import java.util.Objects;

public class Event {

    private final String name;

    public Event(String name) {
        this.name = Objects.requireNonNull(name);
    }
}
