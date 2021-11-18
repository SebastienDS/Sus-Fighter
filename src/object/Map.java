package object;

import mvc.Display;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Map {

    private final Image image;
    private final Element element;
    private final List<Event> events;
    private final double floorHeight;

    public Map(Path path, Element element, List<Event> events, double floorHeight) throws IOException {
        this.element = Objects.requireNonNull(element);
        this.events = Objects.requireNonNull(events);
        this.image = ImageManager.resize(ImageManager.loadImage(Objects.requireNonNull(path)), Display.display().getWidth(), Display.display().getHeight());
        this.floorHeight = floorHeight;
    }

    public void display(Display d) {
        var g = d.getGraphics();

        g.drawImage(image, 0, 0, Color.WHITE, null);
    }

    public double getFloorHeight(){
        return floorHeight;
    }
}
