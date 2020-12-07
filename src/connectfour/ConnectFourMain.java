package connectfour;

import static java.lang.System.out;
import java.util.ArrayList;

import minimax.*;

public class ConnectFourMain {
    static Strategy<ConnectFour, Integer> strategy(String name) {
        switch (name) {
            case "basic": return new BasicStrategy();
            case "heuristic": return new HeuristicStrategy();
            case "random": return new RandomStrategy<>(new ConnectFourGame());

            default: throw new Error("unknown strategy");
        }
    }

    static void usage() {
        out.println("usage: connect_four <strategy1> [<strategy2>]");
        out.println("available strategies:");
        out.println("    basic");
        out.println("    random");
        System.exit(0);
    }
    public static void main(String[] args) {
        ArrayList<Strategy<ConnectFour, Integer>> strategies = new ArrayList<>();

        for (int i = 0; i < args.length ; ++i) {
            strategies.add(strategy(args[i]));
        }

        if (strategies.isEmpty())
            usage();

        UI ui = new UI();
        if (strategies.size() == 1)
            ui.addHuman();
        for (var s : strategies)
            ui.addPlayer(s);

        ui.setVisible(true);
    }
}
