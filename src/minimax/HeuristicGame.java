package minimax;

// S = state type, A = action type
public interface HeuristicGame<S, A> extends AbstractGame<S, A> {
    double evaluate(S state);
}
