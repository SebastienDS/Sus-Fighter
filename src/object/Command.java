package object;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Command {

    public enum KeyCode {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        ATTACK
    }

    private final Map<KeyCode, Integer> keyCodes;

    public Command() {
        keyCodes = new HashMap<>();
    }

    public Command addKeyCode(KeyCode key, int code) {
        Objects.requireNonNull(key);
        keyCodes.put(key, code);
        return this;
    }

    public int get(KeyCode key) {
        Objects.requireNonNull(key);
        return keyCodes.get(key);
    }
}
