package games.tictactoe;

import java.util.*;

import minimax.HeuristicGame;

public class TicTacToe implements HeuristicGame<TTState, Integer> {
    public TTState initialState(int seed) { return new TTState(); }

    public TTState clone(TTState state) { return state.clone(); }

    public int player(TTState state) { return state.player; }

    public List<Integer> actions(TTState state) { return state.actions(); }

    public void apply(TTState state, Integer action) { state.apply(action); }

    public boolean isDone(TTState state) { return state.isDone(); }

    public double outcome(TTState state) {
        switch (state.winner()) {
            case 0: return DRAW;   // draw
            case 1: return PLAYER_1_WIN;
            case 2: return PLAYER_2_WIN;
            default: throw new Error();
        }
    }

    public double evaluate(TTState state) {
        return DRAW;   // just a guess
    }
}

/*
class TicTacTest {
    public static void main(String[] args) {
        Runner.play2(new TicTacToe(), new BasicTicTacToeStrategy(), new RandomTicTacToeStrategy(), 1000);
    }
}
*/
