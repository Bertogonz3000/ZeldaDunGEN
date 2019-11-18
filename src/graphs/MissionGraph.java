package graphs;

import java.util.ArrayList;

public class MissionGraph {

    //TODO - figure out if we want some kind of data structure containing the whole class here.
    //TODO - figure out how the replacement rules are going to work

    //List of all of the nodes in this graph
    private ArrayList<MissionGraphNode> nodes = new ArrayList();
    //The next ID to assign to a node, this will be incremented with each node added, never
    // decreased
    private int nextID = 1;

    //TODO - comment
    public MissionGraph() {
        MissionGraphNode firstNode = new MissionGraphNode(alphabet.START);
        //Set the ID of the first node in the graph to 0
        firstNode.setId(0);
        nodes.add(firstNode);
    }

    public MissionGraph(MissionGraphNode firstNode){
        firstNode.setId(0);
        nodes.add(firstNode);
    }
}
