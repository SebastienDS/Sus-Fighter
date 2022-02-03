package fr.uge.susfighter.mvc;

import java.util.Objects;

/**
 * This class save the current duel of the game
 */
public class DuelManager {

    /**
     * Current duel
     */
    private static Duel DUEL_SAVE;

    /**
     * This method set the current duel to the duel given in parameter
     * @param duel new duel
     */
    public static void setDuel(Duel duel) {
        DUEL_SAVE = Objects.requireNonNull(duel);
    }

    /**
     * This method return the duel of the game
     * @return the duel of the game
     */
    public static Duel getDuel() {
        return DUEL_SAVE;
    }
}
