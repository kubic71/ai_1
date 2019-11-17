import java.util.*;

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
        if (score[1] >= Pig.Goal) return 1;
        if (score[2] >= Pig.Goal) return 2;
        return 0;
    }
}

class Pig implements Game<PigState, Boolean> {
    static final Boolean Roll = true, Hold = false;

    static final int Goal = 100;

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

class RandomPigStrategy implements Strategy<PigState, Boolean> {
    Random random = new Random();

    public Boolean action(PigState s) {
        if (s.turnScore == 0)
            return Pig.Roll;    // obviously

        if (s.score[s.player] + s.turnScore >= Pig.Goal)
            return Pig.Hold;    // obviously
        
        return random.nextBoolean();
    }
}

class BasicPigStrategy implements Strategy<PigState, Boolean> {
    int holdAt;

    public BasicPigStrategy(int holdAt) { this.holdAt = holdAt; }

    public BasicPigStrategy() { holdAt = 20; }

    public Boolean action(PigState s) {
        return s.score[s.player] + s.turnScore >= Pig.Goal ||
               s.turnScore >= holdAt ? Pig.Hold : Pig.Roll;
    }

    @Override
    public String toString() {
        return String.format("%s-%d", getClass().getSimpleName(), holdAt);
    }
}

class PigActionGenerator implements ActionGenerator<PigState, Boolean> {
    static List<Boolean> both = List.of(Pig.Roll, Pig.Hold);

    public List<Boolean> actions(PigState state) { return both; }
}

/*
class PigTest {
    public static void main(String[] args) {
        Runner.play2(new Pig(), new BasicPigStrategy(20), new BasicPigStrategy(5), 1000);
    }
}
*/
