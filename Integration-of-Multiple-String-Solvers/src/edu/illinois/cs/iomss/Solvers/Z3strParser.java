package edu.illinois.cs.iomss.Solvers;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.Condition;
import edu.illinois.cs.iomss.MainLanguage.MainLanguage;

public class Z3strParser extends Parser {

    public Z3strParser(MainLanguage constraints) {
        super(constraints);
    }

    @Override
    public void parse() throws Exception {
        super.parse();
        System.out.println("Parsing: Z3str");
        List<Condition> conditions = this.constraints.getConditions();
        result = new ArrayList<String>();
        for (Condition cond : conditions) {
            String temp = conditionToString(cond);
            if (temp != "") {
                result.add(temp);
            }
        }

        result.add("(check-sat)");
        result.add("(get-model)");
    }

    private String conditionToString(Condition cond) throws Exception {
        String var;
        switch (cond.function) {
        case SolveFor:
            return "";
        case Reg:
            String val = parseRegex(cond.parameters.get(1));
            values.put(cond.parameters.get(0), val);
            return "(define-fun " + cond.parameters.get(0) + " () Regex " + val + ")";
        case AssertIn:
            return "(assert (RegexIn " + cond.parameters.get(0) + " " + cond.parameters.get(1) + "))";
        case SolLength:
            var = cond.parameters.get(0);
            return "(assert (= (Length " + var + ") " + cond.parameters.get(1) + "))";
        case Int:
            return "(declare-variable " + cond.parameters.get(0) + " Int)";
        case String:
            return "(declare-variable " + cond.parameters.get(0) + " String)";
        case Contains:
            return "(assert (Contains " + cond.parameters.get(0) + " " + cond.parameters.get(1) + "))";
        case NotContains:
            return "(assert (not (Contains " + cond.parameters.get(0) + " " + cond.parameters.get(1) + ")))";
        case Length:
            var = cond.parameters.get(0);
            return "(assert (" + cond.parameters.get(1) + " (Length " + var + ") " + cond.parameters.get(2) + "))";
        case SubstringEqual:
            return "(assert (= " + cond.parameters.get(3) + " (Substring " + cond.parameters.get(0) + " "
                    + cond.parameters.get(1) + " " + cond.parameters.get(2) + ")))";
        case CharAtEqual:
            return "(assert (= " + cond.parameters.get(2) + " (CharAt " + cond.parameters.get(0) + " "
                    + cond.parameters.get(1) + ")))";
        case StartsWith:
            return "(assert (StartsWith " + cond.parameters.get(0) + " " + cond.parameters.get(1) + "))";
        case EndsWith:
            return "(assert (EndsWith " + cond.parameters.get(0) + " " + cond.parameters.get(1) + "))";
        case IntCompare:
            var = cond.parameters.get(0);
            return "(assert (" + cond.parameters.get(1) + " " + var + " " + cond.parameters.get(2) + "))";
        default:
            throw new Exception("Unknown function in Z3str");
        }
    }

    private String parseRegex(String str) throws Exception {
        str = str.trim();
        if (str.startsWith("\"")) { // string literal
            return "(Str2Reg " + str + ")";
        } else if (!str.contains("(")) { // must be a variable name
            if (!values.containsKey(str)) {
                return str;
                // throw new Exception("Undefined variable: " + str);
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
                return "(RegexStar " + (parseRegex(str.substring(str.indexOf("(") + 1, str.length() - 1))) + ")";
            }
            if (numArgument == 1) {
                return parseRegex(str.substring(str.indexOf("(") + 1, str.length() - 1));
            }
            if (str.startsWith("concat")) {
                return "(RegexConcat " + (parseRegex(str.substring(str.indexOf("(") + 1, firstSplit)) + " "
                        + parseRegex("concat(" + str.substring(firstSplit + 1))) + ")";
            } else if (str.startsWith("or")) {
                return "(RegexUnion " + (parseRegex(str.substring(str.indexOf("(") + 1, firstSplit)) + " "
                        + parseRegex("or(" + str.substring(firstSplit + 1))) + ")";
            } else {
                throw new Exception("Invalid format in Z3str buildRegex");
            }
        }
    }
}
