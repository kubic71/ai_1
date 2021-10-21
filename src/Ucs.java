import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import search.*;

// uniform-cost search

public class Ucs<S, A> {
  public static <S, A> Solution<S, A> search(Problem<S, A> prob) {
    /// Your implementation goes here.
    S init_state = prob.initialState();
    Node<S, A> init_node = new Node<S,A>(init_state, null, null, 0.0);

    HashSet<S> explored = new HashSet<>();

    HashMap<S, Double> dist = new HashMap<>();
    dist.put(init_state, 0.0);

    PriorityQueue<Node<S, A>> frontier = new PriorityQueue<>();
    frontier.add(init_node);

    Node<S, A> current;
    Node<S, A> goal = null;

    while (!frontier.isEmpty()) {
      current = frontier.poll();

      if (explored.contains(current.s)) {
        continue;
      }

      // In the Grid.java test, the duplicity ratio is somewhere around 1.28
      // suggesting that there aren't many duplicit nodes in the frontier at any given time
      // System.out.println(getDuplicityRatio(frontier));

      if (prob.isGoal(current.s)) {
          // we are closing the goal state (or at-least its first copy in the queue)
          goal = current;
          break;
      }


      explored.add(current.s);

      for (A action : prob.actions(current.s)) {
        S next_state = prob.result(current.s, action);
        Double next_cost = current.cost + prob.cost(current.s, action);

        if (!dist.containsKey(next_state) || next_cost < dist.get(next_state)) {
          dist.put(next_state, next_cost);

          // We don't decrease key here, because java's PriorityQueue doesn't support it
          // We simply insert a copy of the node
          Node<S, A> next_node = new Node<S, A>(next_state, current, action, next_cost);
          frontier.add(next_node);
        }
      }
    }

    return generateSolution(goal);

  }


  private static <S, A> Solution<S, A> generateSolution(Node<S, A> goal) {

    Node<S, A> current = goal;
    ArrayList<A> actions = new ArrayList<A>();

    while(true) {
      if (current.comeFrom == null) {
        break;
      }

      actions.add(current.comeFromAction);
      current = current.comeFrom;
    }

    Collections.reverse(actions);

    return new Solution<S,A>(actions, goal.s, goal.cost);
  }


  private static <S, A> double getDuplicityRatio(PriorityQueue<Node<S, A>> frontier) {
    if (frontier.size() == 0)
      return 1;
    HashSet<S> unique = new HashSet<S>();

    for (var node : frontier) {
      unique.add(node.s);
    }

    System.out.println("frontier: " + frontier.size() + ", unique: " + unique.size());

    return (double)frontier.size() / unique.size();
  }
}
