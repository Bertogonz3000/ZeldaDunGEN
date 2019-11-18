package graphs;

import java.util.ArrayList;

public enum alphabet {
    //The alphabet of symbols to be used in graph grammar.
    BOSS_LEVEL(0, true),
    BOSS_MINI(1, true),
    ENTRANCE(2, true),
    GOAL(3, true),
    KEY(4, true),
    LOCK(5, true),
    EXPLORATION(6, false),
    CHAIN(7, false),
    FINAL_CHAIN(8, false),
    GATE(8, false),
    START(9, false),
    FINAL_KEY(10, true),
    FINAL_LOCK(11, true),
    MONSTER_ROOM(12, true);

    private ArrayList<MissionGraph> bossLevelRules, bossMiniRules, entranceRules,
    goalRules, keyRules, lockRules, explorationRules, chainRules, finalChainRules,
    gateRules, startRules, finalKeyRules, finalLockRules, monsterRoomRules;

    //numerical value of this enum
    private int numVal;
    //boolean telling whether or not a node marked by this symbol is terminal
    private boolean terminal;

    alphabet(int numVal, boolean terminal) {
        this.numVal = numVal;
        this.numVal = numVal;
    }

    public int getNumVal() {
        return this.numVal;
    }

    //return whether or not a node marked by this alphabet symbol is terminal
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
        }
        return "none";
    }
}
