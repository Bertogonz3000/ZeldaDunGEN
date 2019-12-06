package GraphGrammars;

import java.util.ArrayList;

public class MissionReplacementRuleMaker {

    private int alphabetValue;

    //TODO - insert more keys and locks (ex: around chains in start rules), create more and
    // varied start rules.

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
                throw new IndexOutOfBoundsException("Illegal alphabetValue for MRRM: " + alphabetValue);
        }
    }

    //TODO - there may be too many mini bosses now
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

        MissionGraphNode secondFirstNode = new MissionGraphNode(alphabet.EXPLORATION);

        MissionGraph secondRule = new MissionGraph(secondFirstNode);

        addNodeLinear(secondRule, alphabet.MONSTER_ROOM, secondFirstNode, false);
        addNodeLinear(secondRule, alphabet.BOSS_MINI,
                secondRule.getNodes().get(secondRule.getNodes().size() - 1), false);

        chainRules.add(secondRule);

        MissionGraph thirdRule = new MissionGraph(new MissionGraphNode(alphabet.KEY));

        addNodeLinear(thirdRule, alphabet.CHAIN,
                thirdRule.getNodes().get(thirdRule.getNodes().size() - 1), false);
        addNodeLinear(thirdRule, alphabet.LOCK,
                thirdRule.getNodes().get(thirdRule.getNodes().size() - 1), false);

        chainRules.add(thirdRule);

        MissionGraph fourthRule = new MissionGraph(new MissionGraphNode(alphabet.KEY));

        addNodeLinear(fourthRule, alphabet.MONSTER_ROOM,
                fourthRule.getNodes().get(fourthRule.getNodes().size() - 1), false);
        addNodeLinear(fourthRule, alphabet.BOSS_MINI,
                fourthRule.getNodes().get(fourthRule.getNodes().size() - 1), false);
        addNodeLinear(fourthRule, alphabet.LOCK,
                fourthRule.getNodes().get(fourthRule.getNodes().size() - 1), false);

        chainRules.add(fourthRule);

        MissionGraph fifthRule = new MissionGraph(new MissionGraphNode(alphabet.KEY));

        //TODO should I add a mini boss to this rule? maybe take one out of another? already some
        // have a lot
        addNodeLinear(fifthRule, alphabet.MONSTER_ROOM,
                fifthRule.getNodes().get(fifthRule.getNodes().size() - 1), false);
        addNodeLinear(fifthRule, alphabet.LOCK,
                fifthRule.getNodes().get(fifthRule.getNodes().size() - 1), false);
        addNodeLinear(fifthRule, alphabet.KEY,
                fifthRule.getNodes().get(fifthRule.getNodes().size() - 1), false);
        addNodeLinear(fifthRule, alphabet.EXPLORATION,
                fifthRule.getNodes().get(fifthRule.getNodes().size() - 1), false);
        addNodeLinear(fifthRule, alphabet.LOCK,
                fifthRule.getNodes().get(fifthRule.getNodes().size() - 1), false);

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
