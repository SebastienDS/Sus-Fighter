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
        ATTACK,
        ULTIMATE_ATTACK
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
        command.addKeyCode(Key.LEFT, KeyCode.Q)
                .addKeyCode(Key.RIGHT, KeyCode.D)
                .addKeyCode(Key.UP, KeyCode.Z)
                .addKeyCode(Key.DOWN, KeyCode.S)
                .addKeyCode(Key.ATTACK, KeyCode.T)
                .addKeyCode(Key.ULTIMATE_ATTACK, KeyCode.Y);
        return command;
    }

    public static Command getDefaultP2(){
        var command = new Command();
        command.addKeyCode(Key.LEFT, KeyCode.LEFT)
                .addKeyCode(Key.RIGHT, KeyCode.RIGHT)
                .addKeyCode(Key.UP, KeyCode.UP)
                .addKeyCode(Key.DOWN, KeyCode.DOWN)
                .addKeyCode(Key.ATTACK, KeyCode.NUMPAD1)
                .addKeyCode(Key.ULTIMATE_ATTACK, KeyCode.NUMPAD2);
        return command;
    }
}
