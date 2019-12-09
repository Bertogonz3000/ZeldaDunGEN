package GraphGrammars;

import java.io.IOException;

public class Experimenter {

    public static void main(String[] args) {
        try {
            runEverything();
        } catch (Exception e) {
            System.out.println("WOOPS!");
            e.printStackTrace();
        }
    }

    private static void testFileWriting() {
        SpaceGraphRuleSet rules = new SpaceGraphRuleSet(13);
        SpaceGraph set = rules.getRuleSet().get(1);
        System.out.println("writing genstrings....");
        set.writeGenStringsToFiles();
        System.out.println("Done!");
    }

    private static void testGenStrings() {
        Room monsterRoom = new Room(roomContents.MONSTERS);
        Room keyRoom = new Room(roomContents.KEY);
        Room finalKeyRoom = new Room(roomContents.FINAL_KEY);
        Room miniBossRoom = new Room(roomContents.MINI_BOSS);
        Room levelBossRoom = new Room(roomContents.LEVEL_BOSS);
        Room goalRoom = new Room(roomContents.GOAL);
        Room entrance = new Room(roomContents.ENTRACE);
        Room rupeeRoom = new Room(roomContents.RUPEE);
        Room exploration = new Room(roomContents.EXPLORATION);
        Room lockRoom = new Room(roomContents.LOCK);
        Room finalLockRoom = new Room(roomContents.FINAL_LOCK);

        exploration.setConnection(nodePositions.RIGHT, lockRoom);
        lockRoom.setConnection(nodePositions.LEFT, exploration);
        exploration.lockDoor(nodePositions.RIGHT);

        System.out.println("miniboss:");
        System.out.println(miniBossRoom.getGenerationString());
        System.out.println("monster:");
        System.out.println(monsterRoom.getGenerationString());
        System.out.println("key:");
        System.out.println(keyRoom.getGenerationString());
        System.out.println("finalkey:");
        System.out.println(finalKeyRoom.getGenerationString());
        System.out.println("levelBoss:");
        System.out.println(levelBossRoom.getGenerationString());
        System.out.println("entrance:");
        System.out.println(entrance.getGenerationString());
        System.out.println("finalLock:");
        System.out.println(finalLockRoom.getGenerationString());
        System.out.println("rupee:");
        System.out.println(rupeeRoom.getGenerationString());
        System.out.println("goal:");
        System.out.println(goalRoom.getGenerationString());
        System.out.println("exploration:");
        System.out.println(exploration.getGenerationString());
        System.out.println("lock:");
        System.out.println(lockRoom.getGenerationString());
    }

    private static void runEverything() throws IOException, InterruptedException {
        System.out.println("Running...");
        MissionGraph testMission = new MissionGraph(new MissionGraphNode(alphabet.START));
        System.out.println("Starting Mission Replacements...");
        testMission.runReplacements();
        System.out.println("Mission replacements done");
        System.out.println("\nBuilding Space Graph...");
        SpaceGraph testSpace = new SpaceGraph(testMission);
        System.out.println("\nSpace Graph Complete \n");
        System.out.println("Writing to files...");
        System.out.println("Writing GV files...");
        testMission.writeToOutputFile();
        testSpace.writeToOutputFile();

        System.out.println("Writing gen files...");
        testSpace.writeGenStringsToFiles();

        System.out.println("Running commands...");

        Runtime rt = Runtime.getRuntime();

        Process missionProcess = rt.exec("dot -Tpng /Users/berto/Desktop/Pomona4thyr/ai" +
                "/final_project/gvStuff/missionGraph.gv -o " +
                "/Users/berto/Desktop/Pomona4thyr/ai/final_project/gvStuff/mission.png");

        missionProcess.waitFor();

        Process openMission = rt.exec("open /Users/berto/Desktop/Pomona4thyr/ai/final_project" +
                "/gvStuff/mission.png");

        openMission.waitFor();

        Process spaceProcess = rt.exec("fdp -Tpng /Users/berto/Desktop/Pomona4thyr/ai" +
                "/final_project/gvStuff/spaceGraph.gv -o " +
                "/Users/berto/Desktop/Pomona4thyr/ai/final_project/gvStuff/space.png");

        spaceProcess.waitFor();

        Process openSpace = rt.exec("open /Users/berto/Desktop/Pomona4thyr/ai/final_project" +
                "/gvStuff/space.png");

        openSpace.waitFor();

        System.out.println("Complete!");
    }

    private static void testSpaceGraphRuleSelection() {
        MissionGraphNode entranceNode = new MissionGraphNode(alphabet.ENTRANCE);
        MissionGraph testGraph = new MissionGraph(entranceNode);
        MissionReplacementRuleMaker.addNodeLinear(testGraph, alphabet.KEY, entranceNode, false);

        SpaceGraph testSpace = new SpaceGraph(testGraph);
    }

    private static void testMissionGraphReplacements() {
        System.out.println("Running...");
        MissionGraph testGraph = new MissionGraph(new MissionGraphNode(alphabet.START));
        System.out.println("Starting Mission Replacements...");
        testGraph.runReplacements();
        System.out.println("Mission Replacements Done, mission graph: \n");
        System.out.println(testGraph);
        System.out.println("\n");
    }

    private static void testSpaceRule(alphabet symbol) {
        SpaceGraphRuleSet sgrs = new SpaceGraphRuleSet(symbol.getNumVal());
        System.out.println(sgrs.getRuleSet());
    }
}