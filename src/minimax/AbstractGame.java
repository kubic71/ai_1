package minimax;

import java.util.List;

// S = state type, A = action type
public interface AbstractGame<S, A> {
  S initialState();
  S clone(S state);
  int player(S state);         // which player moves next: 1 (maximizing) or 2 (minimizing)
  List<A> actions(S state);    // available moves in this state
  void apply(S state, A action);  // apply action to state
  boolean isDone(S state);     // true if game has finished
  double outcome(S state);     // 1.0 = player 1 wins, 0.5 = draw, 0.0 = player 2 wins
}
