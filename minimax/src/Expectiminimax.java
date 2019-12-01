class Expectiminimax<S, A> implements Strategy<S, A> {

  public Expectiminimax(Game<S, A> game,
                        ActionGenerator<S, A> actionGenerator,
                        ResultGenerator<S, A> resultGenerator,
                        Evaluator<S> evaluator, int limit) {

    // Your implementation goes here.
    
  }

  // method in Strategy interface
  public A action(S state) {

    // Your implementation goes here.

    return null;
  }

}
