package common.game;

public enum Score {
    MAX(150, 0),
    EXCELLENT(100, 5),
    GOOD(70, 8),
    REGULAR(50, 12),
    BAD(0, 15);

    private final int scoreAmount;
    private final int minimumTries;
    Score(int scoreAmount, int minimumTries) {
        this.scoreAmount = scoreAmount;
        this.minimumTries = minimumTries;
    }

    public int getScoreAmount() {
        return scoreAmount;
    }

    public int getMinimumTries() {
        return minimumTries;
    }

    public static Score fromTries(int tries) {
        for (Score score : Score.values()) {
            if (tries <= score.minimumTries) {
                return score;
            }
        }
        return BAD;
    }
}
