class ExpectiminimaxTest {
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();

        int Games = 100;

        Strategy<TTState, Integer> emm =
            new Expectiminimax<>(game, new TTActionGenerator(), new TTResultGenerator(),
                                 new TTEvaluator(), 0); 

        Runner.play2(game, emm, new BasicTicTacToeStrategy(), Games);
    }
}
