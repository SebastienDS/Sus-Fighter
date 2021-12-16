package object;

import mvc.Display;
import org.jbox2d.common.Vec2;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public record Map(Image image, Element element, List<Event> events, float floorHeight, float minHeight, Vec2 gravity){

    public Map{
        Objects.requireNonNull(image);
        Objects.requireNonNull(element);
        Objects.requireNonNull(events);
        Objects.requireNonNull(gravity);

    }

    public Map(Path path, Element element, List<Event> events, float floorHeight) throws  IOException{
        this(
            ImageManager.resize(ImageManager.loadImage(Objects.requireNonNull(path)), Display.display().getWidth(), Display.display().getHeight()),
            element,
            events,
            Float.MIN_VALUE,
            floorHeight,
            new Vec2(0f, 10f)
        );
    }

    public void display(Display d) {
        var g = d.getGraphics();

        g.drawImage(image, 0, 0, Color.WHITE, null);
    }
}
