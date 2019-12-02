package GraphGrammars;

public class Experimenter {

    public static void main(String[] args) {
//        SpaceGraph testGraph = new SpaceGraph();
//        testGraph.testSomeShit();
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