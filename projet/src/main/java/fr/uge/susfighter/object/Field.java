package fr.uge.susfighter.object;


import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Objects;

public record Field(String name, Element element, Vec2 gravity, Rectangle bounds) {

    public Field {
        Objects.requireNonNull(name);
        Objects.requireNonNull(element);
        Objects.requireNonNull(gravity);
        Objects.requireNonNull(bounds);
    }
}
