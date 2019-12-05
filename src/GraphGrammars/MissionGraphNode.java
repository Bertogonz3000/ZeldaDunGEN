package GraphGrammars;

//TODO - implement a system of unique IDs for replacement rules - maybe some
// ordering of the nodoes?

/**
 * A node in the mission graph.  Described by a type and the nodes it's
 * connected to.
 * Heriberto Gonzalez - November 2019
 */
public class MissionGraphNode {

    //Array containing up to four other nodes that a graph is connected to
    //0 is left, continuing clockwise around the node
    private MissionGraphEdge[] connections = new MissionGraphEdge[4];

    //The type of this node, one of the enumerated symbols of the alphabet
    private alphabet nodeType;

    //The unique ID of this node
    private int id;

    //Whether or not this node can be removed at the end of a loop
    private boolean removable = false;

    /**
     * Return the UNIQUE ID of this node, this should be set in the MissionGraph class
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Set the UNIQUE ID of this node, should be set in the MissionGraph Class
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

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
     * @param connectionSide - side of the node where the newNode will be
     *                       placed.  0 is left, increasing clockwise
     * @param newEdge        - the new edge to add to the graph at the given
     *                       location.
     */
    public void setConnection(nodePositions connectionSide,
                              MissionGraphEdge newEdge) {
        connections[connectionSide.getNumVal()] = newEdge;
    }

    public boolean isTerminal() {
        return nodeType.getIsTerminal();
    }


    public boolean isRemovable() {
        return removable;
    }

    public void markForRemoval() {
        this.removable = true;
    }

    /**
     * Returns the requested node
     *
     * @param connectionSide - the side of the graph to get the node from,
     *                       left is 0, increasing clockwise.
     * @return
     */
    public MissionGraphEdge getConnection(nodePositions connectionSide) {
        return connections[connectionSide.getNumVal()];
    }

    /**
     * Return the array of connected edges
     *
     * @return
     */
    public MissionGraphEdge[] getConnections() {
        return connections;
    }

    //TODO - mess with this toString method once we have a testable product
    @Override
    public String toString() {

        StringBuilder aggString = new StringBuilder();

        aggString.append("(").append(this.nodeType).append(", ").append(this.id)
                .append(")").append(" :: (");

        for (int i = 0; i < connections.length; i++) {
            if (connections[i] != null) {
                MissionGraphNode thisNode;
                if (connections[i].getPointingTo() == this) {
                    thisNode = connections[i].getPointingFrom();
                } else {
                    thisNode = connections[i].getPointingTo();
                }
                aggString.append(thisNode.nodeType);
                aggString.append(":");
                aggString.append(thisNode.id);
                if (i < 3) {
                    aggString.append(", ");
                }
            }
        }

        aggString.append(")");

        return aggString.toString();
    }

    /**
     * Returns this mission graph as a graph viz compatible string
     *
     * @return
     */
    public String getGVString() {
        StringBuilder gvString = new StringBuilder();

        for (int i = 0; i < connections.length; i++) {
            if (connections[i] != null && !(connections[i].getPointingTo() == this)) {
                MissionGraphNode nextNode = connections[i].getPointingTo();
                gvString.append(nodeType.toString()).append(id).append(" -> ").
                        append(nextNode.nodeType).append(nextNode.id).append("\n");
            }
        }

        return gvString.toString();
    }
}
