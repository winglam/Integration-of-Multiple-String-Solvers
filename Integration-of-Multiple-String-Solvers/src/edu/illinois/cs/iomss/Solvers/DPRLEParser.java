package edu.illinois.cs.iomss.Solvers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.illinois.cs.iomss.MainLanguage.Condition;
import edu.illinois.cs.iomss.MainLanguage.MainLanguage;
import edu.illinois.cs.iomss.RegDFS.Alt;
import edu.illinois.cs.iomss.RegDFS.Dfa;
import edu.illinois.cs.iomss.RegDFS.MyRegex;
import edu.illinois.cs.iomss.RegDFS.Nfa;
import edu.illinois.cs.iomss.RegDFS.Seq;
import edu.illinois.cs.iomss.RegDFS.Star;
import edu.illinois.cs.iomss.RegDFS.Sym;

public class DPRLEParser extends Parser {

    private List<String> solveFor; // variable we want to solve;

    public DPRLEParser(MainLanguage conditions) {
        super(conditions);
    }

    @Override
    public void parse() throws Exception {
        super.parse();
        System.out.println("Parsing: DPRLE");
        List<Condition> conditions = this.constraints.getConditions();
        result = new ArrayList<String>();
        for (Condition cond : conditions) {
            String temp = conditionToString(cond);
            if (temp != "") {
                result.add(temp + ";");
            }
        }

        result.add("solve();");
        for (String var : solveFor) {
            result.add("strings(" + var + ");");
        }
    }

    public enum Function {
        SolveFor, Reg, AssertIn, Length, Int;
    }

    private String conditionToString(Condition cond) throws Exception {
        String res = "";
        switch (cond.function) {
        case SolveFor:
            solveFor = new ArrayList<>(cond.parameters);
            break;
        case Reg:
            String val = parseRegex(cond.parameters.get(1));
            res = cond.parameters.get(0) + " < " + val;
            values.put(cond.parameters.get(0), cond.parameters.get(1));
            break;
        case AssertIn:
            res = cond.parameters.get(0) + " < " + cond.parameters.get(1);
            break;
        case Length:
            res = getLengthConstraint(cond.parameters.get(0), Integer.parseInt(cond.parameters.get(1)));
            break;
        case Int:
            System.out.println("Warning: DPRLE can't handle int function");
            break;
        case String:
            System.out.println("Warning: DPRLE can't handle string function");
            break;
        default:
            throw new Exception("Unknown function in DPRLE");
        }
        return res;
    }

    private String getLengthConstraint(String var, int len) {
        String res = "";
        res += var + "_length < [" + newLine;
        res += "s: n0" + newLine;
        res += "f: acc_state" + newLine;
        res += "d:" + newLine;
        for (int i = 1; i <= len; i++) {
            res += "n" + (i - 1) + " -> " + "n" + i + " on any" + newLine;
        }
        res += "n" + len + " -> acc_state on epsilon" + newLine;
        res += "];" + newLine;
        res += var + " < " + var + "_length";
        return res;
    }

    private String parseRegex(String str) throws Exception {
        String res = "[" + newLine;
        MyRegex regex = buildRegex(str);
        Nfa nfa = regex.mkNfa(new Nfa.NameSource());
        Dfa dfa = nfa.toDfa();
        res += "s: n" + dfa.getStart() + newLine;
        res += "f: acc_state" + newLine;
        res += "d:" + newLine;
        for (int end : dfa.getAccept()) {
            res += "n" + end + " -> acc_state on epsilon" + newLine;
        }

        // The transitions
        Map<Integer, Map<String, Integer>> transitions = dfa.getTrans();
        for (Integer s1 : transitions.keySet()) {
            Map<String, Integer> s1Trans = transitions.get(s1);
            for (String lab : s1Trans.keySet()) {
                Integer s2 = s1Trans.get(lab);
                lab = lab.replace('"', '\'');
                if (lab.length() == 2) { // transition on epsilon
                    res += "n" + s1 + " -> n" + s2 + " on epsilon" + newLine;
                } else {
                    res += "n" + s1 + " -> n" + s2 + " on {" + lab + "}" + newLine;
                }
            }
        }
        res += "]" + newLine;
        return res;
    }

    private String splitString(String str) {
        if (str.length() == 1) {
            return "\"" + str + "\"";
        } else {
            return "concat(\"" + str.charAt(0) + "\", " + splitString(str.substring(1)) + ")";
        }
    }

    private MyRegex buildRegex(String str) throws Exception {
        str = str.trim();
        if (str.startsWith("\"")) { // string literal
            if (str.length() <= 3) { // a single char or epsilon, this is good.
                return new Sym(str);
            } else {
                return buildRegex(splitString(str.substring(1, str.length() - 1)));
            }
        } else if (!str.contains("(")) { // must be a variable name
            if (!values.containsKey(str)) {
                throw new Exception("Undefined variable: " + str);
            } else {
                return buildRegex(values.get(str));
            }
        } else {
            int numArgument = 1;
            int lvl = 0;
            int firstSplit = -1;
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == '(') {
                    lvl++;
                } else if (c == ')') {
                    lvl--;
                } else if (c == ',') {
                    if (lvl == 1) {
                        numArgument++;
                        if (firstSplit == -1) {
                            firstSplit = i;
                        }
                    }
                }
            }
            if (str.startsWith("star")) {
                return new Star(buildRegex(str.substring(str.indexOf("(") + 1, str.length() - 1)));
            }
            if (numArgument == 1) {
                return buildRegex(str.substring(str.indexOf("(") + 1, str.length() - 1));
            }
            if (str.startsWith("concat")) {
                return new Seq(buildRegex(str.substring(str.indexOf("(") + 1, firstSplit)),
                        buildRegex("concat(" + str.substring(firstSplit + 1)));
            } else if (str.startsWith("or")) {
                return new Alt(buildRegex(str.substring(str.indexOf("(") + 1, firstSplit)),
                        buildRegex("or(" + str.substring(firstSplit + 1)));
            } else {
                throw new Exception("Invalid format in DPRLE buildRegex");
            }
        }
    }
}
