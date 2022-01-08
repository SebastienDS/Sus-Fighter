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
}
