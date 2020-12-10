package minimax;

import static java.lang.System.out;

import java.lang.reflect.Constructor;
import java.util.*;

public class GameMain<S, A> {
    static void error(String message) {
        out.println(message);
        System.exit(0);
    }

    // A hack.  Use reflection to find the Minimax class since it is in the default package
    // and we are not.
    @SuppressWarnings("unchecked")
    Strategy<S, A> newMinimax(HeuristicGame<S, A> game, int depth) {
        try {
            Class<?> minimaxClass = Class.forName("Minimax");
            Constructor<?> constructor =
                minimaxClass.getConstructor(HeuristicGame.class, int.class);

            return (Strategy<S, A>) constructor.newInstance(game, depth);
        } catch (ClassNotFoundException e) {
            error("can't find Minimax class");
        } catch (NoSuchMethodException e) {
            error("can't find Minimax constructor");
        } catch (ReflectiveOperationException e) {
            throw new Error(e);
        }
        return null;
    }

    // A hack.  Use reflection to find the Mcts class since it is in the default package
    // and we are not.
    @SuppressWarnings("unchecked")
    Strategy<S, A> newMcts(AbstractGame<S, A> game, Strategy<S, A> base, int limit) {
        try {
            Class<?> mctsClass = Class.forName("Mcts");
            Constructor<?> constructor =
                mctsClass.getConstructor(AbstractGame.class, Strategy.class, int.class);

            return (Strategy<S, A>) constructor.newInstance(game, base, limit);
        } catch (ClassNotFoundException e) {
            error("can't find Mtcs class");
        } catch (NoSuchMethodException e) {
            error("can't find Mcts constructor");
        } catch (ReflectiveOperationException e) {
            throw new Error(e);
        }
        return null;
    }

    Strategy<S, A> strategy(
        String name, HeuristicGame<S, A> game, List<NamedStrategy<S, A>> extraStrategies) {

        int arg = -1;

        String base = null;
        int i = name.indexOf('/');
        if (i >= 0) {
            base = name.substring(i + 1);
            name = name.substring(0, i);
        }

        i = name.indexOf(':');
        if (i >= 0) {
            arg = Integer.parseInt(name.substring(i + 1));
            name = name.substring(0, i);
        }
        switch (name) {
            case "mcts":
                if (arg < 0)
                    error("must specify number of iterations for mcts");
                if (base == null)
                    error("must specify base strategy for mcts");
                return newMcts(game, strategy(base, game, extraStrategies), arg);
            case "minimax":
                if (arg < 0)
                    error("must specify search depth for minimax");
                return newMinimax(game, arg);
            case "random": return new RandomStrategy<>(game);

            default:
                for (NamedStrategy<S, A> s : extraStrategies)
                    if (s.name.equals(name))
                        return s.strategy;

                error("unknown strategy");
                return null;
        }
    }

    void usage(String program, List<NamedStrategy<S, A>> extraStrategies) {
        out.printf("usage: %s <strategy1> [<strategy2>] [<option> ...]\n", program);
        out.println("options:");
        out.println("  -seed <num> : random seed");
        out.println("  -sim <count> : simulate a series of games without visualization");
        out.println("  -v : verbose output");
        out.println();

        out.println("available strategies:");
        for (NamedStrategy<S, A> s : extraStrategies)
            out.printf("  %s\n", s.name);
        out.println("  random");
        out.println("  minimax:<depth>");
        out.println("  mcts:<depth>/<base-strategy>");
        System.exit(0);
    }
    
    public void main(
        String program, HeuristicGame<S, A> game, UI<S, A> ui,
        List<NamedStrategy<S, A>> extraStrategies, String[] args) {

        ArrayList<Strategy<S, A>> strategies = new ArrayList<>();
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
                        usage(program, extraStrategies);
                }
            else
                strategies.add(strategy(args[i], game, extraStrategies));
        }

        if (games > 0) {
            if (strategies.size() != 2)
                error("must specify 2 strategies with -sim");
            Runner.play(game, strategies.get(0), strategies.get(1),
                        games, seed >= 0 ? seed : 0, verbose);
        } else {
            if (strategies.isEmpty())
                usage(program, extraStrategies);

            if (ui == null) {
                out.println("no UI available");
                return;
            }
            ui.init(seed);
            if (strategies.size() == 1)
                ui.addHuman();
            for (var s : strategies) {
                Runner.seed(s, seed);
                ui.addPlayer(s);
            }

            ui.run();
        }
    }
}
