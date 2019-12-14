package GraphGrammars;

import javax.naming.SizeLimitExceededException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.time.LocalDateTime;
import java.util.*;

public class SpaceGraph {

    //List of all the rooms in this space graph
    private ArrayList<Room> rooms = new ArrayList<>();

    //List of all rooms in this graph with unused connections.
    private ArrayList<Room> viableRooms = new ArrayList<>();

    private int overflowCount = 0;

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

    //TODO - might be a good idea to ensure that build can never be called on the same graph
    // twice - either set the graph to empty at the beginning or have a bool that doesn't allow
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
        setupEntranceRoom(entranceNode);

        //Recursively handle each node in the mission graph
        try {
            useMissionGraphNode(entranceNode);
        } catch (StackOverflowError e) {
            overflowCount++;
            System.out.println("Houston we have overflow: " + overflowCount);
            rooms.clear();
            viableRooms.clear();
            if (overflowCount > 5) {
                throw new StackOverflowError("Something went wrong on our end, please try again");
            }
            build(mission);
        }
    }

    //TODO - decide if we need to worry about tightly connected edges...if we don't have rules
    // for them it might not make sense... not even really sure how we would add rules for them

    /**
     * Takes a mission graph node, and for each neighbor, continues to build the space graph
     *
     * @param node
     */
    private void useMissionGraphNode(MissionGraphNode node) throws StackOverflowError {
        MissionGraphEdge[] nodeConnections = node.getConnections();
        //in order: top, bottom, right, fill the graph using this node's connections
        //Leaving out getting ints from nodePositions to save time
        useHelper(node, nodeConnections[1]);
        useHelper(node, nodeConnections[3]);
        useHelper(node, nodeConnections[2]);
    }

    /**
     * Just a helper method for the useMissionGraphNode class
     *
     * @param node - the node we're moving from
     * @param edge - the edge of the input node that we want to move to next
     */
    private void useHelper(MissionGraphNode node, MissionGraphEdge edge) throws StackOverflowError {
        //If there really is a connection there, and it's pointing away from this node...
        if (edge != null && edge.getPointingTo() != node) {
            MissionGraphNode newNode = edge.getPointingTo();
            //Expand this graph based on that node
            extendGraph(newNode);
            //use the new node as well
            useMissionGraphNode(newNode);
        }
    }

    /**
     * Expand this graph
     *
     * @param newNode - MissionGraphNode to based this expansion on
     */
    private void extendGraph(MissionGraphNode newNode) throws StackOverflowError {
        //Select the rule to apply, and get its list of rooms
        ArrayList<Room> ruleRooms = selectRandomRule(newNode).rooms;
        //Spin the graph to add more randomness
        randomlySpinGraph(ruleRooms);
        boolean applicationFailed = true;
        int count = 0;
        while (applicationFailed) {
            count++;
            if (count >= 20) {
                extendGraph(newNode);
                break;
            }
            //Select a room to extend off of
            Room base = selectRandomViableRoom();
            //Select a side of the room to build off of
            nodePositions side = selectRandomSide(base);
            //adjust all the coords in the rule based on this room
            ArrayList<int[]> adjustedCoords = adjustCoords(ruleRooms, side, base);
            //Check if the rule fits, if so, apply it an leave the while loop
            if (checkRule(adjustedCoords)) {
                applyRule(base, side, ruleRooms, adjustedCoords);
                applicationFailed = false;
                updateConnections(base, ruleRooms);
            }
        }
    }

    //TODO - fix connections as well, not just coords
    private void randomlySpinGraph(ArrayList<Room> rooms) {
        //Randomly get the number of shifts to perform
        Random random = new Random();
        int shifts = random.nextInt(4);
        switch (shifts) {
            case 0:
                break;
            case 1:
                shiftNinety(rooms);
                break;
            case 2:
                shiftOneEighty(rooms);
                break;
            case 3:
                shiftTwoSeventy(rooms);
                break;
            default:
                throw new IndexOutOfBoundsException("This should be impossible");
        }
    }

    /**
     * Shift each room's coords by 90 degrees
     *
     * @param rooms
     */
    private void shiftNinety(ArrayList<Room> rooms) {
        for (Room room : rooms) {
            int[] coords = room.getCoords();
            room.setCoords(new int[]{-coords[1], coords[0]});
        }
    }

    /**
     * Shift each room's coords by 180 degrees
     *
     * @param rooms
     */
    private void shiftOneEighty(ArrayList<Room> rooms) {
        for (Room room : rooms) {
            int[] coords = room.getCoords();
            room.setCoords(new int[]{-coords[0], -coords[1]});
        }
    }

    /**
     * Shift each room's coords by 180 degrees
     *
     * @param rooms
     */
    private void shiftTwoSeventy(ArrayList<Room> rooms) {
        for (Room room : rooms) {
            int[] coords = room.getCoords();
            room.setCoords(new int[]{coords[1], -coords[0]});
        }
    }

    //TODO - should I lock up mini bosses as well?

    /**
     * Update roomsWithUnusedConnections
     *
     * @param base      - room being added to
     * @param ruleRooms - rooms being added
     */
    private void updateConnections(Room base, ArrayList<Room> ruleRooms) {
        //If the first room contains a lock, lock it and the base on the correct sides
        Room firstRuleRoom = ruleRooms.get(0);
        ArrayList<roomContents> firstRuleRoomContents = firstRuleRoom.getContents();
        if (firstRuleRoomContents.contains(roomContents.LOCK)
                || firstRuleRoomContents.contains(roomContents.FINAL_LOCK)
                || firstRuleRoomContents.contains(roomContents.LEVEL_BOSS)) {
            if (!(ruleRooms.size() == 1)) {
                throw new IndexOutOfBoundsException("Rules for locked nodes must only contain " +
                        "one room, if you like to add more complex structures, please add new " +
                        "alphabet symbols or mission rules");
            }
            if (firstRuleRoomContents.contains(roomContents.LOCK)
                    || firstRuleRoomContents.contains(roomContents.FINAL_LOCK)) {
                lock(base, firstRuleRoom);
            }
            //If a room in the graph is locked, we can't allow anything beyond it be placed
            // before it.
            viableRooms.clear();
            //We know that this room must have empty connections, because locked nodes can only
            // have one room.
            viableRooms.add(firstRuleRoom);
            //No reason to continue if this is a locked door
            return;
        }

        //Add new rooms if they have free connections
        for (Room room : ruleRooms) {
            if (!room.freeConnections().isEmpty()) {
                viableRooms.add(room);
            }
        }

        //Remove base if it has no more free connections
        if (base.freeConnections().isEmpty()) {
            viableRooms.remove(base);
        }
    }

    /**
     * Lock the shared connection between two rooms.
     *
     * @param firstRoom
     * @param secondRoom
     */
    private void lock(Room firstRoom, Room secondRoom) {
        Room[] firstRoomConnections = firstRoom.getConnections();
        //Find which of firstRoom's connections is to secondRoom and lock from both sides
        for (int i = 0; i < 4; i++) {
            if (firstRoomConnections[i] != null && firstRoomConnections[i].equals(secondRoom)) {
                firstRoom.lockDoor(nodePositions.getPositionForInt(i));
                secondRoom.lockDoor(nodePositions.getPositionForInt((i + 2) % 4));
                return;
            }
        }
        throw new IndexOutOfBoundsException("The two rooms to be locked must be connected");
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

        if (!(adjustedCoords.size() == ruleRooms.size())) {
            throw new IllegalArgumentException("ruleRooms and adjustedCoords must map to " +
                    "eachother exactly");
        }

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
    private Room selectRandomViableRoom() {
        Random random = new Random();
        return viableRooms.get(random.nextInt(viableRooms.size()));
    }

    /**
     * Selects the a random rule for replacement based on a terminal node in the mission graph
     */
    private SpaceGraph selectRandomRule(MissionGraphNode node) {
        //Ensure the node is terminal in the mission graph
        if (!node.isTerminal()) {
            throw new IllegalArgumentException("nodes used in the mission graph must be terminal");
        }

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
        if (!(entranceNode.getNodeType() == alphabet.ENTRANCE)) {
            throw new IllegalArgumentException("FIrst node must have type alphabet.ENTRANCE");
        }

        //Place the entrance node with position (0,0)
        Room entranceRoom = new Room(roomContents.ENTRACE);
        entranceRoom.setCoords(new int[]{0, 0});

        rooms.add(entranceRoom);
        viableRooms.add(entranceRoom);

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

    /**
     * Write the GV string for this spacegraph out to a file
     */
    public void writeGVStringToFile(LocalDateTime dateTime, boolean openFiles) throws IOException,
            InterruptedException {
        String baseDirString = System.getProperty("user.dir");
        //Name the folder with the current date and time
        String folderName = dateTime.toString().replaceAll(":", ".");
        File allDungeonsDir = new File(baseDirString, "dungeons");
        File dungeonDir = new File(allDungeonsDir, folderName);
        File gvDir = new File(dungeonDir, "GraphViz");
        File spaceFile = new File(gvDir, "spaceGraph.gv");

        try {
            createFilesForGV(allDungeonsDir, dungeonDir, gvDir, spaceFile);
            BufferedWriter writer = new BufferedWriter(new FileWriter(spaceFile));
            writer.write(getGVString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (openFiles) {
            Runtime rt = Runtime.getRuntime();
            Process spaceProcess =
                    rt.exec("fdp -Tpng " + baseDirString + "/dungeons/" + folderName + "/GraphViz" +
                            "/spaceGraph.gv -o " + baseDirString + "/dungeons/" + folderName +
                            "/GraphViz" +
                            "/space.png");
            spaceProcess.waitFor();
            Process openSpace = rt.exec("open " + baseDirString + "/dungeons/" + folderName +
                    "/GraphViz/space.png");
            openSpace.waitFor();
        }
    }

    private void createFilesForGV(File allDungeonsDir, File dungeonsDir, File gvDir,
                                  File spaceFile) throws IOException {
        allDungeonsDir.mkdir();
        dungeonsDir.mkdir();
        gvDir.mkdir();
        spaceFile.createNewFile();
    }

    /**
     * Returns a graph viz compliant string
     */
    public String getGVString() {
        StringBuilder gvString = new StringBuilder("graph space {\n");

        gvString.append(getRoomsGVNodeNames());

        for (Room room : rooms) {
            gvString.append(room.getGVString());
        }

        gvString.append("}");

        return gvString.toString();
    }

    /**
     * Returns the header for this graph's gv string
     */
    public String getRoomsGVNodeNames() {
        StringBuilder nodeNames = new StringBuilder("\nnode [shape=\"box\"]; ");

        for (Room room : rooms) {
            nodeNames.append(room.getGVNodeName()).append(" [pad=\"1.5,0.0\" pos=").append(room.getGVCoordsForPosition()).append("]").append("; ");
        }

        return nodeNames.append("\n").toString();
    }

    /**
     * Write strings for room generation in ASP to files
     *
     * @param dateTime - the dateTime used to name this dungeon
     */
    public void writeGenStringsToFiles(LocalDateTime dateTime) {
        //Name the folder with the current date and time
        String folderName = dateTime.toString().replaceAll(":", ".");

        //Add new files to this folder with gen strings and name = rooms coordinates w/no spaces
        for (Room room : rooms) {
            String fileName = room.getCoordsForLP() + ".lp";
            fileName = fileName.replaceAll("\\s", "");
            File allDungeonsDir = new File(System.getProperty("user.dir"), "dungeons");
            File dungeonDir = new File(allDungeonsDir, folderName);
            File generationDir = new File(dungeonDir, "generation");
            File roomFile = new File(generationDir, fileName);

            try {
                createFilesForRoomGen(allDungeonsDir, dungeonDir, generationDir, roomFile);
                BufferedWriter writer =
                        new BufferedWriter(new FileWriter(roomFile));
                writer.write(room.getGenerationString());
                writer.close();
            } catch (IOException e) {
                System.out.println("Error at: " + fileName);
                e.printStackTrace();
            }
        }
    }

    /**
     * Write strings for dungeon analysis to CSV files
     *
     * @param dateTime -  the dataTime used to name this dungeon
     */
    public void writeAnalysisStringsToFiles(LocalDateTime dateTime) {
        //Name the folder with the current date and time
        String fileName = dateTime.toString().replaceAll(":", ".");

        //Add new files to this folder with gen strings and name = rooms coordinates w/no spaces
        for (Room room : rooms) {
            File allDungeonsDir = new File(System.getProperty("user.dir"), "dungeons");
            File analysisDir = new File(allDungeonsDir, "analysis");
            File dungeonFile = new File(analysisDir, fileName + ".csv");

            try {
                createFilesForDungeonAnalysis(allDungeonsDir, analysisDir, dungeonFile);
                BufferedWriter writer = new BufferedWriter(new FileWriter(dungeonFile));
                writer.write(getDungeonAnalysisString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns a string containing all the necessary info to run dungeon analysis
     *
     * @return
     */
    private String getDungeonAnalysisString() {
        StringBuilder builder = new StringBuilder("rooms,xCoords,yCoords,numMonsters," +
                "miniBoss\n -1,0,0,0,0\n");

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            builder.append(i).append(",").append(room.getAnalysisString()).append("\n");
        }

        return builder.toString();
    }

    /**
     * Create the necessary files and folders to write dungeon analysis .csv files
     *
     * @param allDungeonsDir - directory where all dungeons go
     * @param analysisDir    - directory for this analysis file
     * @param dungeonFile    - the file for this dungeon
     */
    private static void createFilesForDungeonAnalysis(File allDungeonsDir,
                                                      File analysisDir, File dungeonFile) throws IOException {
        if (allDungeonsDir.mkdir()) {
            System.out.println("Dungeons dir created!");
        } else {
            System.out.println("Dungeons dir already exists!");
        }

        if (analysisDir.mkdir()) {
            System.out.println("analysis dir created!");
        } else {
            System.out.println("analysis dir already exists! " + analysisDir.getPath());
        }

        if (dungeonFile.createNewFile()) {
            System.out.println("dungeon file created!");
        } else {
            System.out.println("dungeon file already exists! " + dungeonFile.getPath());
        }

    }

    /**
     * Create the necessary files and folders to write room generation .lp files
     *
     * @param allDungeonsDir - directory where all dungeons go
     * @param dungeonDir     - directory where this dungeon goes (should be based on date and
     *                       time of creation
     * @param generationDir  - directory for these generation files
     * @param roomFile       - the file for this room
     * @throws IOException
     */
    private static void createFilesForRoomGen(File allDungeonsDir, File dungeonDir,
                                              File generationDir,
                                              File roomFile) throws IOException {
        if (!allDungeonsDir.mkdir()) {
            System.out.println("Dungeon dir already created!");
        } else {
            System.out.println("Dungeon dir created!");
        }

        if (dungeonDir.mkdir()) {
            System.out.println("Dungeon Dir Created for: " + dungeonDir.getPath());
        } else {
            System.out.println("dungeonDir already created: " + dungeonDir.getPath());
        }

        if (generationDir.mkdir()) {
            System.out.println("Generation dir created for:" + generationDir.getPath());
        } else {
            System.out.println("Generation dir already created: " + generationDir.getPath());
        }

        if (roomFile.createNewFile()) {
            System.out.println("Room file created for: " + roomFile.getPath());
        } else {
            System.out.println("Roomfile already created: " + roomFile.getPath());
        }
    }
}
