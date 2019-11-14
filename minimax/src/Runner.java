public class Runner {
	public static <S, A> void play(
			Game<S, A> game, Strategy<S, A> strat1, Strategy<S, A> strat2, int count) {

        String name1 = strat1.getClass().getSimpleName(), name2 = strat2.getClass().getSimpleName();
        System.out.printf("playing %d games: %s (player 1) vs. %s (player 2)\n",
                          count, name1, name2);

		int[] wins = new int[3];
		
		for (int i = 0 ; i < count ; ++i) {
			S s = game.initialState();
			while (!game.isDone(s)) {
				A a = game.player(s) == 1 ? strat1.action(s) : strat2.action(s);
				game.apply(s, a);
			}

			double o = game.outcome(s);
			if (o == 0.5)
				++wins[0];
			else if (o > 0.5)
				++wins[1];
			else ++wins[2];
		}
		
		System.out.format("%s won %d (%.1f%%), ",
			name1, wins[1], 100.0 * wins[1] / count);
		
		if (wins[0] > 0)
			System.out.format("%d draws (%.1f%%), ", wins[0], 100.0 * wins[0] / count);
		
		System.out.format("%s won %d (%.1f%%)\n",
			name2, wins[2], 100.0 * wins[2] / count);
	}
}
