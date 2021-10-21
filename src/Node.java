
public class Node<S, A> implements Comparable<Node<S, A>> {


  public A comeFromAction;
  public S s;
  public Node<S, A> comeFrom;
  public Double cost;

  @Override
  public int compareTo(Node<S, A> other) {
    return cost.compareTo(other.cost);
  }

  public Node(S s, Node<S, A> comeFrom, A comeFromAction, Double cost) {
    this.s = s;
    this.comeFrom = comeFrom;
    this.comeFromAction = comeFromAction;
    this.cost = cost;
  }


}