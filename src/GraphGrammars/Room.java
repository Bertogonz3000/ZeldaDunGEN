package GraphGrammars;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A room in a space graph
 */
public class Room {

    /*The coordinates of this room in 2D space, implemented to avoid collisions while building out
        the space graph*/
    private int[] coords = new int[2];

    //The rooms connected to this one.
    private Room[] connections = new Room[4];

    //The contents of this room
    private ArrayList<roomContents> contents = new ArrayList<>();

    private boolean[] lockedDoors = new boolean[4];


    /**
     * 0 arguent constructor
     */
    public Room() {
    }

    /**
     * Constructor that adds a single element of type roomContents to the contents of this room
     *
     * @param content
     */
    public Room(roomContents content) {
        contents.add(content);

    }

    /**
     * @return - the indices of the connections array that are yet unused - as a nodePosition
     */
    public ArrayList<nodePositions> freeConnections() {
        ArrayList<nodePositions> unused = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (connections[i] == null) {
                unused.add(nodePositions.getPositionForInt(i));
            }
        }
        return unused;
    }

    /**
     * Lock the door given by side
     *
     * @param side - the door to lock
     */
    public void lockDoor(nodePositions side) {
        lockedDoors[side.getNumVal()] = true;
    }

    /**
     * Unlock the door given by side
     *
     * @param side - the door to unlock
     */
    public void unlockDoor(nodePositions side) {
        lockedDoors[side.getNumVal()] = false;
    }

    /**
     * Returns this room's coordinates in 2D space
     *
     * @return
     */
    public int[] getCoords() {
        return coords;
    }

    /**
     * Sets this room's coordinates in 2D space
     *
     * @param coords
     */
    public void setCoords(int[] coords) {
        this.coords = coords;
    }

    /**
     * Returns the rooms that this room is conected to
     *
     * @return
     */
    public Room[] getConnections() {
        return connections;
    }

    /**
     * Connects this room to input
     *
     * @param connection - the side of this room connecting to the next
     * @param room       - the new room.
     */
    public void setConnection(nodePositions connection, Room room) {
        connections[connection.getNumVal()] = room;
    }

    /**
     * Add the input into the contents of this room
     *
     * @param content - the content
     */
    public void addContents(roomContents content) {
        contents.add(content);
    }

    /**
     * Returns the contents of this room
     *
     * @return
     */
    public ArrayList<roomContents> getContents() {
        return contents;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(coords[0]).append(", ").append(coords[1]).append(")").append(
                " :: ").append(contents).append(" :: ");
        for (int i = 0; i < connections.length; i++) {
            if (connections[i] != null) {
                int[] roomCoords = connections[i].coords;
                if (lockedDoors[i]) {
                    builder.append("[").append(roomCoords[0]).append(", ").append(roomCoords[1]).append("]");
                } else {
                    builder.append("(").append(roomCoords[0]).append(", ").append(roomCoords[1]).append(")");
                }
            }
        }
        return builder.toString();
    }

    /**
     * Returns a graph viz compatible string
     *
     * @return
     */
    public String getGVString() {
        StringBuilder gvString = new StringBuilder();

        for (Room room : connections) {
            if (room != null) {
                gvString.append(getGVNodeName()).append(" -- ").append(room.getGVNodeName()).append(";\n");
            }
        }

        return gvString.toString();
    }

    //TODO - if we end up adding more than one thing to contents, this ain't gonna work

    /**
     * Return this room as a Graph viz node
     *
     * @return
     */
    public String getGVNodeName() {
        if (contents.isEmpty()) {
            return getGVCoordsForLabel();
        }
        return contents.get(0) + getGVCoordsForLabel();
    }

    /**
     * Return this rooms coords as graph viz code
     *
     * @return
     */
    public String getGVCoordsForLabel() {
        StringBuilder coordsBuilder = new StringBuilder();

        coordsBuilder.append("X");

        if (coords[0] < 0) {
            coordsBuilder.append("neg");
        }

        coordsBuilder.append(Math.abs(coords[0])).append("Y");

        if (coords[1] < 0) {
            coordsBuilder.append("neg");
        }

        coordsBuilder.append(Math.abs(coords[1]));

        return coordsBuilder.toString();
    }

    /**
     * Return graph viz string that allows positioning in the graph.
     *
     * @return
     */
    public String getGVCoordsForPosition() {
        return "\"" + coords[0] + "," + coords[1] + "!\"";
    }

    /**
     * @return - a string version of this room that should be used as input for an ASP program.
     */
    public String getGenerationString() {
        StringBuilder genBuilder = new StringBuilder();

        //Add door lines
        genBuilder.append(getDoorsForGeneration());

        //Add room contents, if any (monsters, items, bosses, exploration).
        if (!contents.isEmpty()) {
            genBuilder.append(contents.get(0).getGenerationString());
        }

        return genBuilder.toString();
    }

    private String getDoorsForGeneration() {
        StringBuilder genBuilder = new StringBuilder();

        //Create lines for doors, closed, locked, and final
        for (int i = 0; i < connections.length; i++) {
            //If there really is a connection here...
            if (connections[i] != null) {
                //TODO - this could change if we just want to have one single item in each room
                ArrayList<roomContents> contents = connections[i].getContents();

                //The type of the door - lock, final lock, or open(unlocked)
                String doorType;

                //Check the type and append the corresponding line
                if (contents.contains(roomContents.LOCK)) {
                    doorType = "locked";
                } else if (contents.contains(roomContents.FINAL_LOCK)) {
                    doorType = "final_locked";
                } else {
                    doorType = "open";
                }

                //Append the direction
                genBuilder.append("door(").append(nodePositions.getPositionForInt(i)).append(",").append(doorType).append(")\n");
            }
        }

        return genBuilder.toString();
    }

    /**
     * Returns a string containing all the necessary information to run dungeon analysis on this
     * room
     *
     * @return
     */
    public String getAnalysisString() {
        //TODO - change this when we get some measure of monster # and deadly score
        //TODO - figure out how to get #monsters into this class....may have to move generation
        // into here
        return coords[0] + "," + coords[1] + "," + 0 + "," + 0;
    }
}
