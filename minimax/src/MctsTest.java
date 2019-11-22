class MctsTest {
    static void playTicTacToe() {
        TicTacToe game = new TicTacToe();

        int Games = 20;

        var mcts = new Mcts<>(game,
                              new BasicTicTacToeStrategy(),   // base strategy
                              new TTActionGenerator(),
                              1,                           // determinizations
                              100_000);                    // time limit (microseconds)

        Runner.play2(game, mcts, new BasicTicTacToeStrategy(), Games);
        mcts.reportStats();
    }

    static void playPig() {
        Pig game = new Pig();

        int Games = 500;

        var mcts = new Mcts<>(game,
                              new BasicPigStrategy(),     // base strategy
                              new PigActionGenerator(),
                              20,                          // determinizations
                              1000);                       // time limit (microseconds)

        Runner.play2(game, mcts, new BasicPigStrategy(), Games);
        mcts.reportStats();
    }

    public static void main(String[] args) {
        playTicTacToe();
        playPig();
    }
}
