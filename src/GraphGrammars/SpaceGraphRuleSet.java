package GraphGrammars;

import java.util.ArrayList;
import java.util.Random;

public class SpaceGraphRuleSet {

    //TODO - create more rules like the + shaped one, they seem to offer excellent branching and
    // more classic dungeon structures.  Don't think we need to worry about restricting and
    // opening up new viable corners if we just have better structures like that

    private int alphabetValue;

    public SpaceGraphRuleSet(int alphabetValue) {
        this.alphabetValue = alphabetValue;
    }

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
            case 3:
                return makeGoalSet();
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
        SpaceGraph thirdRule = makeThirdKeyRule(finalKey);

        keySet.add(firstRule);
        keySet.add(secondRule);
        keySet.add(thirdRule);

        return keySet;
    }

    private SpaceGraph makeThirdKeyRule(boolean finalKey) {
        SpaceGraph thirdRule = new SpaceGraph();

        Room firstRoom = new Room();
        Room keyRoom = finalKey ? new Room(roomContents.FINAL_KEY) : new Room(roomContents.KEY);

        addRoomForRuleset(thirdRule, firstRoom, new int[]{0, 0}, new Room[]{});
        addRoomForRuleset(thirdRule, keyRoom, new int[]{0, 1}, new Room[]{null, null, null,
                firstRoom});

        return thirdRule;
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

    private ArrayList<SpaceGraph> makeGoalSet() {
        ArrayList<SpaceGraph> goalSet = new ArrayList<>();

        SpaceGraph rule = new SpaceGraph();

        Room goalRoom = new Room(roomContents.GOAL);

        addRoomForRuleset(rule, goalRoom, new int[]{0, 0}, new Room[]{});

        goalSet.add(rule);

        return goalSet;
    }

    private ArrayList<SpaceGraph> makeMonsterRoomSet() {
        ArrayList<SpaceGraph> rules = new ArrayList<>();

        SpaceGraph firstRule = new SpaceGraph();

        Room onlyRoom = new Room(roomContents.MONSTERS);

        addRoomForRuleset(firstRule, onlyRoom, new int[]{0, 0}, new Room[]{});

        rules.add(firstRule);


        SpaceGraph secondRule = new SpaceGraph();

        Room monsterRoom = new Room(roomContents.MONSTERS);
        Room rupeeRoom = new Room(roomContents.RUPEE);

        addRoomForRuleset(secondRule, monsterRoom, new int[]{0, 0}, new Room[]{});
        addRoomForRuleset(secondRule, rupeeRoom, new int[]{0, 1}, new Room[]{null, null, null,
                monsterRoom});

        rules.add(secondRule);


        SpaceGraph thirdRule = new SpaceGraph();

        Room first = new Room(roomContents.MONSTERS);
        Room second = new Room(roomContents.MONSTERS);
        Room third = new Room(roomContents.RUPEE);
        Room fourth = new Room(roomContents.RUPEE);
        Room fifth = new Room(roomContents.RUPEE);

        addRoomForRuleset(thirdRule, first, new int[]{0, 0}, new Room[]{});
        addRoomForRuleset(thirdRule, second, new int[]{0, 1}, new Room[]{fourth, fifth, third,
                first});
        addRoomForRuleset(thirdRule, third, new int[]{1, 1}, new Room[]{});
        addRoomForRuleset(thirdRule, fourth, new int[]{-1, 1}, new Room[]{});
        addRoomForRuleset(thirdRule, fifth, new int[]{0, 2}, new Room[]{});

        rules.add(thirdRule);

        return rules;
    }

    //TODO - decide if mini bosses should always have rewards
    //TODO - consider playing with this and with monsters - maybe mini-bosses should be the ones
    // that get three rupees at the end.
    private ArrayList<SpaceGraph> makeBossMiniSet() {
        ArrayList<SpaceGraph> rules = new ArrayList<>();

        SpaceGraph firstRule = new SpaceGraph();

        Room onlyRoom = new Room(roomContents.MINI_BOSS);

        addRoomForRuleset(firstRule, onlyRoom, new int[]{0, 0}, new Room[]{});

        rules.add(firstRule);


        SpaceGraph secondRule = new SpaceGraph();

        Room bossRoom = new Room(roomContents.MINI_BOSS);
        Room rupeeRoom = new Room(roomContents.RUPEE);

        //left, top, right, bottom
        addRoomForRuleset(secondRule, bossRoom, new int[]{0, 0}, new Room[]{});
        addRoomForRuleset(secondRule, rupeeRoom, new int[]{0, 1}, new Room[]{null, null, null,
                bossRoom});

        rules.add(secondRule);

        return rules;
    }

    private ArrayList<SpaceGraph> makeExplorationSet() {
        ArrayList<SpaceGraph> explorationSet = new ArrayList<>();

        SpaceGraph firstRule = new SpaceGraph();

        Room one = new Room(getRandomContents());
        Room two = new Room(getRandomContents());
        Room three = new Room(getRandomContents());
        Room four = new Room(getRandomContents());
        Room five = new Room(getRandomContents());

        addRoomForRuleset(firstRule, one, new int[]{0, 0}, new Room[]{});
        addRoomForRuleset(firstRule, two, new int[]{0, 1}, new Room[]{three, four, five, one});
        addRoomForRuleset(firstRule, three, new int[]{-1, 1}, new Room[]{});
        addRoomForRuleset(firstRule, four, new int[]{0, 2}, new Room[]{});
        addRoomForRuleset(firstRule, five, new int[]{1, 1}, new Room[]{});

        explorationSet.add(firstRule);

        return explorationSet;
    }

    /**
     * To get some real randomness in the mix, return a random roomContents for the exploration
     * rules.
     *
     * @return
     */
    private roomContents getRandomContents() {
        Random contentsSelector = new Random();

        switch (contentsSelector.nextInt(3)) {
            case 0:
                return roomContents.MONSTERS;
            case 1:
                return roomContents.RUPEE;
            case 2:
                return roomContents.EXPLORATION;
            default:
                throw new IndexOutOfBoundsException("This shouldn't be possible");
        }
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
