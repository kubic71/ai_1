import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.stat.interval.*;

public class Runner {
    static int debugLevel = 0;

    static <S, A> String name(Strategy<S, A> strat) {
        String s = strat.toString();
        return s.contains("@") ? strat.getClass().getSimpleName() : s;
    }

    public static <S, A> int[] play(Game<S, A> game, Strategy<S, A> strat1, Strategy<S, A> strat2,
                                    int count) {
        System.out.printf("\nplaying %d games: %s (player 1) vs. %s (player 2)\n",
                          count, name(strat1), name(strat2));

		int[] wins = new int[3];
		
		for (int i = 0 ; i < count ; ++i) {
			S s = game.initialState();
			while (!game.isDone(s)) {
				A a = game.player(s) == 1 ? strat1.action(s) : strat2.action(s);
                game.apply(s, a);
                if (debugLevel >= 2)
                    System.out.println(s);
			}

			double o = game.outcome(s);
			if (o == 0.5) {
                ++wins[0];
                if (debugLevel >= 1)
                    System.out.println("draw");
            }
			else if (o > 0.5) {
                ++wins[1];
                if (debugLevel >= 1)
                    System.out.printf("%s wins\n", name(strat1));
            }
            else {
                ++wins[2];
                if (debugLevel >= 1)
                    System.out.printf("%s wins\n", name(strat2));
            }                
		}
		
		System.out.format("    %s won %d (%.1f%%), ", name(strat1), wins[1], 100.0 * wins[1] / count);
        
        int draws = wins[0];
		if (draws > 0)
			System.out.format("%d draws (%.1f%%), ", draws, 100.0 * draws / count);
		
		System.out.format("%s won %d (%.1f%%)\n", name(strat2), wins[2], 100.0 * wins[2] / count);

        return wins;
    }

    public static <S, A> int[][] play2(
			Game<S, A> game, Strategy<S, A> strat1, Strategy<S, A> strat2, int count) {
        int[][] wins = new int[3][];
        wins[1] = play(game, strat1, strat2, count);
        wins[2] = play(game, strat2, strat1, count);
        int totalDraws = wins[1][0] + wins[2][0];

        int confidence = 99;
        System.out.printf("\nwith %d%% confidence, %s\n", confidence, name(strat1));

        for (int asPlayer = 1 ; asPlayer <= 3 ; ++asPlayer) {
            int games = asPlayer < 3 ? count : 2 * count;
            int win = asPlayer < 3 ? wins[asPlayer][asPlayer] : wins[1][1] + wins[2][2];

            ConfidenceInterval ci =
                IntervalUtils.getWilsonScoreInterval(games, win, confidence / 100.0);
            double lo = ci.getLowerBound() * 100, hi = ci.getUpperBound() * 100;

            int draw = asPlayer < 3 ? wins[asPlayer][0] : totalDraws;

            ConfidenceInterval ci2 =
            IntervalUtils.getWilsonScoreInterval(games, draw, confidence / 100.0);
            double lo2 = ci2.getLowerBound() * 100, hi2 = ci2.getUpperBound() * 100;

            System.out.printf("    %s: ",
                              asPlayer < 3 ? "as player " + asPlayer : "overall    ");
            System.out.printf(" wins %4.1f%% - %4.1f%%", lo, hi);
            if (totalDraws > 0)
                System.out.printf(", draws %4.1f%% - %4.1f%%", lo2, hi2);
            System.out.println();
        }

        return wins;
    }

    public static boolean report(int games, int[][] wins, boolean neverLose,
                                 double shouldWin1, double shouldWin2) {
        if (neverLose && (wins[1][2] > 0 || wins[2][1] > 0)) {
            System.out.println("FAILURE: should never lose a game");
            return false;
        }
        double alpha = 0.01;
        int minWin1 = new BinomialDistribution(games, shouldWin1)
                          .inverseCumulativeProbability(alpha);
        System.out.printf("minWin1 = %d\n", minWin1);
        if (wins[1][1] < minWin1) {
            System.out.printf("FAILURE: should win at least %d games as player 1\n", minWin1);
            return false;
        }
        int minWin2 = new BinomialDistribution(games, shouldWin2)
                          .inverseCumulativeProbability(alpha);
        System.out.printf("minWin2 = %d\n", minWin2);
        if (wins[2][2] < minWin2) {
            System.out.printf("FAILURE: should win at least %d games as player 2\n", minWin2);
            return false;
        }
        System.out.println("PASSED");
        return true;
    }
}
