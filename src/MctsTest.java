
import games.tictactoe.*;
import minimax.*;

public class MctsTest {
    public static <S, A> boolean test(
        AbstractGame<S, A> game, Strategy<S, A> baseStrategy, int limit,
        int numGames, Strategy<S, A> opponent,
        boolean neverLose, double shouldWin1, double shouldWin2) {

        Mcts<S, A> mcts = new Mcts<>(game, baseStrategy, limit);

        int[][] wins = Runner.play2(game, mcts, opponent, numGames);

        return Runner.report(numGames, wins, neverLose, shouldWin1, shouldWin2);
    }

    public static boolean testTrivial() {
        return test(new TrivialGame(), new RandomStrategy<>(new TrivialGame()), 1000,
                    100, new RandomStrategy<>(new TrivialGame()),
                    true, 0.55, 0.55);
    }

    public static boolean testTicTacToe() {
        return test(new TicTacToe(), new BasicStrategy(), 1000,
                    500, new BasicStrategy(),
                    true, 0.5, .06);
    }

    public static void main(String[] args) {
        testTrivial();
        testTicTacToe();
    }
}
