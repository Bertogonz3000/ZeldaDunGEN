package GraphGrammars;

import java.util.ArrayList;

public class MissionReplacementRuleMaker {

    private int alphabetValue;

    /**
     * 0 param constructor
     */
    public MissionReplacementRuleMaker(int alphabetValue) {
        this.alphabetValue = alphabetValue;
    }

    public ArrayList<MissionGraph> getReplacementRuleSet() {
        switch (alphabetValue) {
            case 7:
                return makeChainRules();
            case 8:
                return makeFinalChainRules();
            case 10:
                return makeStartRules();
            default:
                throw new IndexOutOfBoundsException("Illegal alphabetValue for MRRM");
        }
    }

    private static ArrayList<MissionGraph> makeChainRules() {
        ArrayList<MissionGraph> chainRules = new ArrayList<>();

        MissionGraph firstRule = new MissionGraph(new MissionGraphNode(alphabet.MONSTER_ROOM));

        addNodeLinear(firstRule, alphabet.KEY,
                firstRule.getNodes().get(firstRule.getNodes().size() - 1), true);
        addNodeLinear(firstRule, alphabet.LOCK,
                firstRule.getNodes().get(firstRule.getNodes().size() - 1), false);
        addNodeLinear(firstRule, alphabet.BOSS_MINI,
                firstRule.getNodes().get(firstRule.getNodes().size() - 1), true);

        chainRules.add(firstRule);

        return chainRules;
    }

    private static ArrayList<MissionGraph> makeFinalChainRules() {
        ArrayList<MissionGraph> finalChainRules = new ArrayList<>();

        MissionGraph firstRule = new MissionGraph(new MissionGraphNode(alphabet.FINAL_KEY));

        addNodeLinear(firstRule, alphabet.FINAL_LOCK,
                firstRule.getNodes().get(firstRule.getNodes().size() - 1), false);
        addNodeLinear(firstRule, alphabet.BOSS_LEVEL,
                firstRule.getNodes().get(firstRule.getNodes().size() - 1), false);

        finalChainRules.add(firstRule);

        return finalChainRules;
    }

    /**
     * Create replacement rules for the start node
     *
     * @return
     */
    private static ArrayList<MissionGraph> makeStartRules() {
        ArrayList<MissionGraph> startRules = new ArrayList<>();

        MissionGraph oneChain = entranceAndChains(1);
        MissionGraph twoChains = entranceAndChains(2);
        MissionGraph threeChains = entranceAndChains(3);

        startRules.add(oneChain);
        startRules.add(twoChains);
        startRules.add(threeChains);

        return startRules;
    }

    private static MissionGraph entranceAndChains(int numChains) {
        MissionGraphNode entranceNode = new MissionGraphNode(alphabet.ENTRANCE);

        MissionGraph newRule = new MissionGraph(entranceNode);

        for (int i = 0; i < numChains; i++) {
            MissionGraphNode prevNode = newRule.getNodes().get(i);
            MissionGraphNode newChainNode = new MissionGraphNode(alphabet.CHAIN);

            MissionGraphEdge newEdge = new MissionGraphEdge(prevNode, newChainNode, false);
            prevNode.setConnection(nodePositions.RIGHT, newEdge);
            newChainNode.setConnection(nodePositions.LEFT, newEdge);

            newRule.addNode(newChainNode);
        }

        MissionGraphNode finalChainNode = new MissionGraphNode(alphabet.FINAL_CHAIN);
        MissionGraphNode penUltimateNode = newRule.getNodes().get(newRule.getNodes().size() - 1);
        MissionGraphEdge finalEdge =
                new MissionGraphEdge(penUltimateNode, finalChainNode, false);

        finalChainNode.setConnection(nodePositions.LEFT, finalEdge);
        penUltimateNode.setConnection(nodePositions.RIGHT, finalEdge);

        newRule.addNode(finalChainNode);

        return newRule;
    }

    public static void addNodeLinear(MissionGraph graph, alphabet roomType,
                                     MissionGraphNode prevNode, boolean tightCoupling) {
        MissionGraphNode newNode = new MissionGraphNode(roomType);
        graph.addNode(newNode);

        MissionGraphEdge newEdge = new MissionGraphEdge(prevNode, newNode, tightCoupling);

        prevNode.setConnection(nodePositions.RIGHT, newEdge);
        newNode.setConnection(nodePositions.LEFT, newEdge);
    }
}
