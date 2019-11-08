import java.util.List;

public interface ActionGenerator<S, A> {
  List<A> actions(S state);  // actions to try in this state
}
