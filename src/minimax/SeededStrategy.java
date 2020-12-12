package minimax;

import java.util.Random;

public abstract class SeededStrategy<S, A> implements Strategy<S, A> {
    protected Random random = new Random();

    public void setSeed(int seed) {
        random = new Random(seed);
    }
}
