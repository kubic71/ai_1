import static java.lang.System.out;
import java.util.*;

import csp.*;

class Assignment {
    int var;
    boolean value;

    public Assignment(int var, boolean value) {
        this.var = var;  this.value = value;
    }
}

class SolverTest {
    Solver solver = new Solver();

    void fail(String s) {
        System.out.println(s);
        System.exit(0);
    }

    // Problems that are solvable by forward checking.
    String[] easy = { 
        "1 var: 1 of {0}",
        "1 var: 0 of {0}",
        "3 vars: 1 of {0}, 1 of {1}, 0 of {2}",
        "4 vars: 1 of {0 1}, 1 of {1 2}, 1 of {2 3}, 0 of {3}",
        "4 vars: 2 of {0 1 2 3}, 0 of {0 3}",
        "5 vars: 2 of {0 1 2 3 4}, 2 of {1 4}"
    };

    // These problems can't be solved by forward checking, but they are all satisfiable,
    // so a backtracking search should find a solution for all of them.  Also, some
    // inferences are possible in most of them.
    String[] harder = {
        "4 vars: 1 of {0 1}, 1 of {1 2}, 1 of {2 3}",

        "4 vars: 1 of {0 1}, 1 of {1 2}, 1 of {2 3}, 2 of {0 2 3} / 0=T, 1=F, 2=T, 3=F",

        // a situation from a 3 x 3 Minesweeper board
        "9 vars: 0 of {2 5 6}, 1 of {1 4 5}, 1 of {1 2 4 7 8}, 1 of {3 4 7} / 7=F, 8=F",

        // a situation from a 4 x 4 Minesweeper board
        "16 vars: 0 of {0 1 2 3 4 5 6 7}, 1 of {0 1 5 8 9}, 1 of {0 1 2 4 6 8 9 10}, " +
                 "1 of {1 2 3 5 7 9 10 11}, 1 of {2 3 6 10 11} / 8=T, 9=F, 10=F, 11=T",

        // a situation from a 4 x 4 Minesweeper board
        "16 vars: 0 of {4 5 8 9 12 13}, 1 of {0 1 5 8 9}, 2 of {0 1 2 4 6 8 9 10}, " +
                 "2 of {4 5 6 8 10 12 13 14}, 2 of {8 9 10 12 14} / 2=F, 6=F, 10=T, 14=T",

        // a situation from a 5 x 5 Minesweeper board
        "25 vars: 0 of {10 11 12 13 14 15 16 18 19 20 21 23 24}, " +
                 "1 of {5 6 11 15 16}, 2 of {5 6 7 10 12 15 16 17}, " +
                 "1 of {10 11 12 15 17 20 21 22}, 1 of {15 16 17 20 22}, " +
                 "3 of {6 7 8 11 13 16 17 18}, " +
                 "2 of {7 8 9 12 14 17 18 19}, 1 of {8 9 13 18 19}, " +
                 "1 of {12 13 14 17 19 22 23 24}, 1 of {17 18 19 22 24} " +
                 "/ 5=F, 6=T, 8=T, 9=F"
    };

    String[][] lists = { easy, harder };

    BooleanCSP parse(String s, List<Assignment> inferences) {
        String[] top = s.split("/");

        String[] parts = top[0].split(":");
        String v = parts[0];
        int i = v.indexOf(' ');
        int numVars = Integer.parseInt(v.substring(0, i));

        BooleanCSP csp = new BooleanCSP(numVars);

        String[] constraints = parts[1].split(",");
        for (String c : constraints) {
            c = c.trim();
            i = c.indexOf(' ');
            int count = Integer.parseInt(c.substring(0, i));

            int j = c.indexOf('{'), k = c.indexOf('}');
            String[] nums = c.substring(j + 1, k).split(" +");
            ArrayList<Integer> vars = new ArrayList<Integer>();
            for (String n : nums)
                vars.add(Integer.parseInt(n));
            csp.addConstraint(new Constraint(count, vars));
        }

        if (top.length > 1 && inferences != null) {
            String[] assignments = top[1].split(",");
            for (String a : assignments) {
                i = a.indexOf('=');
                int var = Integer.parseInt(a.substring(0, i).trim());
                String b = a.substring(i + 1).trim();
                inferences.add(new Assignment(var, b.equals("T")));
            }
        }

        return csp;
    }

    void print(BooleanCSP csp) {
        for (int v = 0 ; v < csp.numVars ; ++v) {
            if (v > 0)
                System.out.print(", ");
            Boolean b = csp.value[v];
            String s = b == null ? "X" : b ? "T" : "F";
            System.out.printf("%d = %s", v, s);
        }
        System.out.println();
    }

    void checkSolved(BooleanCSP csp) {
        for (int v = 0 ; v < csp.numVars ; ++v)
            if (csp.value[v] == null && !csp.constraints(v).isEmpty())
                fail("no value for var " + v);
        
        for (Constraint c : csp.constraints) {
            int count = 0;
            for (int v : c.vars)
                if (csp.value[v])
                    count += 1;
            
            if (count != c.count)
                fail("constraint not satisfied");
        }
    }

    void test_forward() {
        out.println("testing forward checking");

        for (String p : easy) {
            out.println(p);
            BooleanCSP csp = parse(p, null);
            List<Integer> found = solver.forwardCheck(csp);
            if (found == null)
                fail("failed to find a solution");
            print(csp);
            if (found.size() != csp.numVars)
                fail("failed to solve all variables");
            checkSolved(csp);
        }
    }

    void test_solve() {
        out.println("\ntesting solver");

        for (String[] list : lists)
            for (String p : list) {
                out.println(p);
                BooleanCSP csp = parse(p, null);
                List<Integer> found = solver.solve(csp);
                if (found == null)
                    fail("failed to find a solution");
                print(csp);
                checkSolved(csp);
            }
    }

    void test_infer() {
        out.println("\ntesting inference");

        for (String p : harder) {
            out.println(p);

            ArrayList<Assignment> expected = new ArrayList<Assignment>();
            BooleanCSP csp = parse(p, expected);
            while (true) {
                if (solver.forwardCheck(csp) == null)
                    fail("forward inference failed");
                if (solver.inferVar(csp) == -1)
                    break;
            }
            print(csp);

            for (int v = 0 ; v < csp.numVars ; ++v) {
                Boolean b = null;
                for (Constraint c : csp.constraints(v))
                    if (c.count == 0) {
                        b = false;
                        break;
                    }
                if (b == null)
                    for (Assignment a : expected)
                        if (a.var == v) {
                            b = a.value;
                            break;
                        }
                Boolean c = csp.value[v];
                if (b == null && c != null)
                    fail("should not have inferrred value for var " + v);
                else if (b != null && c == null)
                    fail("should have inferred value for var " + v);
                else if (b != c)
                    fail("inferred wrong value for var " + v);
            }
        }
    }

    void run() {
        test_forward();
        test_solve();
        test_infer();
        out.println("all tests passed");
    }

    public static void main(String[] args) {
        new SolverTest().run();
    }
}
