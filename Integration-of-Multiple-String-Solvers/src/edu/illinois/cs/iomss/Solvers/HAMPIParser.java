package edu.illinois.cs.iomss.Solvers;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.Condition;
import edu.illinois.cs.iomss.MainLanguage.MainLanguage;

public class HAMPIParser extends Parser {

    private String solveFor; // variable we want to solve;

    public HAMPIParser(MainLanguage conditions) {
        super(conditions);
    }

    @Override
    public void parse() throws Exception {
        super.parse();
        System.out.println("Parsing: HAMPI");
        List<Condition> conditions = this.constraints.getConditions();
        result = new ArrayList<String>();
        for (Condition cond : conditions) {
            String temp = conditionToString(cond);
            if (temp != "") {
                result.add(temp + ";");
            }
        }
    }

    private String conditionToString(Condition cond) throws Exception {
        switch (cond.function) {
        case SolveFor:
            solveFor = cond.parameters.get(0);
            return "";
        case Reg:
            String val = parseRegex(cond.parameters.get(1));
            values.put(cond.parameters.get(0), val);
            return "reg " + cond.parameters.get(0) + " := " + val;
        case AssertIn:
            return "assert " + cond.parameters.get(0) + " in " + cond.parameters.get(1);
        case SolLength:
            String var = cond.parameters.get(0);
            if (!var.equals(solveFor)) {
                throw new Exception();
            }
            return "var " + solveFor + " : " + cond.parameters.get(1);
        case Int:
            System.out.println("Warning: HAMPI can't handle int function");
            return "";
        case String:
            System.out.println("Warning: HAMPI can't handle string function");
            return "";
        case Contains:
            return "assert " + cond.parameters.get(0) + " contains " + cond.parameters.get(1);
        case NotContains:
            return "assert " + cond.parameters.get(0) + " not contains " + cond.parameters.get(1);
        case Length:
            throw new Exception("Error: HAMPI can't handle length");
        case SubstringEqual:
            throw new Exception("Error: HAMPI can't handle substring");
        case CharAtEqual:
            throw new Exception("Error: HAMPI can't handle charAt");
        case StartsWith:
            throw new Exception("Error: HAMPI can't handle startWith");
        case EndsWith:
            throw new Exception("Error: HAMPI can't handle endWith");
        default:
            throw new Exception("Unknown function in HAMPI");
        }
    }

    private String parseRegex(String str) throws Exception {
        str = str.trim();
        if (str.startsWith("\"")) { // string literal
            return str;
        } else if (!str.contains("(")) { // must be a variable name
            if (!values.containsKey(str)) {
                throw new Exception("Undefined variable: " + str);
            } else {
                return values.get(str);
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
                return "star(" + parseRegex(str.substring(str.indexOf("(") + 1, str.length() - 1)) + ")";
            }
            if (numArgument == 1) {
                return parseRegex(str.substring(str.indexOf("(") + 1, str.length() - 1));
            }
            if (str.startsWith("concat")) {
                return "concat(" + (parseRegex(str.substring(str.indexOf("(") + 1, firstSplit)) + ", "
                        + parseRegex("concat(" + str.substring(firstSplit + 1))) + ")";
            } else if (str.startsWith("or")) {
                return "or(" + (parseRegex(str.substring(str.indexOf("(") + 1, firstSplit)) + ", "
                        + parseRegex("or(" + str.substring(firstSplit + 1))) + ")";
            } else {
                throw new Exception("Invalid format in HAMPI buildRegex");
            }
        }
    }
}
