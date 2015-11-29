package edu.illinois.cs.iomss.RegDFS;

import java.io.IOException;


public class Example {
	public static void main(String[] args) throws IOException {
		MyRegex a = new Sym("A");
		MyRegex b = new Sym("B");
		MyRegex r = new Seq(new Star(new Alt(a, b)), new Seq(a, b));
		// The regular expression (a|b)*ab
		buildAndShow("dfa1.dot", r);
		// The regular expression ((a|b)*ab)*
		buildAndShow("dfa2.dot", new Star(r));
		// The regular expression ((a|b)*ab)((a|b)*ab)
		buildAndShow("dfa3.dot", new Seq(r, r));
	}

	public static void buildAndShow(String filename, MyRegex r) throws IOException {
		Nfa nfa = r.mkNfa(new Nfa.NameSource());
		System.out.println(nfa);
		Dfa dfa = nfa.toDfa();
		System.out.println(dfa);
		System.out.println("Writing DFA graph to file " + filename);
		dfa.writeDot(filename);
		System.out.println();
	}
}
