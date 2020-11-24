package minimax;

import java.util.*;

public class RandomStrategy<S, A> implements Strategy<S, A> {
    AbstractGame<S, A> game;
    Random random = new Random(0);

    public RandomStrategy(AbstractGame<S, A> game) {
        this.game = game;
    }

    @Override
    public A action(S state) {
        List<A> actions = game.actions(state);
        return actions.get(random.nextInt(actions.size()));
    }
}
