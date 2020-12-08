
import games.tictactoe.*;
import minimax.*;

public class MinimaxTest {
    public static <S, A> boolean test(
        HeuristicGame<S, A> game, int limit, int numGames, Strategy<S, A> opponent,
        int minWin1, int minWin2) {

        Minimax<S, A> minimax = new Minimax<>(game, limit);

        int[][] wins = Runner.play2(game, minimax, opponent, numGames);

        return Runner.report(wins, minWin1, minWin2);
    }

    public static boolean testTrivial() {
        return test(new TrivialGame(), 0, 100, new RandomStrategy<>(new TrivialGame()),
                    55, 55);
    }

    public static boolean testTicTacToe() {
        return test(new TicTacToe(), 0, 500, new BasicStrategy(),
                    250, 30);
    }

    public static void main(String[] args) {
        testTrivial();
        testTicTacToe();
    }
}
