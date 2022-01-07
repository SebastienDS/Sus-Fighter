package object;

import mvc.Display;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public record Field(Image image, Element element, List<Event> events, float minHeight, float floorHeight, Vec2 gravity, Rectangle bounds) {

    public Field {
        Objects.requireNonNull(image);
        Objects.requireNonNull(element);
        Objects.requireNonNull(events);
        Objects.requireNonNull(gravity);
        Objects.requireNonNull(bounds);
    }

    public Field(Path path, Element element, List<Event> events, float floorHeight) throws  IOException{
        this(
            Images.resize(Images.loadImage(Objects.requireNonNull(path)), Display.display().getWidth(), Display.display().getHeight()),
            element,
            events,
            Float.MIN_VALUE,
            floorHeight,
            new Vec2(0f, 1),
            new Rectangle(0, 0, Display.display().getWidth(), (int)floorHeight)
        );
    }

    public void display(Display d) {
        var g = d.getGraphics();

        g.drawImage(image, 0, 0, Color.WHITE, null);
    }

}
