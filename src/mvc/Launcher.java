package mvc;

import mvc.Game;

import java.awt.*;
import java.io.IOException;

public class Launcher {

    public static void main(String[] args) {
        var dimension = Toolkit.getDefaultToolkit().getScreenSize();
        try{
            var game = new Game("Sus Fighter", (int)dimension.getWidth(), (int)dimension.getHeight());
            game.start();
        }catch(IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return;
        }

    }
}
