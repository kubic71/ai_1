// Monte Carlo tree search
class Mcts<S, A> implements Strategy<S, A> {

  public Mcts(Game<S, A> game, Strategy<S, A> base,
              ActionGenerator<S, A> actionGenerator, ResultGenerator<S, A> resultGenerator,
              int limit) {
  
    // Your implementation goes here.
    
  }

  // method in Strategy interface
  public A action(S state) {

    // Your implementation goes here.

    return null;
  }

  public void reportStats() {
      System.out.println("Report statistics here.");
  }

}
