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
            case MONSTERS:
                return getMonsterString(true);
            case KEY:
                return getGenericString(KEY) + "\n" + getMonsterString(false);
            case RUPEE:
                return getGenericString(RUPEE);
            case FINAL_KEY:
                return getGenericString(FINAL_KEY) + "\n" + getMonsterString(false);
            case GOAL:
                return getGenericString(GOAL);
            case ENTRACE:
                return getGenericString(ENTRACE);
            case MINI_BOSS:
                return getBossString(MINI_BOSS);
            case LEVEL_BOSS:
                return getBossString(LEVEL_BOSS);
            case EXPLORATION:
                return getMonsterString(false);
            default:
                //TODO - if we get nullpointers, check this out
                return "";
        }
    }


    /**
     * Return a generation string to put monsters in a room
     * If this is a monster room, more monsters will be added
     *
     * @param monsterRoom - whether or not this room is a monster room
     * @return
     */
    private String getMonsterString(boolean monsterRoom) {
        int numMonsters;
        Random rand = new Random();
        StringBuilder builder = new StringBuilder();
        HashMap<Integer, Integer> numMonstersByType = new HashMap<>();

        //Randomly select the amount of monsters based on the type of room this is.
        if (monsterRoom) {
            numMonsters = rand.nextInt(5) + 4;
        } else {
            numMonsters = rand.nextInt(4);
        }

        //For each monster, randomly select a type
        for (int i = 0; i < numMonsters; i++) {
            int type = rand.nextInt(4);
            if (numMonstersByType.containsKey(type)) {
                numMonstersByType.put(type, numMonstersByType.get(type) + 1);
            } else {
                numMonstersByType.put(type, 1);
            }
        }

        //For each monster type, append a string describing it and its # for generation
        for (Integer type : numMonstersByType.keySet()) {
            builder.append("monster_present(").append(type).append(",").append(numMonstersByType.get(type)).append(")\n");
        }

        return builder.toString();
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
        return "in_room(" + this + ")";
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
                return "boss_present(mini)";
            case LEVEL_BOSS:
                return "boss_present(level)";
            default:
                throw new IllegalArgumentException("input to getBossString must be a boss type");

        }
    }


}
