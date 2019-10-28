import java.util.List;

class Solution<S, A> {
  public List<A> actions;  // series of actions from start state to goal state
  public S goalState;      // goal state that was reached
  public double pathCost;  // total cost from start state to goal

  public Solution(List<A> actions, S goalState, double pathCost) {
    this.actions = actions; this.goalState = goalState; this.pathCost = pathCost;
  }
}
