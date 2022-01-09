package fr.uge.susfighter.mvc;

import java.util.Objects;

public class DuelManager {

    private static Duel DUEL_SAVE;

    public static void setDuel(Duel duel) {
        DUEL_SAVE = Objects.requireNonNull(duel);
    }

    public static Duel getDuel() {
        return DUEL_SAVE;
    }
}
