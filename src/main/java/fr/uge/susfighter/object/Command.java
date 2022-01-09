package fr.uge.susfighter.object;

import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Command {

    public enum Key {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        ATTACK
    }

    private final Map<Key, KeyCode> keyCodes;

    public Command() {
        keyCodes = new HashMap<>();
    }

    public Command addKeyCode(Key key, KeyCode code) {
        Objects.requireNonNull(key);
        keyCodes.put(key, code);
        return this;
    }

    public KeyCode get(Key key) {
        Objects.requireNonNull(key);
        return keyCodes.get(key);
    }

    public static Command getDefaultP1(){
        var command = new Command();
        command.addKeyCode(Command.Key.LEFT, KeyCode.Q)
                .addKeyCode(Command.Key.RIGHT, KeyCode.D)
                .addKeyCode(Command.Key.UP, KeyCode.Z)
                .addKeyCode(Command.Key.DOWN, KeyCode.S)
                .addKeyCode(Command.Key.ATTACK, KeyCode.T);
        return command;
    }

    public static Command getDefaultP2(){
        var command = new Command();
        command.addKeyCode(Command.Key.LEFT, KeyCode.LEFT)
                .addKeyCode(Command.Key.RIGHT, KeyCode.RIGHT)
                .addKeyCode(Command.Key.UP, KeyCode.UP)
                .addKeyCode(Command.Key.DOWN, KeyCode.DOWN)
                .addKeyCode(Command.Key.ATTACK, KeyCode.NUMPAD1);
        return command;
    }
}
