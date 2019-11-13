package graphs;

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
}
