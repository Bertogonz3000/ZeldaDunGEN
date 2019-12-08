package GraphGrammars;

import java.util.Random;

public enum nodePositions {
    //List of possible positions for connections to a node
    LEFT(0), TOP(1), RIGHT(2), BOTTOM(3);

    //The numerical value of a type nodePositions
    private int numVal;

    //constructor
    nodePositions(int numVal) {
        this.numVal = numVal;
    }

    //get the numberical value of this enum
    public int getNumVal() {
        return numVal;
    }

    public static nodePositions getPositionForInt(int num) {
        switch (num) {
            case 0:
                return LEFT;
            case 1:
                return TOP;
            case 2:
                return RIGHT;
            case 3:
                return BOTTOM;
        }
        throw new IndexOutOfBoundsException("nodePositions go from 0 to 4");
    }

    @Override
    public String toString() {
        switch (this) {
            case LEFT:
                return "w";
            case TOP:
                return "n";
            case RIGHT:
                return "e";
            case BOTTOM:
                return "s";
            default:
                throw new IndexOutOfBoundsException("nodePositions should only be left right top " +
                        "bottom");
        }
    }
}
