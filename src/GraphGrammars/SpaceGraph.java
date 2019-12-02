package GraphGrammars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class SpaceGraph {

    //List of all the rooms in this space graph
    private ArrayList<Room> rooms = new ArrayList<>();

    //List of all rooms in this graph with unused connections.
    private ArrayList<Room> roomsWithUnusedConnections = new ArrayList<>();

    /**
     * Empty constructor used for building rules
     */
    public SpaceGraph() {
    }

    /**
     * Constructor used for building a usable dungeon space based on a mission
     *
     * @param mission - the mission this space graph will be based on.
     */
    public SpaceGraph(MissionGraph mission) {
        build(mission);
    }

    /**
     * Add a room to this dungeon without addressing its location.
     *
     * @param room
     */
    public void addRoom(Room room) {
        rooms.add(room);
    }

    //TODO - when making new connections, be sure to make the connection on both sides
    //TODO - consider taking addRoomForRuleset from SpaceGraphRuleSet, or at least its logic
    //TODO - might be a good idea to ensure that build can never be called on the same graph
    // twice - either set the graph to empty at the beginning or have a bool that doens't allow
    // it to be built without being cleared first

    /**
     * Builds the space graph based on the mission graph input
     *
     * @param mission - the mission graph input
     */
    private void build(MissionGraph mission) {
        //Get the first node in the graph
        MissionGraphNode entranceNode = mission.getNodes().get(0);

        //Ensure it's an entrance node and, if so, set it up as the first node in the graph, with
        // all unused doors
        Room entranceRoom = setupEntranceRoom(entranceNode);
        roomsWithUnusedConnections.add(entranceRoom);

        //Recursively handle each node in the mission graph
        useMissionGraphNode(entranceNode);

        //For each  node in the mission graph...

        //  Iterate over its connections (top, right, and bottom, since it goes left to right).
        //  For each one found...
        //      Get the rules associated with it...
        //      while a successful rule hasn't been chosen...
        //          Randomly Choose one, and mark it as chosen...somehow
        //          Then, choose a random open position on the space graph (these should all be
        //          added to an
        //          array or something)
        //          TODO - decide if we want to also choose a random spot on the rule to connect
        //           to...this could lead to more trouble though, so IDK
        //          With this position in the space graph, check if the selected rule would fit
        //          based on
        //              its position in 2D space
        //          If it doesn't fit, loop again
        //      Apply the rule by adding it to the space graph
    }

    //TODO - decide if we need to worry about tightly connected edges...if we don't have rules
    // for them it might not make sense... not even really sure how we would add rules for them
    //TODO - should we check for if nodes have already been used? This won't be an issue if the
    // nodes can't circle back around

    /**
     * Takes a mission graph node, and for each neighbor, continues to build the space graph
     *
     * @param node
     */
    private void useMissionGraphNode(MissionGraphNode node) {
        MissionGraphEdge[] nodeConnections = node.getConnections();
        //For each possible edge connected to the given node...
        for (int i = 0; i < nodeConnections.length; i++) {
            //If there really is a connection there, and it's pointing away from this node...
            if (nodeConnections[i] != null && nodeConnections[i].getPointingTo() != node) {
                MissionGraphNode newNode = nodeConnections[i].getPointingTo();
                //Expand this graph based on that node
                extendGraph(newNode);
                //use the new node as well
                useMissionGraphNode(newNode);
            }
        }
    }

    /**
     * Expand this graph
     *
     * @param newNode - MissionGraphNode to based this expansion on
     */
    private void extendGraph(MissionGraphNode newNode) {
        //Select the rule to apply, and get its list of rooms
        ArrayList<Room> ruleRooms = selectRandomRule(newNode).rooms;
        boolean applicationFailed = true;
        while (applicationFailed) {
            //Select a room to extend off of
            Room base = selectRandomRoomWithAvailableDoors();
            //Select a side of the room to build off of
            nodePositions side = selectRandomSide(base);
            //adjust all the coords in the rule based on this room
            ArrayList<int[]> adjustedCoords = adjustCoords(ruleRooms, side, base);
            //Check if the rule fits, if so, apply it an leave the while loop
            if (checkRule(adjustedCoords)) {
                //TODO - pickup here
                applyRule(base, side, ruleRooms, adjustedCoords);
                applicationFailed = false;
            }
        }
    }

    //TODO - do we ever want to connect two rooms that aren't connected by rules?  Maybe this is
    // some post-processing we can do?

    /**
     * Apply a rule to the graph at the given base room on side side
     *
     * @param base           - base room to add the graph to
     * @param side           - side of base room to add to
     * @param ruleRooms      - rooms to add
     * @param adjustedCoords - adjusted coordinates of the rooms (should be calculated during
     *                       rule checking step)
     */
    private void applyRule(Room base, nodePositions side, ArrayList<Room> ruleRooms,
                           ArrayList<int[]> adjustedCoords) {
        assert adjustedCoords.size() == ruleRooms.size() : "ruleRooms and adjustedCoords must map" +
                " to eachother exactly";

        //for each new node: add to graph, adjust coords, and setup connections
        for (int i = 0; i < ruleRooms.size(); i++) {
            Room newRoom = ruleRooms.get(i);
            addRoom(newRoom);
            newRoom.setCoords(adjustedCoords.get(i));
        }

        //TODO - decide if this should be checked or if we can just be big bois and gorls and
        // follow the rules
        //Since the rooms in a rule should already be connected, the only needed new connection
        // is the base to the first room
        connect(base, side, ruleRooms.get(0));
    }

    /**
     * Connect first room to second room via firstRoom's sideOfFirstRoom side
     *
     * @param firstRoom       - first room in connection
     * @param sideOfFirstRoom - the side of first room that the connection will be made on -
     *                        opposite connection will be made for secondRoom
     * @param secondRoom      - second room in connection
     */
    private void connect(Room firstRoom, nodePositions sideOfFirstRoom, Room secondRoom) {
        firstRoom.setConnection(sideOfFirstRoom, secondRoom);
        int firstSideNumVal = sideOfFirstRoom.getNumVal();
        nodePositions sideOfSecondRoom = nodePositions.getPositionForInt((firstSideNumVal + 2) % 4);
        secondRoom.setConnection(sideOfSecondRoom, firstRoom);
    }

    //TODO - get rid of this testing method
//    public void testSomeShit() {
//        Room entrance = new Room(roomContents.ENTRACE);
//        entrance.setCoords(new int[]{0, 0});
//
//        Room one = new Room();
//        one.setCoords(new int[]{0, 0});
//
//        Room two = new Room();
//        two.setCoords(new int[]{1, 0});
//
//        Room three = new Room();
//        three.setCoords(new int[]{1, 1});
//
//        connect(one, nodePositions.RIGHT, two);
//        connect(two, nodePositions.TOP, three);
//
//        ArrayList<Room> rule = new ArrayList<>();
//        rule.add(one);
//        rule.add(two);
//        rule.add(three);
//
//        applyRule(entrance, nodePositions.RIGHT, rule, adjustCoords(rule, nodePositions.RIGHT,
//                entrance));
//
//        System.out.println(entrance);
//        System.out.println(one);
//        System.out.println(two);
//        System.out.println(three);
//    }

    /**
     * Check to make sure the given rule works with the given room
     *
     * @param adjustedCoords- coords in the rule to check
     * @return - whether or not the rule works
     */
    private boolean checkRule(ArrayList<int[]> adjustedCoords) {
        //Check each of these adjusted coordinates against each room currently in the graph
        for (int[] ruleCoords : adjustedCoords) {
            for (Room room : rooms) {
                if (Arrays.equals(ruleCoords, room.getCoords())) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * @param ruleRooms - the rooms in the rule we are checking
     * @param side      - the side of the base room to add this rule to
     * @param base      - the base room
     * @return - adjusted room coordinates based on
     */
    private ArrayList<int[]> adjustCoords(ArrayList<Room> ruleRooms, nodePositions side,
                                          Room base) {
        ArrayList<int[]> adjustedCoords = new ArrayList<>();
        int[] baseCoords = base.getCoords();

        //Set the origin of the rule to be to one side of the base
        int[] newOriginCoords = new int[2];
        switch (side) {
            case LEFT:
                newOriginCoords = new int[]{baseCoords[0] - 1, baseCoords[1]};
                break;
            case RIGHT:
                newOriginCoords = new int[]{baseCoords[0] + 1, baseCoords[1]};
                break;
            case TOP:
                newOriginCoords = new int[]{baseCoords[0], baseCoords[1] + 1};
                break;
            case BOTTOM:
                newOriginCoords = new int[]{baseCoords[0], baseCoords[1] - 1};
                break;
        }

        //TODO - come here if there are issues with placement

        adjustedCoords.add(newOriginCoords);

        //Now that we have the new origin, each new adjusted coords becomes origin+Coords
        for (int i = 1; i < ruleRooms.size(); i++) {
            int[] nextCoords = ruleRooms.get(i).getCoords();
            adjustedCoords.add(new int[]{newOriginCoords[0] + nextCoords[0],
                    newOriginCoords[1] + nextCoords[1]});
        }

        return adjustedCoords;
    }

    /**
     * Get a random nodePositions value
     *
     * @return - a random nodePositions value
     */
    private nodePositions selectRandomSide(Room base) {
        Random random = new Random();
        ArrayList<nodePositions> freeConnections = base.freeConnections();
        return freeConnections.get(random.nextInt(freeConnections.size()));
    }


    /**
     * Get a random room from the list of rooms that still have empty connections
     *
     * @return
     */
    private Room selectRandomRoomWithAvailableDoors() {
        Random random = new Random();
        return roomsWithUnusedConnections.get(random.nextInt(roomsWithUnusedConnections.size()));
    }

    /**
     * Selects the a random rule for replacement based on a terminal node in the mission graph
     */
    private SpaceGraph selectRandomRule(MissionGraphNode node) {
        //Ensure the node is terminal in the mission graph
        assert node.isTerminal() : "nodes used in the mission graph must be terminal";

        //Get all possible rules for this node
        ArrayList<SpaceGraph> possibleRules = node.getNodeType().getSpaceRules();

        //Randomly select a rule
        Random random = new Random();
        return possibleRules.get(random.nextInt(possibleRules.size()));
    }


    /**
     * Setup the first room in the dungeon
     *
     * @param entranceNode - the first node in the mission graph, if this isn't an entrance,
     *                     throw an error
     * @return - the first room in the dungeon
     */
    private Room setupEntranceRoom(MissionGraphNode entranceNode) {
        //if this node is not an entrance node, throw an error
        assert entranceNode.getNodeType() == alphabet.ENTRANCE : "First node must have type " +
                "alphabet" +
                ".ENTRACE";

        //Place the entrance node with position (0,0)
        Room entranceRoom = new Room(roomContents.ENTRACE);
        entranceRoom.setCoords(new int[]{0, 0});

        return entranceRoom;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n");
        for (Room room : rooms) {
            builder.append(room.toString()).append("\n");
        }

        return builder.toString();
    }
}
