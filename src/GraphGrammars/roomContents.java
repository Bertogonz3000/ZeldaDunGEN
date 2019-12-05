package GraphGrammars;

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
        }
        return "none";
    }
}
