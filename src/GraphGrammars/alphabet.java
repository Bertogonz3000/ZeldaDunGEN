package GraphGrammars;

import java.util.ArrayList;

public enum alphabet {

    //TODO - decide if numerical values are necessary
    //TODO - if we have two different constructors, is the boolean necessary?
    //The alphabet of symbols to be used in graph grammar.
    BOSS_LEVEL(0, true, new SpaceGraphRuleSet(0)),
    BOSS_MINI(1, true, new SpaceGraphRuleSet(1)),
    ENTRANCE(2, true),
    GOAL(3, true),
    KEY(4, true, new SpaceGraphRuleSet(4)),
    LOCK(5, true, new SpaceGraphRuleSet(5)),
    MONSTER_ROOM(13, true, new SpaceGraphRuleSet(13)),
    EXPLORATION(6, true, new SpaceGraphRuleSet(6)),
    CHAIN(7, false, new MissionReplacementRuleMaker(7)),
    FINAL_CHAIN(8, false, new MissionReplacementRuleMaker(8)),
    GATE(9, false),
    START(10, false, new MissionReplacementRuleMaker(10)),
    FINAL_KEY(11, true, new SpaceGraphRuleSet(11)),
    FINAL_LOCK(12, true, new SpaceGraphRuleSet(12));

    //numerical value of this enum
    private int numVal;
    //boolean telling whether or not a node marked by this symbol is terminal
    private boolean terminal;
    //replacement rules for one node
    private MissionReplacementRuleMaker missionRules;
    //replacement rules for space graph
    private SpaceGraphRuleSet spaceRules;

    /**
     * Constructor for symbols that are non-terminal in the space graph
     *
     * @param numVal   - numerical value of this symbol
     * @param terminal - is this node terminal in the mission graph?
     * @param rules    - rules for replacing this node with others in the mission graph.
     */
    alphabet(int numVal, boolean terminal, MissionReplacementRuleMaker rules) {
        this.numVal = numVal;
        this.terminal = terminal;
        this.missionRules = rules;
    }

    /**
     * Constructor for symbols that are terminal in the space graph
     *
     * @param numVal   - numerical value of this symbol - I should make sure these are necessary
     * @param terminal - is this node terminal in the mission graph?
     * @param rules    - rules for building out a space graph based on this symbol
     */
    alphabet(int numVal, boolean terminal, SpaceGraphRuleSet rules) {
        this.numVal = numVal;
        this.terminal = terminal;
        this.spaceRules = rules;
    }

    //TODO - this constructor is just so we don't have to fill out rules for everything while
    // testing get rid of this when done!
    alphabet(int numVal, boolean terminal) {
        this.numVal = numVal;
        this.terminal = terminal;
    }

    /**
     * Return the replacement rules for this node
     *
     * @return
     */
    public ArrayList<MissionGraph> getReplacementRules() {
        return this.missionRules.getReplacementRuleSet();
    }

    /**
     * Return the rules for building the space graph for this node
     *
     * @return
     */
    public ArrayList<SpaceGraph> getSpaceRules() {
        return this.spaceRules.getRuleSet();
    }

    /**
     * return the numberical value for this node
     *
     * @return
     */
    public int getNumVal() {
        return this.numVal;
    }


    /**
     * return whether or not a node marked by this alphabet symbol is terminal
     */
    public boolean getIsTerminal() {
        return this.terminal;
    }

    @Override
    public String toString() {
        switch (this.numVal) {
            case 0:
                return "boss_level";
            case 1:
                return "boss_mini";
            case 2:
                return "entrance";
            case 3:
                return "goal";
            case 4:
                return "key";
            case 5:
                return "lock";
            case 6:
                return "exploration";
            case 7:
                return "chain";
            case 8:
                return "final_chain";
            case 9:
                return "gate";
            case 10:
                return "start";
            case 11:
                return "final_key";
            case 12:
                return "final_lock";
            case 13:
                return "monster_room";
        }
        return "none";
    }
}
