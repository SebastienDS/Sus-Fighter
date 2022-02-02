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
        ULTIMATE_ATTACK,
        BLOCK,
    }

    private final Map<Key, KeyCode> keyCodes;

    public Command() {
        keyCodes = new HashMap<>();
    }

    /**
     * Assign to a Key the specific KeyCode
     * @param key key to bind
     * @param code value of the key
     * @return the instance to be able to chain the calls
     */
    public Command addKeyCode(Key key, KeyCode code) {
        Objects.requireNonNull(key);
        keyCodes.put(key, code);
        return this;
    }

    /**
     * Get the KeyCode of a Key
     * @param key key binded
     * @return the keyCode value for the given key
     */
    public KeyCode get(Key key) {
        Objects.requireNonNull(key);
        return keyCodes.get(key);
    }

    /**
     * Get the default keys for the player 1
     * @return the default keys
     */
    public static Command getDefaultP1(){
        var command = new Command();
        command.addKeyCode(Key.LEFT, KeyCode.Q)
                .addKeyCode(Key.RIGHT, KeyCode.D)
                .addKeyCode(Key.UP, KeyCode.Z)
                .addKeyCode(Key.DOWN, KeyCode.S)
                .addKeyCode(Key.ATTACK, KeyCode.T)
                .addKeyCode(Key.ULTIMATE_ATTACK, KeyCode.Y)
                .addKeyCode(Key.BLOCK, KeyCode.U);
        return command;
    }

    /**
     * Get the default keys for the player 2
     * @return the default keys
     */
    public static Command getDefaultP2(){
        var command = new Command();
        command.addKeyCode(Key.LEFT, KeyCode.LEFT)
                .addKeyCode(Key.RIGHT, KeyCode.RIGHT)
                .addKeyCode(Key.UP, KeyCode.UP)
                .addKeyCode(Key.DOWN, KeyCode.DOWN)
                .addKeyCode(Key.ATTACK, KeyCode.NUMPAD1)
                .addKeyCode(Key.ULTIMATE_ATTACK, KeyCode.NUMPAD2)
                .addKeyCode(Key.BLOCK, KeyCode.NUMPAD3);
        return command;
    }
}
