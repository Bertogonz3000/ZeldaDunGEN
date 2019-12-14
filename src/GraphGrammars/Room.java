package GraphGrammars;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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

    //Which doors in this room are locked (follows nodePositions conventions)
    private boolean[] lockedDoors = new boolean[4];

    //The number of each type of monster located in this room
    private HashMap<Integer, Integer> numMonstersByType;


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
     * Return this room's coordinates as a string formatted in such a way so it works with our
     * .lp file name format.
     *
     * @return
     */
    public String getCoordsForLP() {
        String x = coords[0] < 0 ? "neg" + Math.abs(coords[0]) :
                Integer.toString(Math.abs(coords[0]));
        String y = coords[1] < 0 ? "neg" + Math.abs(coords[1]) :
                Integer.toString(Math.abs(coords[1]));
        return x + "_" + y;
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
            //If the room should have monsters...
            if (roomShouldHaveMonsters()) {
                //if there are none, generate them!
                if (numMonstersByType == null) {
                    genRandomMonsters();
                }
                //Either way, add string to list!
                genBuilder.append(getMonsterString());
            }
        }

        return genBuilder.toString();
    }

    /**
     * Return whether or no this room should have monsters in it, regardless of how many
     *
     * @return
     */
    private boolean roomShouldHaveMonsters() {
        return contents.contains(roomContents.MONSTERS)
                || contents.contains(roomContents.EXPLORATION)
                || contents.contains(roomContents.KEY)
                || contents.contains(roomContents.FINAL_KEY);
    }

    /**
     * Generates random numbers of random types of monsters
     */
    private void genRandomMonsters() {
        int numMonsters;
        Random rand = new Random();
        numMonstersByType = new HashMap<>();

        //Randomly select the amount of monsters based on the type of room this is.
        if (contents.contains(roomContents.MONSTERS)) {
            numMonsters = rand.nextInt(5) + 4;
        } else {
            numMonsters = rand.nextInt(4);
        }

        //For each monster, randomly select a type
        for (int i = 0; i < numMonsters; i++) {
            int type = rand.nextInt(3) + 1;
            if (numMonstersByType.containsKey(type)) {
                numMonstersByType.put(type, numMonstersByType.get(type) + 1);
            } else {
                numMonstersByType.put(type, 1);
            }
        }
    }

    private String getMonsterString() {
        if (numMonstersByType == null) {
            throw new IllegalArgumentException("You must generate the number of monsters using " +
                    "genRandomMonsters before gettingMonster string");
        }
        StringBuilder builder = new StringBuilder();
        //For each monster type, append a string describing it and its # for generation
        for (Integer type : numMonstersByType.keySet()) {
            builder.append("monster_present(monster").append(type).append(",").append(numMonstersByType.get(type)).append(").\n");
        }

        return builder.toString();
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
                genBuilder.append("door(").append(nodePositions.getPositionForInt(i)).append(",").append(doorType).append(").\n");
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
        int totalMonsters = 0;
        //If this room should have monsters in it...
        if (roomShouldHaveMonsters()) {
            //If there are none yet, generate them!
            if (numMonstersByType == null) {
                genRandomMonsters();
            } else {
                //if they've already been generated, add them up!
                for (Integer num : numMonstersByType.values()) {
                    totalMonsters += num;
                }
            }
        }

        int miniBoss = contents.contains(roomContents.MINI_BOSS) ? 1 : 0;

        return coords[0] + "," + coords[1] + "," + totalMonsters + "," + miniBoss;
    }
}
