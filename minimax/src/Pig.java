import java.util.*;

// The game of Pig, with 2 dice

class PigState {
    int player;  // current player, 1 or 2

    int[] score = new int[3];  // score[0] is unused

    // points the current player has accumulated so far this turn
    int turnScore;

    static Random random = new Random();

    public PigState() {
        player = 1;
    }

    public PigState(int player, int score1, int score2, int turnScore) {
        this.player = player;
        this.score[1] = score1; this.score[2] = score2;
        this.turnScore = turnScore;
    }

    public PigState clone() {
        return new PigState(player, score[1], score[2], turnScore);
    }

    public PigState newTurn() {
        turnScore = 0;
        player = 3 - player;
        return this;
    }

    public PigState hold() {
        score[player] += turnScore;
        newTurn();
        return this;
    }

    public PigState goBroke() {
        score[player] = 0;
        newTurn();
        return this;
    }

    public PigState add(int total) {
        turnScore += total;
        return this;
    }

    public void play(Boolean action) {
        if (action == Pig.Hold)
            hold();
        else {  // Pig.Roll
            int roll1 = random.nextInt(6) + 1;
            int roll2 = random.nextInt(6) + 1;
            if (roll1 == 1 && roll2 == 1)   // snake eyes
                goBroke();
            else if (roll1 == 1 || roll2 == 1)
                newTurn();
            else add(roll1 + roll2);
        }
    }

    public int winner() {
        if (score[1] >= Pig.Goal) return 1;
        if (score[2] >= Pig.Goal) return 2;
        return 0;
    }

    @Override
    public String toString() {
        return String.format("player = %d, score[1] = %d, score[2] = %d, turnScore = %d",
            player, score[1], score[2], turnScore);
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

class PigResultGenerator implements ResultGenerator<PigState, Boolean> {
    public List<Possibility<PigState>> possibleResults(PigState state, Boolean action) {
        var r = new ArrayList<Possibility<PigState>>();
        if (action == Pig.Hold) {
            r.add(new Possibility<>(1.0, state.clone().hold()));
        }
        else {
            // possible outcomes:
            // 1/36: go broke (lose entire score)
            // 10/36: bust (lose turn score)
            // 1/36: +4
            // 2/36: +5
            // 3/36: +6
            // 4/36: +7
            // 5/36: +8
            // 4/36: +9
            // 3/36: +10
            // 2/36: +11
            // 1/36: +12
            r.add(new Possibility<>(1 / 36.0, state.clone().goBroke()));
            r.add(new Possibility<>(10 / 36.0, state.clone().newTurn()));
            for (int i = 4 ; i <= 8 ; ++i)
                r.add(new Possibility<>((i - 3) / 36.0, state.clone().add(i)));
            for (int i = 9 ; i <= 12 ; ++i)
            r.add(new Possibility<>((13 - i) / 36.0, state.clone().add(i)));
        }
        return r;
    }
}

class PigEvaluator implements Evaluator<PigState> {
    static double logistic(double d) {
        return 1.0 / (1.0 + Math.exp(- d));
    }

    public double evaluate(PigState s) {
        double p = s.score[s.player] + s.turnScore >= 100 ? 1.0
                 : logistic(s.score[s.player] / 28.0 - s.score[3 - s.player] / 30.0 + s.turnScore / 32.0);
        return s.player == 1 ? p : 1 - p;
    }
}

/*
class PigTest {
    public static void main(String[] args) {
        Runner.play2(new Pig(), new BasicPigStrategy(20), new BasicPigStrategy(5), 1000);
    }
}*/
