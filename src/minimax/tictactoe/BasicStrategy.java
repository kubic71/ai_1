package minimax.tictactoe;

import minimax.SeededStrategy;

public class BasicStrategy extends SeededStrategy<TicTacToe, Integer> {
    public Integer action(TicTacToe s) {
        // win if possible
        for (int i = 0 ; i < 9 ; ++i)
            if (s.board[i] == 0 && s.result(i).isDone())
                return i;

        // block a win if possible
        TicTacToe t = new TicTacToe(s.board, 3 - s.player);  // assume other player's turn
        for (int i = 0 ; i < 9 ; ++i)
            if (t.board[i] == 0 && t.result(i).isDone())
                return i;

        // move randomly
        return s.randomAction(random);
    }
}
