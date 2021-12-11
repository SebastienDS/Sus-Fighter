package object;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import mvc.Display;
import object.Command.KeyCode;
import object.ImagePlayer.ImageKey;

public class Player implements KeyListener, Displayable {

    private final String name;
    private final Coordinate coordinate;
    private final Statistic statistic;
    private final Element element;
    private final Command command;
    private final ImagePlayer images;
    private boolean isFlipped;
    private boolean jump;
    private final int width;
    private final int height;
    private KeyCode xMovement;

    private double dx = 0;
    private double dy = 0;
    private double push = 0;
    private double minHeight;
    private double maxHeight;

    public Player(String name, Coordinate coordinate, Element element, Command command, Path path, boolean isFlipped) throws IOException {
        this.name = Objects.requireNonNull(name);
        this.coordinate = Objects.requireNonNull(coordinate);
        this.element = Objects.requireNonNull(element);
        this.command = Objects.requireNonNull(command);
        statistic = new Statistic();
        this.isFlipped = isFlipped;
        images = new ImagePlayer(path);
        width = images.get(ImageKey.IDLE).getWidth(null);
        height = images.get(ImageKey.IDLE).getHeight(null);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        var key = e.getKeyCode();

        if (key == command.get(KeyCode.LEFT)) {
            dx = -10;
            xMovement = KeyCode.LEFT;
        }
        else if (key == command.get(KeyCode.RIGHT)) {
            dx = 10;
            xMovement = KeyCode.RIGHT;
        }
        else if (key == command.get(KeyCode.UP) && !jump) {
            dy = -30;
            jump = true;
        }
        else if (key == command.get(KeyCode.DOWN) && jump) {
            if(dy < 0) dy = 0;
            else dy += 5;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        var key = e.getKeyCode();
        if ((key == command.get(KeyCode.LEFT) || key == command.get(KeyCode.RIGHT)) && key == command.get(xMovement)) {
            dx = 0;
        }
    }

    @Override
    public void display(Display d) {
        var g = d.getGraphics();
        g.setColor(Color.BLACK);
        var imageKey = (isFlipped) ? ImageKey.IDLE_FLIPPED : ImageKey.IDLE;
        var image = images.get(imageKey);
        g.drawImage(image, coordinate.getX(), coordinate.getY(),Color.BLACK, null);
    }

    private void manageFlip(boolean needFlip){
        if(needFlip) {
            isFlipped = !isFlipped;
        }
    }

    private void manageGround(){
        if((int) (maxHeight) <= coordinate.getY()){
            dy = 0;
            if(jump) jump = false;
        }
    }

    public void update(boolean needFlip) {
        dy += 1;
        coordinate.move(dx + push, dy, 0, Display.display().getWidth() - width, minHeight, maxHeight);
        manageFlip(needFlip);
        manageGround();
        push = 0;
    }

    public boolean needFlip(Player player){
        return coordinate.getX() < player.coordinate.getX() && isFlipped || player.coordinate.getX() < coordinate.getX() && player.isFlipped;
    }

    public void push(Player player2) {
        if(collision(player2, 3) && player2.isPushable() && Math.abs(dx) > Math.abs(player2.dx)){
            player2.push = dx;
            return;
        }
        if(collision(player2, 3) &&  Math.abs(dx) >= Math.abs(player2.dx)){
            push = -dx;
        }
    }

    private boolean isPushable() {
        return coordinate.getX() > 0 && coordinate.getX() < Display.display().getWidth() - width;
    }

    private boolean collision(Player player2, int margin) {
        return collisionX(player2) && collisionY(player2, margin);
    }

    private boolean collisionX(Player player2){
        return collisionX(player2, true);
    }

    private boolean collisionX(Player player2, boolean firstPlayer){
        return nextX() + width >= player2.nextX()
                && nextX() + width <= player2.nextX() + player2.width
                || firstPlayer && player2.collisionX(this, false);
    }

    private boolean collisionY(Player player2, double margin){
        return collisionY(player2, margin, true);
    }

    private boolean collisionY(Player player2, double margin, boolean firstPlayer){
        return  nextY() + height - margin >= player2.nextY()
                && nextY() + height - margin <= player2.nextY() + player2.height
                || firstPlayer && player2.collisionY(this, margin,false);
    }

    public void interact(Player player2, double minHeight, double floorHeight) {
        this.manageHeadCollision(player2, minHeight, floorHeight);
        this.push(player2);
    }

    private void manageHeadCollision(Player player2, double minHeight, double floorHeight) {
        double height_difference = (coordinate.getY() + height - dy) - (player2.coordinate.getY() + player2.dy);
        if(!(height_difference < 5 && height_difference > -5 && coordinate.getY() < player2.coordinate.getY()
                && collisionX(player2) && maxHeight != player2.coordinate.getY())){
            maxHeight = floorHeight - height;
            player2.minHeight = minHeight;
            return;
        }
        maxHeight = player2.coordinate.getY() - height;
        player2.minHeight = coordinate.getY() + height;
    }

    private double nextX(){
        return coordinate.getX() + dx;
    }

    private double nextY() {
        return coordinate.getY() + dy;
    }
}
