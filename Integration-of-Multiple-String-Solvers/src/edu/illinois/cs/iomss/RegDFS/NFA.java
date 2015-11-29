package edu.illinois.cs.iomss.RegDFS;

//Example 139 from page 109 of Java Precisely second edition (The MIT Press 2005)
//Author: Peter Sestoft (sestoft@itu.dk)

import java.util.*;
import java.io.*;

//Regular expressions, NFAs, DFAs, and dot graphs

//This file contains, in order:
//* A class Nfa for representing an NFA (a nondeterministic finite 
//  automaton), and for converting it to a DFA (a deterministic 
//  finite automaton).  Most complexity is in this class.
//* A class Dfa for representing a DFA, a deterministic finite 
//  automaton, and for writing a dot input file representing the DFA.
//* Classes for representing regular expressions, and for building an 
//  NFA from a regular expression
//* A test class that creates an NFA, a DFA, and a dot input file 
//  for a number of small regular expressions.  The DFAs are 
//  not minimized.

/* Class Nfa and conversion from NFA to DFA ---------------------------

A nondeterministic finite automaton (NFA) is represented as a HashMap
from state number (Integer) to a List of Transitions, a Transition
being a pair of a label lab (a String, null meaning epsilon) and a
target state (an Integer).

A DFA is created from an NFA in two steps:

 (1) Construct a DFA whose states are composite, namely sets of NFA
     states (Set of Integer).  This is done by methods 
     compositeDfaTrans and epsilonClose.

 (2) Replace composite states (Set of Integer) by simple states
     (Integer).  This is done by methods rename and mkRenamer.

Method compositeDfaTrans works as follows: 

 Create the epsilon-closure S0 (a HashSet of Integers) of the start
 state s0, and put it in a worklist.  Create an empty DFA transition
 relation, which is a HashMap from a composite state (an 
 epsilon-closed HashSet of Integers) to a HashMap from a label 
 (a non-null String) to a composite state.

 Repeatedly choose a composite state S from the worklist.  If it is
 not already in the keyset of the DFA transition relation, compute
 for every non-epsilon label lab the set T of states reachable by
 that label from some state s in S.  Compute the epsilon-closure
 Tclose of every such state T and put it on the worklist.  Then add
 the transition S -lab-> Tclose to the DFA transition relation, for
 every lab.

Method epsilonClosure works as follows: 

 Given a set S of states.  Put the states of S in a worklist.
 Repeatedly choose a state s from the worklist, and consider all
 epsilon-transitions s -eps-> s' from s.  If s' is in S already, then
 do nothing; otherwise add s' to S and the worklist.  When the
 worklist is empty, S is epsilon-closed; return S.

Method mkRenamer works as follows: 

 Given a Map from Set of Integer to something, create an injective
 Map from Set of Integer to Integer, by choosing a fresh Integer for
 every value of the map.

Method rename works as follows:

 Given a Map from Set of Integer to Map from String to Set of
 Integer, use the result of mkRenamer to replace all Sets of Integers
 by Integers.  

*/

class Nfa {
	private Integer startState;
	private Integer exitState; // This is the unique accept state
	private Map<Integer, List<Transition>> trans;

	public Nfa(Integer startState, Integer exitState) {
		this.startState = startState;
		this.exitState = exitState;
		trans = new HashMap<Integer, List<Transition>>();
		if (!startState.equals(exitState))
			trans.put(exitState, new LinkedList<Transition>());
	}

	public Integer getStart() {
		return startState;
	}

	public Integer getExit() {
		return exitState;
	}

	public Map<Integer, List<Transition>> getTrans() {
		return trans;
	}

	public void addTrans(Integer s1, String lab, Integer s2) {
		List<Transition> s1Trans;
		if (trans.containsKey(s1))
			s1Trans = trans.get(s1);
		else {
			s1Trans = new LinkedList<Transition>();
			trans.put(s1, s1Trans);
		}
		s1Trans.add(new Transition(lab, s2));
	}

	public void addTrans(Map.Entry<Integer, List<Transition>> tr) {
		// assert !trans.containsKey(tr.getKey());
		trans.put(tr.getKey(), tr.getValue());
	}

	public String toString() {
		return "NFA start=" + startState + " exit=" + exitState + "\n" + trans;
	}

	// Construct the transition relation of a composite-state DFA from
	// an NFA with start state s0 and transition relation trans (a
	// HashMap from Integer to List of Transition). The start state of
	// the constructed DFA is the epsilon closure of s0, and its
	// transition relation is a HashMap from a composite state (a
	// HashSet of Integers) to a HashMap from label (a String) to a
	// composite state (a HashSet of Integers).

	static Map<Set<Integer>, Map<String, Set<Integer>>> compositeDfaTrans(Integer s0,
			Map<Integer, List<Transition>> trans) {
		Set<Integer> S0 = epsilonClose(Collections.singleton(s0), trans);
		LinkedList<Set<Integer>> worklist = new LinkedList<Set<Integer>>();
		worklist.add(S0);
		// The transition relation of the DFA
		Map<Set<Integer>, Map<String, Set<Integer>>> res = new HashMap<Set<Integer>, Map<String, Set<Integer>>>();
		while (!worklist.isEmpty()) {
			Set<Integer> S = worklist.removeFirst();
			if (!res.containsKey(S)) {
				// The S -lab-> T transition relation being constructed for a
				// given S
				Map<String, Set<Integer>> STrans = new HashMap<String, Set<Integer>>();
				// For all s in S, consider all transitions s -lab-> t
				for (Integer s : S) {
					// For all non-epsilon transitions s -lab-> t, add t to T
					for (Transition tr : trans.get(s)) {
						if (tr.lab != null) { // Already a transition on lab
							Set<Integer> toState;
							if (STrans.containsKey(tr.lab))
								toState = STrans.get(tr.lab);
							else { // No transitions on lab yet
								toState = new HashSet<Integer>();
								STrans.put(tr.lab, toState);
							}
							toState.add(tr.target);
						}
					}
				}
				// Epsilon-close all T such that S -lab-> T, and put on worklist
				HashMap<String, Set<Integer>> STransClosed = new HashMap<String, Set<Integer>>();
				for (Map.Entry<String, Set<Integer>> entry : STrans.entrySet()) {
					Set<Integer> Tclose = epsilonClose(entry.getValue(), trans);
					STransClosed.put(entry.getKey(), Tclose);
					worklist.add(Tclose);
				}
				res.put(S, STransClosed);
			}
		}
		return res;
	}

	// Compute epsilon-closure of state set S in transition relation trans.
	// Parameter S is a Set of Integer.
	// Parameter trans is a HashMap from Integer to List of Transition.
	// The result is a Set of Integer.

	static Set<Integer> epsilonClose(Set<Integer> S, Map<Integer, List<Transition>> trans) {
		LinkedList<Integer> worklist = new LinkedList<Integer>(S);
		Set<Integer> res = new HashSet<Integer>(S);
		while (!worklist.isEmpty()) {
			Integer s = worklist.removeFirst();
			for (Transition tr : trans.get(s)) {
				if (tr.lab == null && !res.contains(tr.target)) {
					res.add(tr.target);
					worklist.add(tr.target);
				}
			}
		}
		return res;
	}

	// Compute a renamer, which is a Map from Set of Integer to Integer,
	// provided parameter states is a Collection of Set of Integer.

	static Map<Set<Integer>, Integer> mkRenamer(Collection<Set<Integer>> states) {
		Map<Set<Integer>, Integer> renamer = new HashMap<Set<Integer>, Integer>();
		for (Set<Integer> k : states)
			renamer.put(k, renamer.size());
		return renamer;
	}

	// Using a renamer (a Map from Set of Integer to Integer), replace
	// composite (Set of Integer) states with simple (Integer) states in
	// the transition relation trans, which is assumed to be a Map from
	// Set of Integer to Map from String to Set of Integer. The result
	// is a Map from Integer to Map from String to Integer.

	static Map<Integer, Map<String, Integer>> rename(Map<Set<Integer>, Integer> renamer,
			Map<Set<Integer>, Map<String, Set<Integer>>> trans) {
		Map<Integer, Map<String, Integer>> newtrans = new HashMap<Integer, Map<String, Integer>>();
		for (Map.Entry<Set<Integer>, Map<String, Set<Integer>>> entry : trans.entrySet()) {
			Set<Integer> k = entry.getKey();
			Map<String, Integer> newktrans = new HashMap<String, Integer>();
			for (Map.Entry<String, Set<Integer>> tr : entry.getValue().entrySet())
				newktrans.put(tr.getKey(), renamer.get(tr.getValue()));
			newtrans.put(renamer.get(k), newktrans);
		}
		return newtrans;
	}

	static Set<Integer> acceptStates(Set<Set<Integer>> states, Map<Set<Integer>, Integer> renamer, Integer exit) {
		Set<Integer> acceptStates = new HashSet<Integer>();
		for (Set<Integer> state : states)
			if (state.contains(exit))
				acceptStates.add(renamer.get(state));
		return acceptStates;
	}

	public Dfa toDfa() {
		Map<Set<Integer>, Map<String, Set<Integer>>> cDfaTrans = compositeDfaTrans(startState, trans);
		Set<Integer> cDfaStart = epsilonClose(Collections.singleton(startState), trans);
		Set<Set<Integer>> cDfaStates = cDfaTrans.keySet();
		Map<Set<Integer>, Integer> renamer = mkRenamer(cDfaStates);
		Map<Integer, Map<String, Integer>> simpleDfaTrans = rename(renamer, cDfaTrans);
		Integer simpleDfaStart = renamer.get(cDfaStart);
		Set<Integer> simpleDfaAccept = acceptStates(cDfaStates, renamer, exitState);
		return new Dfa(simpleDfaStart, simpleDfaAccept, simpleDfaTrans);
	}

	// Nested class for creating distinctly named states when constructing NFAs

	static class NameSource {
		private int nextName = 0;

		public Integer next() {
			return nextName++;
		}
	}

	// Nested class for representing a transition from one state to another

	public static class Transition {
		String lab;
		Integer target;

		public Transition(String lab, Integer target) {
			this.lab = lab;
			this.target = target;
		}

		public String toString() {
			return "-" + lab + "-> " + target;
		}
	}
}
