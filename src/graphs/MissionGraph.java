package graphs;

public class MissionGraph {

    //TODO - figure out if we want some kind of data structure containing the whole class here.
    //TODO - figure out how the replacement rules are going to work

    private MissionGraphNode firstNode;

    public MissionGraph(MissionGraphNode firstNode) {
        //The first node of the graph, from which each other node can be extrapolated
        this.firstNode = firstNode;
    }
}
