package graphs;

import java.util.ArrayList;

public class ReplacementRuleMaker {

    /**
     * 0 param constructor
     */
    public ReplacementRuleMaker() {
    }

    public ArrayList<MissionGraph> makeStartRules() {
        ArrayList<MissionGraph> startRules = new ArrayList<>();

        //TODO - have this instead read from a text document to pull in the rules, cuz this
        // fuckin sucks
        MissionGraphNode entranceNode = new MissionGraphNode(alphabet.ENTRANCE);
        MissionGraphNode firstChainNode = new MissionGraphNode(alphabet.CHAIN);
        entranceNode.setConnection(nodePositions.RIGHT, new MissionGraphEdge(firstChainNode,
                false));
        MissionGraph firstRule = new MissionGraph(entranceNode);
    }
}
