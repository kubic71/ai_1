import java.util.*;

// An empty problem.  The start state is the goal.
// There are no actions.

class Empty implements Problem<Integer, Integer> {
  public Integer initialState() { return 0; }

  public List<Integer> actions(Integer state) { return new ArrayList<Integer>(); }

  public Integer result(Integer state, Integer action) {
    throw new AssertionError("should not be called");
  }

  public boolean isGoal(Integer state) { return state == 0; }

  public double cost(Integer state, Integer action) {
    throw new AssertionError("should not be called");
  }

  public static void test() {
    Empty e = new Empty();
    Solution<Integer, Integer> s = Ucs.search(e);
    if (s != null) {
      System.out.format("total cost is %.1f\n", s.pathCost);
      System.out.format("number of actions = %d\n", s.actions.size());
    }
    else
      System.out.println("no solution");
  }
}
