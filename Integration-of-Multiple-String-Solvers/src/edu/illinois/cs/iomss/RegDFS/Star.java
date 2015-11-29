package edu.illinois.cs.iomss.RegDFS;

import java.util.List;
import java.util.Map;

public class Star extends MyRegex {
	MyRegex r;

	public Star(MyRegex r) {
		this.r = r;
	}

	// If nfa1 has form s1s ----> s1e
	// then nfa0 has form s0s ----> s0s
	// s0s -eps-> s1s
	// s1e -eps-> s0s

	public Nfa mkNfa(Nfa.NameSource names) {
		Nfa nfa1 = r.mkNfa(names);
		Integer s0s = names.next();
		Nfa nfa0 = new Nfa(s0s, s0s);
		for (Map.Entry<Integer, List<Nfa.Transition>> entry : nfa1.getTrans().entrySet())
			nfa0.addTrans(entry);
		nfa0.addTrans(s0s, null, nfa1.getStart());
		nfa0.addTrans(nfa1.getExit(), null, s0s);
		return nfa0;
	}
}