class ExpectiminimaxTest {
    static boolean testTicTacToe() {
        TicTacToe game = new TicTacToe();

        int Games = 400;

        Strategy<TTState, Integer> emm =
            new Expectiminimax<>(game, new TTActionGenerator(), new TTResultGenerator(),
                                 new TTEvaluator(), 0); 

        int[][] wins = Runner.play2(game, emm, new BasicTicTacToeStrategy(), Games);
        return Runner.report(Games, wins, true, 45, 3);
    }

    static boolean testPig() {
        Pig game = new Pig();

        int Games = 10000;

        Strategy<PigState, Boolean> emm =
            new Expectiminimax<>(game, new PigActionGenerator(), new PigResultGenerator(),
                                 new PigEvaluator(), 1); 

        int[][] wins = Runner.play2(game, emm, new BasicPigStrategy(15), Games);
        return Runner.report(Games, wins, false, 53, 50);
    }

    public static void test(String[] args) {
        testTicTacToe();
        testPig();
    }
}
