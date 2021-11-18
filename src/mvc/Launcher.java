package mvc;

import mvc.Game;

import java.awt.*;

public class Launcher {

    public static void main(String[] args) {
        var dimension = Toolkit.getDefaultToolkit().getScreenSize();
        var game = new Game("Sus Fighter", (int)dimension.getWidth(), (int)dimension.getHeight());
        game.start();
    }
}
