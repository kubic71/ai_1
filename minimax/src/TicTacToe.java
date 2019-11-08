import java.util.*;

class TTState {
    int[] board = new int[9];
    public int player = 1;

    Random rand = new Random();

    public TTState() { }
    public TTState(int[] board, int player) {
        this.board = board; this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TTState)) return false;

        TTState t = (TTState) o;
        return player == t.player && Arrays.equals(board, t.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board) + player;
    }

    @Override
    public TTState clone() {
        return new TTState(board.clone(), player);
    }

    List<Integer> actions() {
        List<Integer> r = new ArrayList<Integer>();
        for (int i = 0 ; i < 9 ; ++i)
            if (board[i] == 0)
                r.add(i);
        return r;
    }

    int countEmpty() {
        int count = 0;
        for (int i = 0 ; i < 9 ; ++i)
            if (board[i] == 0)
                ++count;

        return count;
    }

    int randomAction() {
        int m = rand.nextInt(countEmpty());
        int i = -1;
        for (int j = 0 ; j <= m ; ++j)
            do {
                i += 1;
            } while (board[i] != 0);

        return i;
    }

    void apply(int action) {
        if (board[action] != 0)
            throw new Error("illegal move");

        board[action] = player;
        player = 3 - player;
    }

    TTState result(int action) {
        TTState s = clone();
        s.apply(action);
        return s;
    }

    int winner(int s, int d) {
        int w = board[s];

        return board[s + d] == w && board[s + 2 * d] == w ? w : 0;
    }

    static int[] check =   { 0, 1, 2, 0, 3, 6, 0, 2 };
    static int[] check_d = { 3, 3, 3, 1, 1, 1, 4, 2 };

    public int winner() {
        for (int i = 0 ; i < check.length ; ++i) {
            int w = winner(check[i], check_d[i]);
            if (w > 0)
                return w;
        }
        return 0;
    }

    public boolean isDone() {
        return (winner() != 0 || countEmpty() == 0);
    }

    public double outcome() {
        switch (winner()) {
        case 0: return 0.5;   // draw
        case 1: return 1.0;
        case 2: return 0.0;
        default: throw new Error();
        }
    }

    char asChar(int i) {
        switch (i) {
        case 0: return '.';
        case 1: return 'X';
        case 2: return 'O';
        default: throw new Error();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0 ; i < 3 ; ++i) {
            for (int j = 0 ; j < 3 ; ++j)
                sb.append(String.format("%c ", asChar(board[3 * i + j])));
            sb.append("\n");
        }

        return sb.toString();
    }
}

class TTRandomStrategy implements Strategy<TTState, Integer> {
    Random rand = new Random();

    public Integer action(TTState s) {
        return s.randomAction();
    }
}

class TTBasicStrategy implements Strategy<TTState, Integer> {
    public Integer action(TTState s) {
        // win if possible
        for (int i = 0 ; i < 9 ; ++i)
            if (s.board[i] == 0 && s.result(i).isDone())
                return i;

        // block a win if possible
        TTState t = new TTState(s.board, 3 - s.player);  // assume other player's turn
        for (int i = 0 ; i < 9 ; ++i)
            if (t.board[i] == 0 && t.result(i).isDone())
                return i;

        // move randomly
        return s.randomAction();
    }
}

class TTActionGenerator implements ActionGenerator<TTState, Integer> {
    public List<Integer> actions(TTState s) { return s.actions(); }
}

class TTResultGenerator implements ResultGenerator<TTState, Integer> {
    public List<Possibility<TTState>> possibleResults(TTState state, Integer action) {
        return List.of(new Possibility<TTState>(1.0, state.result(action)));
    }
}

class TTEvaluator implements Evaluator<TTState> {
    public double evaluate(TTState state) {
        return 0.5;   // just a guess
    }
}

public class TicTacToe implements Game<TTState, Integer> {
    public TTState initialState() { return new TTState(); }

    public TTState clone(TTState state) { return state.clone(); }

    public int player(TTState state) { return state.player; }

    public void apply(TTState state, Integer action) { state.apply(action); }

    public boolean isDone(TTState state) { return state.isDone(); }

    public double outcome(TTState state) { return state.outcome(); }

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();

        Strategy<TTState, Integer> emm =
            new Expectiminimax<>(game, new TTActionGenerator(), new TTResultGenerator(),
                                 new TTEvaluator(), 8); 

        System.out.println("running");
        Runner.play(game, emm, new TTBasicStrategy(), 20);
    }

}
