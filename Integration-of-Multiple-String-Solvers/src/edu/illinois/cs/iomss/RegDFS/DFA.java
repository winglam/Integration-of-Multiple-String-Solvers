package edu.illinois.cs.iomss.RegDFS;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

//Class Dfa, deterministic finite automata --------------------------

/*
* A deterministic finite automaton (DFA) is represented as a Map from state
* number (Integer) to a Map from label (a String, non-null) to a target state
* (an Integer).
*/

class Dfa {
	private Integer startState;
	private Set<Integer> acceptStates;
	private Map<Integer, Map<String, Integer>> trans;

	public Dfa(Integer startState, Set<Integer> acceptStates, Map<Integer, Map<String, Integer>> trans) {
		this.startState = startState;
		this.acceptStates = acceptStates;
		this.trans = trans;
	}

	public Integer getStart() {
		return startState;
	}

	public Set<Integer> getAccept() {
		return acceptStates;
	}

	public Map<Integer, Map<String, Integer>> getTrans() {
		return trans;
	}

	public String toString() {
		return "DFA start=" + startState + "\naccept=" + acceptStates + "\n" + trans;
	}

	// Write an input file for the dot program. You can find dot at
	// http://www.research.att.com/sw/tools/graphviz/

	public void writeDot(String filename) throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(filename));
		out.println("// Format this file as a Postscript file with ");
		out.println("//    dot " + filename + " -Tps -o out.ps\n");
		out.println("digraph dfa {");
		out.println("size=\"11,8.25\";");
		out.println("rotate=90;");
		out.println("rankdir=LR;");
		out.println("n999999 [style=invis];"); // Invisible start node
		out.println("n999999 -> n" + startState); // Edge into start state

		// Accept states are double circles
		for (Integer state : trans.keySet())
			if (acceptStates.contains(state))
				out.println("n" + state + " [peripheries=2];");

		// The transitions
		for (Map.Entry<Integer, Map<String, Integer>> entry : trans.entrySet()) {
			Integer s1 = entry.getKey();
			for (Map.Entry<String, Integer> s1Trans : entry.getValue().entrySet()) {
				String lab = s1Trans.getKey();
				Integer s2 = s1Trans.getValue();
				out.println("n" + s1 + " -> n" + s2 + " [label=" + lab + "];");
			}
		}
		out.println("}");
		out.close();
	}
}
