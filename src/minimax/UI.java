package minimax;

public interface UI<S, A> {
    void init(int seed);
    void addPlayer(Strategy<S, A> strategy);
    void addHuman();
    void run();
}
