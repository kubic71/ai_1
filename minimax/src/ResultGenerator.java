import java.util.List;

public interface ResultGenerator<S, A> {
  List<Possibility<S>> possibleResults(S state, A action); // some possible results of an action
}
