package graphs;

//TODO - decide if we want setters for the two values in this class

public class MissionGraphEdge {

    //The ID of the node this edge is pointing to
    private MissionGraphNode pointingTo;

    //Boolean telling whether or not this connection is a tight coupling, which means that the
    //events that 2 nodes convey must be completed in a specific order quickly one after another.
    // Like
    //collecting a key after beating several enemies in a room
    //TODO - decide if we need this - Like maybe this is too much to ask of us since we're JUST
    //TODO - learning Solarus
    private boolean tightCoupling = false;

    public MissionGraphEdge(MissionGraphNode pointingTo, boolean tightCoupling) {
        this.pointingTo = pointingTo;
        this.tightCoupling = tightCoupling;
    }

    public MissionGraphNode getPointingTo() {
        return pointingTo;
    }

    public boolean isTightCoupling() {
        return tightCoupling;
    }
}
