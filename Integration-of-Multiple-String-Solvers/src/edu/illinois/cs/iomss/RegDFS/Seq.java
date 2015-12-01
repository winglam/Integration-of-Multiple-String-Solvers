package edu.illinois.cs.iomss.RegDFS;

import java.util.List;
import java.util.Map;

public class Seq extends MyRegex {
    MyRegex r1, r2;

    public Seq(MyRegex r1, MyRegex r2) {
        this.r1 = r1;
        this.r2 = r2;
    }

    // If nfa1 has form s1s ----> s1e
    // and nfa2 has form s2s ----> s2e
    // then nfa0 has form s1s ----> s1e -eps-> s2s ----> s2e

    public Nfa mkNfa(Nfa.NameSource names) {
        Nfa nfa1 = r1.mkNfa(names);
        Nfa nfa2 = r2.mkNfa(names);
        Nfa nfa0 = new Nfa(nfa1.getStart(), nfa2.getExit());
        for (Map.Entry<Integer, List<Nfa.Transition>> entry : nfa1.getTrans().entrySet())
            nfa0.addTrans(entry);
        for (Map.Entry<Integer, List<Nfa.Transition>> entry : nfa2.getTrans().entrySet())
            nfa0.addTrans(entry);
        nfa0.addTrans(nfa1.getExit(), null, nfa2.getStart());
        return nfa0;
    }
}
