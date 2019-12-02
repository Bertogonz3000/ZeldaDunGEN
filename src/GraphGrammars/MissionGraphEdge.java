package GraphGrammars;

//TODO - decide if we want setters for the two values in this class

public class MissionGraphEdge {

    //The ID of the node this edge is pointing to
    private MissionGraphNode pointingTo, pointingFrom;

    //Boolean telling whether or not this connection is a tight coupling, which means that the
    //events that 2 nodes convey must be completed in a specific order quickly one after another.
    // Like
    //collecting a key after beating several enemies in a room
    //TODO - decide if we need this - Like maybe this is too much to ask of us since we're JUST
    //TODO - learning Solarus
    private boolean tightCoupling = false;

    public MissionGraphEdge(MissionGraphNode pointingFrom, MissionGraphNode pointingTo,
                            boolean tightCoupling) {
        this.pointingTo = pointingTo;
        this.tightCoupling = tightCoupling;
        this.pointingFrom = pointingFrom;
    }

    public MissionGraphNode getPointingTo() {
        return pointingTo;
    }

    public MissionGraphNode getPointingFrom() {
        return pointingFrom;
    }

    public MissionGraphEdge setPointingTo(MissionGraphNode pointingTo) {
        this.pointingTo = pointingTo;
        return this;
    }

    public MissionGraphEdge setPointingFrom(MissionGraphNode pointingFrom) {
        this.pointingFrom = pointingFrom;
        return this;
    }

    public boolean isTightCoupling() {
        return tightCoupling;
    }
}
