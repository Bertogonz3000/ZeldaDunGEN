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
}
