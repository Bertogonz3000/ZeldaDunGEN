package graphs;

/**
 * A node in the mission graph.  Described by a type and the nodes it's connected to.
 * Heriberto Gonzalez - November 2019
 */
public class MissionGraphNode {

    //Array containing up to four other nodes that a graph is connected to
    //0 is left, continuing clockwise around the node
    private MissionGraphNode[] connections = new MissionGraphNode[4];

    //The type of this node, one of the enumerated symbols of the alphabet
    private alphabet nodeType;

    /**
     * Creates a new instance of a MissionGraphNode with a nodeType
     *
     * @param nodeType - the type of the node, must be in the given alphabet
     */
    public MissionGraphNode(alphabet nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * @return - The type (from the alphabet) of this node
     */
    public alphabet getNodeType() {
        return nodeType;
    }

    /**
     * Change the indicated graph connection
     *
     * @param connectionSide - side of the node where the newNode will be placed.  0 is left, increasing clockwise
     * @param newNode        - the new node to add to the graph at the given location.
     */
    public void setConnection(nodePositions connectionSide, MissionGraphNode newNode) {
        connections[connectionSide.getNumVal()] = newNode;
    }

    /**
     * Returns the requested node
     *
     * @param connectionSide - the side of the graph to get the node from, left is 0, increasing clockwise.
     * @return
     */
    public MissionGraphNode getConnection(nodePositions connectionSide) {
        return connections[connectionSide.getNumVal()];
    }

    //TODO - mess with this toString method once we have a testable product
    @Override
    public String toString() {
        String string = this.nodeType.toString() + "\n";

        if (connections[nodePositions.LEFT.getNumVal()] != null) {
            string += "      " + getConnection(nodePositions.LEFT) + "\n";
        } else {
            string += "none,\n";
        }

        if (connections[nodePositions.RIGHT.getNumVal()] != null) {
            string += "      " + getConnection(nodePositions.RIGHT) + "\n";
        } else {
            string += "none,\n";
        }

        if (connections[nodePositions.RIGHT.getNumVal()] != null) {
            string += "      " + getConnection(nodePositions.RIGHT) + "\n";
        } else {
            string += "none,\n";
        }

        if (connections[nodePositions.RIGHT.getNumVal()] != null) {
            string += "      " + getConnection(nodePositions.RIGHT) + "\n";
        } else {
            string += "none,\n";
        }

        return string;
    }
}
