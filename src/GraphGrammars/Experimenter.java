package GraphGrammars;

import java.io.IOException;

public class Experimenter {

    public static void main(String[] args) {
        try {
            testFullPipeline();
        } catch (Exception e) {
            System.out.println("WOOPS!");
        }
    }

    public static void testFullPipeline() throws IOException, InterruptedException {
        System.out.println("Running...");
        MissionGraph testMission = new MissionGraph(new MissionGraphNode(alphabet.START));
        System.out.println("Starting Mission Replacements...");
        testMission.runReplacements();
        System.out.println("Mission replacements done");
        System.out.println("\nBuilding Space Graph...");
        SpaceGraph testSpace = new SpaceGraph(testMission);
        System.out.println("\nSpace Graph Complete \n");

        System.out.println("Writing to files...");
        testMission.writeToOutputFile();
        testSpace.writeToOutputFile();

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

    public static void testSpaceGraphRuleSelection() {
        MissionGraphNode entranceNode = new MissionGraphNode(alphabet.ENTRANCE);
        MissionGraph testGraph = new MissionGraph(entranceNode);
        MissionReplacementRuleMaker.addNodeLinear(testGraph, alphabet.KEY, entranceNode, false);

        SpaceGraph testSpace = new SpaceGraph(testGraph);
    }

    public static void testMissionGraphReplacements() {
        System.out.println("Running...");
        MissionGraph testGraph = new MissionGraph(new MissionGraphNode(alphabet.START));
        System.out.println("Starting Mission Replacements...");
        testGraph.runReplacements();
        System.out.println("Mission Replacements Done, mission graph: \n");
        System.out.println(testGraph);
        System.out.println("\n");
    }

    public static void testSpaceRule(alphabet symbol) {
        SpaceGraphRuleSet sgrs = new SpaceGraphRuleSet(symbol.getNumVal());
        System.out.println(sgrs.getRuleSet());
    }
}