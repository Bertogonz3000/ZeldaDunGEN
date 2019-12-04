package GraphGrammars;

import java.util.ArrayList;

public class SpaceGraphRuleSet {

    private int alphabetValue;

    public SpaceGraphRuleSet(int alphabetValue) {
        this.alphabetValue = alphabetValue;
    }

    //TODO - add cases in here for each of the new rules I just made
    public ArrayList<SpaceGraph> getRuleSet() {
        switch (alphabetValue) {
            case 0:
                return makeBossLevelSet();
            case 1:
                return makeBossMiniSet();
            case 13:
                return makeMonsterRoomSet();
            case 4:
                return makeKeySet(false);
            case 11:
                return makeKeySet(true);
            case 6:
                return makeExplorationSet();
            case 5:
                return makeLockSet(false);
            case 12:
                return makeLockSet(true);
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

    private ArrayList<SpaceGraph> makeKeySet(boolean finalKey) {
        ArrayList<SpaceGraph> keySet = new ArrayList<>();

        SpaceGraph firstRule = makeFirstKeyRule(finalKey);
        SpaceGraph secondRule = makeSecondKeyRule(finalKey);

        keySet.add(firstRule);
        keySet.add(secondRule);

        return keySet;
    }

    private SpaceGraph makeSecondKeyRule(boolean finalKey) {
        SpaceGraph secondRule = new SpaceGraph();

        //initialize the rooms
        Room firstRoom = new Room();
        Room secondRoom = new Room();

        Room keyRoom;

        if (finalKey) {
            keyRoom = new Room(roomContents.FINAL_KEY);
        } else {
            keyRoom = new Room(roomContents.KEY);
        }

        addRoomForRuleset(secondRule, firstRoom, new int[]{0, 0}, new Room[]{});
        addRoomForRuleset(secondRule, secondRoom, new int[]{1, 0}, new Room[]{firstRoom, null,
                keyRoom, null});
        addRoomForRuleset(secondRule, keyRoom, new int[]{2, 0}, new Room[]{});

        return secondRule;
    }

    private SpaceGraph makeFirstKeyRule(boolean finalKey) {
        SpaceGraph firstRule = new SpaceGraph();

        //Initialize the three rooms
        Room firstRoom = new Room();
        Room secondRoom = new Room();

        Room keyRoom;

        if (finalKey) {
            keyRoom = new Room(roomContents.FINAL_KEY);
        } else {
            keyRoom = new Room(roomContents.KEY);
        }


        //Add them to the set, connected to eachother in the correct places.
        addRoomForRuleset(firstRule, firstRoom, new int[]{0, 0}, new Room[]{});
        addRoomForRuleset(firstRule, secondRoom, new int[]{1, 0}, new Room[]{firstRoom, keyRoom,
                null, null});
        addRoomForRuleset(firstRule, keyRoom, new int[]{1, 1}, new Room[]{});

        return firstRule;
    }

    private ArrayList<SpaceGraph> makeMonsterRoomSet() {
        ArrayList<SpaceGraph> rules = new ArrayList<>();

        SpaceGraph firstRule = new SpaceGraph();

        Room onlyRoom = new Room(roomContents.MONSTERS);

        addRoomForRuleset(firstRule, onlyRoom, new int[]{0, 0}, new Room[]{});

        rules.add(firstRule);

        return rules;
    }

    private ArrayList<SpaceGraph> makeBossMiniSet() {
        ArrayList<SpaceGraph> rules = new ArrayList<>();

        SpaceGraph firstRule = new SpaceGraph();

        Room onlyRoom = new Room(roomContents.MINI_BOSS);

        addRoomForRuleset(firstRule, onlyRoom, new int[]{0, 0}, new Room[]{});

        rules.add(firstRule);

        return rules;
    }

    private ArrayList<SpaceGraph> makeExplorationSet() {
        ArrayList<SpaceGraph> explorationSet = new ArrayList<>();

        SpaceGraph firstRule = new SpaceGraph();

        Room one = new Room();
        Room two = new Room();
        Room three = new Room();
        Room four = new Room();
        Room five = new Room();

        addRoomForRuleset(firstRule, one, new int[]{0, 0}, new Room[]{});
        addRoomForRuleset(firstRule, two, new int[]{0, 1}, new Room[]{three, four, five, one});
        addRoomForRuleset(firstRule, three, new int[]{-1, 1}, new Room[]{});
        addRoomForRuleset(firstRule, four, new int[]{0, 2}, new Room[]{});
        addRoomForRuleset(firstRule, five, new int[]{1, 1}, new Room[]{});

        explorationSet.add(firstRule);

        return explorationSet;
    }

    private ArrayList<SpaceGraph> makeLockSet(boolean finalLock) {
        ArrayList<SpaceGraph> lockSet = new ArrayList<>();

        SpaceGraph firstrule = new SpaceGraph();

        Room onlyRoom = new Room();

        if (finalLock) {
            onlyRoom.addContents(roomContents.FINAL_LOCK);
        } else {
            onlyRoom.addContents(roomContents.LOCK);
        }

        addRoomForRuleset(firstrule, onlyRoom, new int[]{0, 0}, new Room[]{});

        lockSet.add(firstrule);

        return lockSet;
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
