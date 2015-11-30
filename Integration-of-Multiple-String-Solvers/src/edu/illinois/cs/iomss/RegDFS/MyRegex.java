package edu.illinois.cs.iomss.RegDFS;

//Regular expressions ----------------------------------------------
//
//Abstract syntax of regular expressions
//r ::= A | r1 r2 | (r1|r2) | r*
//

public abstract class MyRegex {
    abstract public Nfa mkNfa(Nfa.NameSource names);
}
