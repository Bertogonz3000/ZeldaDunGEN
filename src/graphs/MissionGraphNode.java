package graphs;

/**
 * A node in the mission graph.  Described by a type and the nodes it's connected to.
 * Heriberto Gonzalez - November 2019
 */
public class MissionGraphNode {

    //Array containing up to four other nodes that a graph is connected to
    //0 is left, continuing clockwise around the node
    private MissionGraphNode[] connections = new MissionGraphNode[4];

    public MissionGraphNode() {

    }

    /**
     * Change the indicated graph connection
     *
     * @param connectionSide - side of the node where the newNode will be placed.  0 is left, increasing clockwise
     * @param newNode        - the new node to add to the graph at the given location.
     */
    public void setConnection(NODE_POSITIONS connectionSide, MissionGraphNode newNode) {
        connections[connectionSide.getNumVal()] = newNode;
    }

    /**
     * Returns the requested node
     *
     * @param connectionSide - the side of the graph to get the node from, left is 0, increasing clockwise.
     * @return
     */
    public MissionGraphNode getConnection(NODE_POSITIONS connectionSide) {
        return connections[connectionSide.getNumVal()];
    }
}
