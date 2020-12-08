
import games.tictactoe.*;
import minimax.*;

public class MinimaxTest {
    public static <S, A> boolean test(
        HeuristicGame<S, A> game, int limit,
        int numGames, Strategy<S, A> opponent,
        boolean neverLose, double shouldWin1, double shouldWin2) {

        Minimax<S, A> minimax = new Minimax<>(game, limit);

        int[][] wins = Runner.play2(game, minimax, opponent, numGames);

        return Runner.report(numGames, wins, neverLose, shouldWin1, shouldWin2);
    }

    public static boolean testTrivial() {
        return test(new TrivialGame(), 0, 100, new RandomStrategy<>(new TrivialGame()),
                    true, 0.55, 0.55);
    }

    public static boolean testTicTacToe() {
        return test(new TicTacToe(), 0, 500, new BasicStrategy(),
                    true, 0.5, .06);
    }

    public static void main(String[] args) {
        testTrivial();
        testTicTacToe();
    }
}
