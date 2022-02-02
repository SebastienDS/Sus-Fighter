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

    /**
     * Get the bonus multiplicator against the given Element
     * @param enemy enemy element
     * @return the multiplicator of the element against the given enemy element
     */
    public double getElementMultiplicator(Element enemy) {
        if (enemy == Element.valueOf(goodAgainst)) {
            return 1.25;
        } else if (enemy == Element.valueOf(badAgainst)) {
            return 0.75;
        }
        return 1;
    }

}
