package csp;

import java.util.List;

// A constraint that says that exactly a certain number (count) of a certain set
// of variables are true.
public class Constraint {
    public int count;
    public List<Integer> vars;

    public Constraint(int count, List<Integer> vars) {
        this.count = count;  this.vars = vars;
    }
}
