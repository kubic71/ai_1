package minimax;

import java.util.List;

class TrivialState {
    int p1move, p2move;

    TrivialState(int p1move, int p2move) {
        this.p1move = p1move;
        this.p2move = p2move;
    }
}

// A trivial game. There are two moves. First player 1 chooses a number from 1 to 3,
// then player 2 chooses a number from 1 to 3. Whoever chose the higher number wins.
public class Trivial implements AbstractGame<TrivialState, Integer> {

    @Override
    public TrivialState initialState() { return new TrivialState(0, 0); }

    @Override
    public TrivialState clone(TrivialState state) {
        return new TrivialState(state.p1move, state.p2move);
    }

    @Override
    public int player(TrivialState state) {
        return state.p1move == 0 ? 1 : 2;
    }

    @Override
    public List<Integer> actions(TrivialState state) {
        if (state.p1move == 0 || state.p2move == 0)
            return List.of(1, 2, 3);
        return List.of();
    }

    @Override
    public void apply(TrivialState state, Integer action) {
        if (action < 1 || action > 3)
            throw new Error("illegal move");
        if (state.p1move == 0)
            state.p1move = action;
        else if (state.p2move == 0)
            state.p2move = action;
        else throw new Error("game is over");
    }

    @Override
    public boolean isDone(TrivialState state) {
        return state.p1move != 0 && state.p2move != 0;
    }

    @Override
    public double outcome(TrivialState state) {
        if (state.p1move > state.p2move)
            return 1.0;
        if (state.p1move < state.p2move)
            return 0.0;
        return 0.5;
    }
    
}
