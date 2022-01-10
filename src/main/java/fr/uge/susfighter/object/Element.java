package fr.uge.susfighter.object;


public enum Element {
    WATER("FIRE", "EARTH"),
    FIRE("WIND", "WATER"),
    EARTH("WATER", "WIND"),
    WIND("EARTH", "FIRE"),
    DARK("LIGHT", "LIGHT"),
    LIGHT("DARK", "DARK");

    private final String goodAgainst;
    private final String badAgainst;

    Element(String goodAgainst, String badAgainst) {
        this.goodAgainst = goodAgainst;
        this.badAgainst = badAgainst;
    }

    public double getElementMultiplicator(Element enemy) {
        if (enemy == Element.valueOf(goodAgainst)) {
            return 1.25;
        } else if (enemy == Element.valueOf(badAgainst)) {
            return 0.75;
        }
        return 1;
    }

}
