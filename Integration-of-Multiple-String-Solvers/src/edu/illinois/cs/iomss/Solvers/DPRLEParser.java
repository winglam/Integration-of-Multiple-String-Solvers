package edu.illinois.cs.iomss.Solvers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.illinois.cs.iomss.MainLanguage.MAlias;
import edu.illinois.cs.iomss.MainLanguage.MAssertIn;
import edu.illinois.cs.iomss.MainLanguage.MAssertInRegex;
import edu.illinois.cs.iomss.MainLanguage.MConcatRegex;
import edu.illinois.cs.iomss.MainLanguage.MContains;
import edu.illinois.cs.iomss.MainLanguage.MEndsWith;
import edu.illinois.cs.iomss.MainLanguage.MFixedLength;
import edu.illinois.cs.iomss.MainLanguage.MID;
import edu.illinois.cs.iomss.MainLanguage.MIntLIT;
import edu.illinois.cs.iomss.MainLanguage.MIsIn;
import edu.illinois.cs.iomss.MainLanguage.MShowString;
import edu.illinois.cs.iomss.MainLanguage.MStartsWith;
import edu.illinois.cs.iomss.MainLanguage.MStatement;
import edu.illinois.cs.iomss.MainLanguage.MStringLIT;
import edu.illinois.cs.iomss.MainLanguage.MainLanguage;
import edu.illinois.cs.iomss.RegDFS.Alt;
import edu.illinois.cs.iomss.RegDFS.Dfa;
import edu.illinois.cs.iomss.RegDFS.MyRegex;
import edu.illinois.cs.iomss.RegDFS.Nfa;
import edu.illinois.cs.iomss.RegDFS.Seq;
import edu.illinois.cs.iomss.RegDFS.Star;
import edu.illinois.cs.iomss.RegDFS.Sym;

public class DPRLEParser extends Parser {

    public DPRLEParser() {
        super();
    }

    @Override
    public void parse(MainLanguage input) throws Exception {
        super.parse(input);
        System.out.println("Parsing: DPRLE");
        List<MStatement> statements = input.getStatements();
        result = new ArrayList<String>();
        for (MStatement statement : statements) {
            String temp = statementToString(statement);
            if (temp != "") {
                result.add(temp + ";");
            }
        }
    }

    private String statementToString(MStatement statement) throws Exception {
        String res = "";
        switch (statement.type) {
        case BaseType:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case Concat:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case Star:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case Fix:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case Or:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case Expression:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case STR_LIT:
            MStringLIT str = (MStringLIT) statement;
            res = str.s;
            break;
        case INT_LIT:
            MIntLIT intl = (MIntLIT) statement;
            res = String.valueOf(intl.number);
            break;
        case String:
            break;
        case Int:
            break;
        case Regex:
            String val = parseRegex(statement.parameters.get(1));
            res = statement.parameters.get(0) + " < " + val;
            values.put(statement.parameters.get(0), statement.parameters.get(1));
            break;
        case AssertIn:
            MAssertIn asse = (MAssertIn) statement;
            res = statementToString(asse.id1) + " < " + statementToString(asse.id2);
            break;
        case AssertNotIn:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case ConcatRegex:
            MConcatRegex conc = (MConcatRegex) statement;
            res = statementToString(conc.regex_expression1) + " . " + statementToString(conc.regex_expression2);
            break;
        case FixedLength:
            MFixedLength fixe = (MFixedLength) statement;
            res = getLengthConstraint(statementToString(fixe.string_expression1),
                    Integer.parseInt(statementToString(fixe.int_expression2)));
            break;
        case RangedLength:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case AssertInRegex:
            MAssertInRegex asse1 = (MAssertInRegex) statement;
            res = statementToString(asse1.regex_expression1) + " < " + statementToString(asse1.regex_expression2);
            break;
        case Alias:
            MAlias alia = (MAlias) statement;
            res = statementToString(alia.id) + " alias " + statementToString(alia.regex_expression);
            break;
        case Push:
            res = "push()";
            break;
        case Pop:
            res = "pop()";
            break;
        case Solve:
            res = "solve()";
            break;
        case SolveAll:
            res = "solve_all()";
            break;
        case Show:
            res = "show()";
            break;
        case ShowString:
            MShowString show = (MShowString) statement;
            if (show.empty) {
                res = "strings()";
            } else {
                res = "strings(" + statementToString(show.id1) + ")";
            }
            break;
        case IsIn:
            MIsIn isin = (MIsIn) statement;
            res = statementToString(isin.id1) + " ?< " + statementToString(isin.id2);
            break;
        case CharAt:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case Equal:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case NotEqual:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case LessThan:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case GreaterThan:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case LessOrEqual:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case GreaterOrEqual:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case Substring:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case StartsWith:
            MStartsWith start = (MStartsWith) statement;
            res = getStartsWithConstraint(statementToString(start.string_expression1),
                    statementToString(start.string_lit_expression2));
            break;
        case EndsWith:
            MEndsWith end = (MEndsWith) statement;
            res = getEndsWithConstraint(statementToString(end.string_expression1),
                    statementToString(end.string_lit_expression2));
            break;
        case Contains:
            MContains cont = (MContains) statement;
            res = getContainsConstraint(statementToString(cont.string_expression1),
                    statementToString(cont.string_expression2));
            break;
        case NotContains:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case ConcatString:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case Replace:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case Length:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case IndexOf:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case LastIndexOf:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case ID:
            MID id = (MID) statement;
            res = id.id;
            break;
        case Not:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case Plus:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case CFG:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        case StringDecl:
            throw new Exception("Error: DPRLE can't handle " + statement.type);
        default:
            throw new Exception(statement.toString() + ": " + statement.type);
        }
        return res;
    }

    private String getLengthConstraint(String var, int len) {
        String res = "";
        res += var + " < [" + newLine;
        res += "s: n0" + newLine;
        res += "f: acc_state" + newLine;
        res += "d:" + newLine;
        for (int i = 1; i <= len; i++) {
            res += "n" + (i - 1) + " -> " + "n" + i + " on any" + newLine;
        }
        res += "n" + len + " -> acc_state on epsilon" + newLine;
        res += "]" + newLine;
        return res;
    }

    private String getContainsConstraint(String var, String sub) {
        String res = "";
        res += var + " < [" + newLine;
        res += "s: n0" + newLine;
        res += "f: acc_state" + newLine;
        res += "d:" + newLine;
        sub = sub.substring(1, sub.length() - 1);
        for (int i = 1; i <= sub.length(); i++) {
            char x = sub.charAt(i - 1);
            res += "n" + (i - 1) + " -> " + "n" + i + " on {'" + x + "'}" + newLine;
        }
        res += "n" + sub.length() + " -> acc_state on epsilon" + newLine;
        res += "n0 -> n0 on any" + newLine;
        res += "acc_state -> acc_state on any" + newLine;
        res += "]" + newLine;
        return res;
    }

    private String getStartsWithConstraint(String var, String sub) {
        String res = "";
        res += var + " < [" + newLine;
        res += "s: n0" + newLine;
        res += "f: acc_state" + newLine;
        res += "d:" + newLine;
        sub = sub.substring(1, sub.length() - 1);
        for (int i = 1; i <= sub.length(); i++) {
            char x = sub.charAt(i - 1);
            res += "n" + (i - 1) + " -> " + "n" + i + " on {'" + x + "'}" + newLine;
        }
        res += "n" + sub.length() + " -> acc_state on epsilon" + newLine;
        res += "acc_state -> acc_state on any" + newLine;
        res += "]" + newLine;
        return res;
    }

    private String getEndsWithConstraint(String var, String sub) {
        String res = "";
        res += var + " < [" + newLine;
        res += "s: n0" + newLine;
        res += "f: acc_state" + newLine;
        res += "d:" + newLine;
        sub = sub.substring(1, sub.length() - 1);
        for (int i = 1; i <= sub.length(); i++) {
            char x = sub.charAt(i - 1);
            res += "n" + (i - 1) + " -> " + "n" + i + " on {'" + x + "'}" + newLine;
        }
        res += "n" + sub.length() + " -> acc_state on epsilon" + newLine;
        res += "n0 -> n0 on any" + newLine;
        res += "]" + newLine;
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
            return "Concat(\"" + str.charAt(0) + "\", " + splitString(str.substring(1)) + ")";
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
            if (str.startsWith("Star")) {
                return new Star(buildRegex(str.substring(str.indexOf("(") + 1, str.length() - 1)));
            }
            if (numArgument == 1) {
                return buildRegex(str.substring(str.indexOf("(") + 1, str.length() - 1));
            }
            if (str.startsWith("Concat")) {
                return new Seq(buildRegex(str.substring(str.indexOf("(") + 1, firstSplit)),
                        buildRegex("Concat(" + str.substring(firstSplit + 1)));
            } else if (str.startsWith("Or")) {
                return new Alt(buildRegex(str.substring(str.indexOf("(") + 1, firstSplit)),
                        buildRegex("Or(" + str.substring(firstSplit + 1)));
            } else {
                throw new Exception("Invalid format in DPRLE buildRegex: " + str);
            }
        }
    }
}
