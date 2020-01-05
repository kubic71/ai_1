package ai_1.csp;

import java.util.List;

public class Constraint {
    int count;
    List<Integer> vars;

    public Constraint(int count, List<Integer> vars) {
        this.count = count;  this.vars = vars;
    }
}
