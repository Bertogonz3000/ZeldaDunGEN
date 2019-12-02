package GraphGrammars;


import java.util.ArrayList;
import java.util.Random;

/**
 * Mission graph for a dungeon, built to move from left to right.  Will be used as the basis for
 * a space graph.
 */
public class MissionGraph {

    //TODO - figure out if we want some kind of data structure containing the whole class here.
    //TODO - figure out how the replacement rules are going to work

    //List of all of the nodes in this graph
    private ArrayList<MissionGraphNode> nodes = new ArrayList<>();

    /**
     * Create a mission graph with no nodes
     */
    public MissionGraph() {
        MissionGraphNode firstNode = new MissionGraphNode(alphabet.START);
        //Set the ID of the first node in the graph to 0
        firstNode.setId(0);
        nodes.add(firstNode);
    }

    /**
     * Constructor that takes only the first node in the graph
     *
     * @param firstNode
     */
    public MissionGraph(MissionGraphNode firstNode) {
        firstNode.setId(0);
        nodes.add(firstNode);
    }

    /**
     * Constructor that takes all the nodes associated with this graph, sets their IDs
     *
     * @param nodes - nodes in the graph
     */
    public MissionGraph(ArrayList<MissionGraphNode> nodes) {
        for (MissionGraphNode node : nodes) {
            node.setId(nodes.indexOf(node));
        }
        this.nodes = nodes;
    }

    public void addNode(MissionGraphNode node) {
        nodes.add(node);
        node.setId(nodes.indexOf(node));
    }

    /**
     * Return the nodes in this graph
     *
     * @return
     */
    public ArrayList<MissionGraphNode> getNodes() {
        return nodes;
    }

    /**
     * Decide if this graph has any nonterminal nodes.
     *
     * @return
     */
    public boolean hasNonTerminalNodes() {
        for (MissionGraphNode node : nodes) {
            if (!node.isTerminal()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Replace any non-terminal nodes in this graph, following the rules laid out in
     * ReplacementRuleMaker.class
     */
    public void runReplacements() {
        assert !nodes.isEmpty() : "Graph must have nodes to run replacement";

        while (this.hasNonTerminalNodes()) {
            for (int i = 0; i < nodes.size(); i++) {
                MissionGraphNode currentNode = nodes.get(i);
                //If the node is non-terminal...
                if (!currentNode.getNodeType().getIsTerminal() && !currentNode.isRemovable()) {
                    //Get the possible rules from its type and choose one
                    ArrayList<MissionGraph> possibleRules =
                            currentNode.getNodeType().getReplacementRules();
                    Random random = new Random();
                    int nextInt = random.nextInt(possibleRules.size());
                    ArrayList<MissionGraphNode> newNodes =
                            possibleRules.get(nextInt).getNodes();

                    //For each node in the chosen rule, add it to this graph and set its ID
                    for (MissionGraphNode newNode : newNodes) {
                        nodes.add(newNode);
                        newNode.setId(newNodes.indexOf(newNode));
                    }

                    //If the current node has a left connection, make it the connection of the
                    // first node in the rule
                    if (currentNode.getConnection(nodePositions.LEFT) != null) {
                        MissionGraphEdge newLeftEdge =
                                currentNode.getConnection(nodePositions.LEFT).setPointingTo(newNodes.get(0));
                        newNodes.get(0).setConnection(nodePositions.LEFT,
                                newLeftEdge);
                        newLeftEdge.getPointingFrom().setConnection(nodePositions.RIGHT,
                                newLeftEdge);

                    }

                    //If the current node has a right connection, make it the connection of the
                    // last node in the rule
                    if (currentNode.getConnection(nodePositions.RIGHT) != null) {
                        MissionGraphEdge newRightEdge =
                                currentNode.getConnection(nodePositions.RIGHT).setPointingFrom(newNodes.get(newNodes.size() - 1));
                        newNodes.get(newNodes.size() - 1).setConnection(nodePositions.RIGHT,
                                newRightEdge);
                        newRightEdge.getPointingTo().setConnection(nodePositions.LEFT,
                                newRightEdge);
                    }

                    //Remove the replaced node
                    currentNode.markForRemoval();
                }
            }
            //Remove any node that has been marked - done because removing within the loop causes
            // problems
            for (int i = 0; i < nodes.size(); i++) {
                MissionGraphNode curNode = nodes.get(i);
                if (curNode.isRemovable()) {
                    nodes.remove(curNode);
                }
            }
        }
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setId(i);
        }
    }

    @Override
    public String toString() {
        StringBuilder aggString = new StringBuilder();
        for (MissionGraphNode node : nodes) {
            aggString.append(node.toString()).append("\n");
        }
        return aggString.toString();
    }
}
