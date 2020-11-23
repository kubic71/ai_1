
import minimax.*;

public class MctsTest {
    public static <S, A> boolean test(
        Game<S, A> game, Strategy<S, A> baseStrategy, int limit,
        int numGames, Strategy<S, A> opponent,
        boolean neverLose, double shouldWin1, double shouldWin2) {

        Mcts<S, A> mcts = new Mcts<>(game, baseStrategy, limit);

        int[][] wins = Runner.play2(game, mcts, opponent, numGames);
        mcts.reportStats();

        return Runner.report(numGames, wins, neverLose, shouldWin1, shouldWin2);
    }

    public static void main(String[] args) {
        test(new Trivial(), new RandomStrategy<>(new Trivial()), 1000,
             100, new RandomStrategy<>(new Trivial()),
             true, 0.6, 0.6);

         test(new TicTacToe(), new BasicTicTacToeStrategy(), 1000,
              500, new BasicTicTacToeStrategy(),
              true, 0.5, .06);
    }
}
