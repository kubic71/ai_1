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
        out.println("usage: connect_four <strategy1> [<strategy2>] [<option> ...]");
        out.println("options:");
        out.println("  -seed <num> : random seed");
        out.println("  -sim <count> : simulate a series of games without visualization");
        out.println("  -v : verbose output");
        out.println();
        out.println("available strategies:");
        out.println("  basic");
        out.println("  heuristic");
        out.println("  random");
        System.exit(0);
    }
    public static void main(String[] args) {
        ArrayList<Strategy<ConnectFour, Integer>> strategies = new ArrayList<>();
        int games = 0;
        int seed = -1;
        boolean verbose = false;

        for (int i = 0; i < args.length ; ++i) {
            if (args[i].startsWith("-"))
                switch (args[i]) {
                    case "-seed":
                        seed = Integer.parseInt(args[++i]);
                        break;
                    case "-sim":
                        games = Integer.parseInt(args[++i]);
                        break;
                    case "-v":
                        verbose = true;
                        break;
                    default:
                        usage();
                }
            else
                strategies.add(strategy(args[i]));
        }

        if (games > 0) {
            if (strategies.size() != 2) {
                out.println("must specify 2 strategies with -sim");
                return;
            }
            Runner.play(new ConnectFourGame(), strategies.get(0), strategies.get(1),
                        games, seed >= 0 ? seed : 0, verbose);
        } else {
            if (strategies.isEmpty())
                usage();

            UI ui = new UI();
            if (strategies.size() == 1)
                ui.addHuman();
            for (var s : strategies) {
                Runner.seed(s, seed);
                ui.addPlayer(s);
            }

            ui.setVisible(true);
        }
    }
}
