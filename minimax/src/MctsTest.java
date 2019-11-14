class MctsTest {
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();

        int Games = 20;

        Strategy<TTState, Integer> mcts =
            new Mcts<>(game,
                       new TTBasicStrategy(),   // base strategy
                       new TTActionGenerator(),
                       1,                       // determinizations
                       100);                    // time limit (ms)

        Runner.play(game, mcts, new TTBasicStrategy(), Games);
        Runner.play(game, new TTBasicStrategy(), mcts, Games);
    }
}
