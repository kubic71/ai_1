package minimax;

public class NamedStrategy<S, A> {
    String name;
    Strategy<S, A> strategy;

    public NamedStrategy(String name, Strategy<S, A> strategy) {
        this.name = name; this.strategy = strategy;
    }
}
