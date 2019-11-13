package graphs;

public enum NODE_POSITIONS {
    LEFT(0), TOP(1), RIGHT(2), BOTTOM(3);

    private int numVal;

    NODE_POSITIONS(int numVal){
        this.numVal = numVal;
    }

    public int getNumVal(){
        return numVal;
    }
}
