package fr.uge.susfighter.object;


import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Objects;

public record Field(Element element, List<Event> events, Vec2 gravity, Rectangle bounds) {

    public Field {
        Objects.requireNonNull(element);
        Objects.requireNonNull(events);
        Objects.requireNonNull(gravity);
        Objects.requireNonNull(bounds);
    }

//    public void display(Display d, Images images) {
//        var g = d.getGraphics();
//
//        g.drawImage(images.get(ImageKey.FIELD), 0, 0, Color.WHITE, null);
//    }

}
