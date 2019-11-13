package graphs;

public enum alphabet {
    //The alphabet of symbols to be used in graph grammar.
    boss_level(0), boss_mini(1), entrance(2), goal(3), key(4), lock(5),
    exploration(6);

    //numerical value of this enum
    private int numVal;

    alphabet(int numVal) {
        this.numVal = numVal;
        this.numVal = numVal;
    }

    public int getNumVal() {
        return this.numVal;
    }

    @Override
    public String toString() {
        switch (this.numVal) {
            case 0:
                return "boss_level";
            case 1:
                return "boss_mini";
            case 2:
                return "entrance";
            case 3:
                return "goal";
            case 4:
                return "key";
            case 5:
                return "lock";
            case 6:
                return "exploration";
        }
        return "none";
    }
}
