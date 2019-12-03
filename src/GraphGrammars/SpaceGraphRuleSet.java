package GraphGrammars;

import java.util.ArrayList;

public class SpaceGraphRuleSet {

    private int alphabetValue;

    public SpaceGraphRuleSet(int alphabetValue) {
        this.alphabetValue = alphabetValue;
    }

    public ArrayList<SpaceGraph> getRuleSet() {
        switch (alphabetValue) {
            case 0:
                return makeBossLevelSet();
            case 4:
                return makeKeySet();
            default:
                throw new IndexOutOfBoundsException("Alphabet value must be a real alphabet " +
                        "symbols " +
                        "numval");
        }
    }

    /**
     * Make rules for a boss level - it is simply one room containing a level boss
     *
     * @return
     */
    private ArrayList<SpaceGraph> makeBossLevelSet() {
        //Make arrayList of rules
        ArrayList<SpaceGraph> bossLevelSet = new ArrayList<>();

        //Make a room containing the level boss
        Room bossLevelRoom = new Room(roomContents.LEVEL_BOSS);

        //Make a new graph, add the room, and add that room to the set
        SpaceGraph newGraph = new SpaceGraph();
        newGraph.addRoom(bossLevelRoom);
        bossLevelSet.add(newGraph);

        return bossLevelSet;
    }

    private ArrayList<SpaceGraph> makeKeySet() {
        ArrayList<SpaceGraph> keySet = new ArrayList<>();

        SpaceGraph firstRule = makeFirstKeyRule();
        SpaceGraph secondRule = makeSecondKeyRule();

        keySet.add(firstRule);
        keySet.add(secondRule);

        return keySet;
    }

    private SpaceGraph makeSecondKeyRule() {
        SpaceGraph secondRule = new SpaceGraph();

        //initialize the rooms
        Room firstRoom = new Room();
        Room secondRoom = new Room();
        Room keyRoom = new Room(roomContents.KEY);

        addRoomForRuleset(secondRule, firstRoom, new int[]{0, 0}, new Room[]{});
        addRoomForRuleset(secondRule, secondRoom, new int[]{1, 0}, new Room[]{firstRoom, null,
                keyRoom, null});
        addRoomForRuleset(secondRule, keyRoom, new int[]{2, 0}, new Room[]{});

        return secondRule;
    }

    private SpaceGraph makeFirstKeyRule() {
        SpaceGraph firstRule = new SpaceGraph();

        //Initialize the three rooms
        Room firstRoom = new Room();
        Room secondRoom = new Room();
        Room keyRoom = new Room(roomContents.KEY);


        //Add them to the set, connected to eachother in the correct places.
        addRoomForRuleset(firstRule, firstRoom, new int[]{0, 0}, new Room[]{});
        addRoomForRuleset(firstRule, secondRoom, new int[]{1, 0}, new Room[]{firstRoom, keyRoom,
                null, null});
        addRoomForRuleset(firstRule, keyRoom, new int[]{1, 1}, new Room[]{});

        return firstRule;
    }

    /**
     * Add a rule to the given graph, with the given contents, at the given coords, with the
     * given connections
     *
     * @param graph       - the graph to add the room to
     * @param newRoom     - The new room to add to this graph
     * @param coords      - the coordinates of the room to be added
     * @param connections - the rooms that this room is connected to 0 is left, ints moving
     *                    clockwise from there
     */
    private void addRoomForRuleset(SpaceGraph graph, Room newRoom, int[] coords,
                                   Room[] connections) {
        if (coords.length != 2 || connections.length > 4) {
            throw new IllegalArgumentException("length of an array is incorrect");
        }

        //Set the coords of the new room
        newRoom.setCoords(coords);

        if (connections.length > 0) {
            //For each possible connection, check if there is one: if so, create it on both ends
            for (int i = 0; i < 4; i++) {
                Room connection;
                if (connections[i] != null) {
                    connection = connections[i];
                    newRoom.setConnection(nodePositions.getPositionForInt(i), connection);
                    connection.setConnection(nodePositions.getPositionForInt((i + 2) % 4), newRoom);
                }
            }
        }

        graph.addRoom(newRoom);
    }
}
