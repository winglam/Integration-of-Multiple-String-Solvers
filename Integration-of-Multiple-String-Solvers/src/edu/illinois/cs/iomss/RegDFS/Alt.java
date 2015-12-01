package edu.illinois.cs.iomss.RegDFS;

import java.util.List;
import java.util.Map;

public class Alt extends MyRegex {
    MyRegex r1, r2;

    public Alt(MyRegex r1, MyRegex r2) {
        this.r1 = r1;
        this.r2 = r2;
    }

    // If nfa1 has form s1s ----> s1e
    // and nfa2 has form s2s ----> s2e
    // then nfa0 has form s0s -eps-> s1s ----> s1e -eps-> s0e
    // s0s -eps-> s2s ----> s2e -eps-> s0e

    public Nfa mkNfa(Nfa.NameSource names) {
        Nfa nfa1 = r1.mkNfa(names);
        Nfa nfa2 = r2.mkNfa(names);
        Integer s0s = names.next();
        Integer s0e = names.next();
        Nfa nfa0 = new Nfa(s0s, s0e);
        for (Map.Entry<Integer, List<Nfa.Transition>> entry : nfa1.getTrans().entrySet())
            nfa0.addTrans(entry);
        for (Map.Entry<Integer, List<Nfa.Transition>> entry : nfa2.getTrans().entrySet())
            nfa0.addTrans(entry);
        nfa0.addTrans(s0s, null, nfa1.getStart());
        nfa0.addTrans(s0s, null, nfa2.getStart());
        nfa0.addTrans(nfa1.getExit(), null, s0e);
        nfa0.addTrans(nfa2.getExit(), null, s0e);
        return nfa0;
    }
}
