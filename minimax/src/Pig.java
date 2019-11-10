import java.util.Random;

// The game of Pig, with 2 dice

class PigState {
    int player = 1;  // current player, 1 or 2

    int[] score = new int[3];  // score[0] is unused

    // points the current player has accumulated so far this turn
    int turnScore;

    static Random random = new Random();

    public PigState clone() {
        PigState s = new PigState();
        s.player = player; s.score = score.clone(); s.turnScore = turnScore;
        return s;
    }

    public void play(Boolean action) {
        if (action == Pig.Hold) {
            score[player] += turnScore;
            turnScore = 0;
            player = 3 - player;
        } else {  // Pig.Roll
            int roll1 = random.nextInt(6) + 1;
            int roll2 = random.nextInt(6) + 1;
            if (roll1 == 1 || roll2 == 1) {
                turnScore = 0;
                player = 3 - player;
                if (roll1 == 1 && roll2 == 1)
                    score[player] = 0;
            } else turnScore += roll1 + roll2;
        }
    }

    public int winner() {
        if (score[1] >= 100) return 1;
        if (score[2] >= 100) return 2;
        return 0;
    }
}

class Pig implements Game<PigState, Boolean> {
    static final Boolean Roll = true, Hold = false;

    public PigState initialState() { return new PigState(); }
    public PigState clone(PigState s) { return s.clone(); }
    public int player(PigState s) { return s.player; }
    public void apply(PigState s, Boolean a) { s.play(a); }
    public boolean isDone(PigState s) { return s.winner() > 0; }

    public double outcome(PigState s) {
        switch (s.winner()) {
            case 1: return 1.0;
            case 2: return 0.0;
            default: throw new RuntimeException("game is not done");
        }
    }
}
