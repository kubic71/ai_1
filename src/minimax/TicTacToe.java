package minimax;

import java.util.*;

class TTEvaluator implements Evaluator<TTState> {
    public double evaluate(TTState state) {
        return 0.5;   // just a guess
    }
}

public class TicTacToe implements AbstractGame<TTState, Integer> {
    public TTState initialState() { return new TTState(); }

    public TTState clone(TTState state) { return state.clone(); }

    public int player(TTState state) { return state.player; }

    public List<Integer> actions(TTState state) { return state.actions(); }

    public void apply(TTState state, Integer action) { state.apply(action); }

    public boolean isDone(TTState state) { return state.isDone(); }

    public double outcome(TTState state) { return state.outcome(); }
}

/*
class TicTacTest {
    public static void main(String[] args) {
        Runner.play2(new TicTacToe(), new BasicTicTacToeStrategy(), new RandomTicTacToeStrategy(), 1000);
    }
}
*/
