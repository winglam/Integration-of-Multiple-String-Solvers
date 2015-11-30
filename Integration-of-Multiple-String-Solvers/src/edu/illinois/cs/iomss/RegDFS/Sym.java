package edu.illinois.cs.iomss.RegDFS;

public class Sym extends MyRegex {
    String sym;

    public Sym(String sym) {
        this.sym = sym;
    }

    // The resulting nfa0 has form s0s -sym-> s0e

    public Nfa mkNfa(Nfa.NameSource names) {
        Integer s0s = names.next();
        Integer s0e = names.next();
        Nfa nfa0 = new Nfa(s0s, s0e);
        nfa0.addTrans(s0s, sym, s0e);
        return nfa0;
    }
}
