package GraphGrammars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public enum roomContents {

    //TODO - decide if lock is required
    LEVEL_BOSS,
    MINI_BOSS,
    ENTRACE,
    GOAL,
    KEY,
    LOCK,
    MONSTERS,
    RUPEE,
    EXPLORATION,
    FINAL_KEY,
    FINAL_LOCK;

    @Override
    public String toString() {
        switch (this) {
            case LEVEL_BOSS:
                return "level_boss";
            case MINI_BOSS:
                return "mini_boss";
            case ENTRACE:
                return "entrance";
            case GOAL:
                return "goal";
            case KEY:
                return "key";
            case LOCK:
                return "lock";
            case MONSTERS:
                return "monsters";
            case FINAL_KEY:
                return "final_key";
            case FINAL_LOCK:
                return "final_lock";
            case RUPEE:
                return "rupee";
            case EXPLORATION:
                return "exploration";
        }
        return "none";
    }

    /**
     * Return the necessary line or lines for room generation in ASP
     *
     * @return
     */
    public String getGenerationString() {
        switch (this) {
            case KEY:
                return getGenericString(KEY);
            case RUPEE:
                return getGenericString(RUPEE);
            case FINAL_KEY:
                return getGenericString(FINAL_KEY);
            case GOAL:
                return getGenericString(GOAL);
            case ENTRACE:
                return getGenericString(ENTRACE);
            case MINI_BOSS:
                return getBossString(MINI_BOSS);
            case LEVEL_BOSS:
                return getBossString(LEVEL_BOSS);
            default:
                return "";
        }
    }

    /**
     * Return a generation string for generic item types (pickup-ables)
     *
     * @param type
     * @return
     */
    private String getGenericString(roomContents type) {
        if (type != KEY && type != RUPEE && type != FINAL_KEY && type != GOAL && type != ENTRACE) {
            throw new IllegalArgumentException("No generic string for:" + this);
        }
        return "in_room(" + this + ")\n";
    }

    /**
     * Return a generation string for the two boss types
     *
     * @param type - bossType
     * @return
     */
    private String getBossString(roomContents type) {
        switch (type) {
            case MINI_BOSS:
                return "boss_present(mini)\n";
            case LEVEL_BOSS:
                return "boss_present(level)\n";
            default:
                throw new IllegalArgumentException("input to getBossString must be a boss type");

        }
    }


}
