package edu.illinois.cs.iomss.Solvers;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MAssertIn;
import edu.illinois.cs.iomss.MainLanguage.MAssertNotIn;
import edu.illinois.cs.iomss.MainLanguage.MCharAt;
import edu.illinois.cs.iomss.MainLanguage.MConcatRegex;
import edu.illinois.cs.iomss.MainLanguage.MConcatString;
import edu.illinois.cs.iomss.MainLanguage.MContains;
import edu.illinois.cs.iomss.MainLanguage.MEndsWith;
import edu.illinois.cs.iomss.MainLanguage.MEqual;
import edu.illinois.cs.iomss.MainLanguage.MFixedLength;
import edu.illinois.cs.iomss.MainLanguage.MGreaterOrEqual;
import edu.illinois.cs.iomss.MainLanguage.MGreaterThan;
import edu.illinois.cs.iomss.MainLanguage.MID;
import edu.illinois.cs.iomss.MainLanguage.MIndexOf;
import edu.illinois.cs.iomss.MainLanguage.MInt;
import edu.illinois.cs.iomss.MainLanguage.MIntLIT;
import edu.illinois.cs.iomss.MainLanguage.MLastIndexOf;
import edu.illinois.cs.iomss.MainLanguage.MLength;
import edu.illinois.cs.iomss.MainLanguage.MLessOrEqual;
import edu.illinois.cs.iomss.MainLanguage.MLessThan;
import edu.illinois.cs.iomss.MainLanguage.MNot;
import edu.illinois.cs.iomss.MainLanguage.MNotContains;
import edu.illinois.cs.iomss.MainLanguage.MPlus;
import edu.illinois.cs.iomss.MainLanguage.MRegex;
import edu.illinois.cs.iomss.MainLanguage.MReplace;
import edu.illinois.cs.iomss.MainLanguage.MStartsWith;
import edu.illinois.cs.iomss.MainLanguage.MStatement;
import edu.illinois.cs.iomss.MainLanguage.MString;
import edu.illinois.cs.iomss.MainLanguage.MStringLIT;
import edu.illinois.cs.iomss.MainLanguage.MSubstring;
import edu.illinois.cs.iomss.MainLanguage.MainLanguage;

public class Z3strParser extends Parser {

    public Z3strParser() {
        super();
    }

    @Override
    public void parse(MainLanguage input) throws Exception {
        super.parse(input);
        System.out.println("Parsing: Z3str");
        List<MStatement> statements = input.getStatements();
        result = new ArrayList<String>();
        for (MStatement statement : statements) {
            String temp = statementToString(statement);
            if (temp != "") {
                result.add(temp + ";");
            }
        }
        result.add("(check-sat);");
        result.add("(get-model);");
    }

    private String statementToString(MStatement statement) throws Exception {
        String res = "";
        switch (statement.type) {
        case BaseType:
            throw new Exception("Error: Z3str can't handle " + statement.type);
        case Concat:
            // MConcat conc = (MConcat) statement;
            // List<MExpression> li = conc.list;
            // res = parseReg(li, 0, "RegexConcat");
            break;
        case Star:
            // MStar star = (MStar) statement;
            // res = "(RegexStar " + statementToString(star.regex_statement) +
            // ")";
            break;
        case Fix:
            throw new Exception("Error: Z3str can't handle " + statement.type);
        case Or:
            // MOr or = (MOr) statement;
            // List<MExpression> li_or = or.list;
            // res = parseReg(li_or, 0, "RegexUnion");
            break;
        case Expression:
            throw new Exception("Error: Z3str can't handle " + statement.type);
        case STR_LIT:
            MStringLIT str = (MStringLIT) statement;
            res = str.s;
            break;
        case INT_LIT:
            MIntLIT intl = (MIntLIT) statement;
            res = String.valueOf(intl.number);
            break;
        case String:
            MString strdec = (MString) statement;
            res = "(declare-variable " + strdec.id + " String)";
            break;
        case Int:
            MInt intdec = (MInt) statement;
            res = "(declare-variable " + intdec.id + " Int)";
            break;
        case Regex:
            MRegex reg = (MRegex) statement;
            String val = parseRegex(statementToString(reg.regex_statement));
            res = "(define-fun " + reg.id + " () Regex " + val + ")";
            break;
        case AssertIn:
            MAssertIn asse = (MAssertIn) statement;
            res = "(assert (RegexIn " + statementToString(asse.id1) + " " + statementToString(asse.id2) + "))";
            break;
        case AssertNotIn:
            MAssertNotIn assenot = (MAssertNotIn) statement;
            res = "(assert (not (RegexIn " + statementToString(assenot.id1) + " " + statementToString(assenot.id2)
                    + ")))";
            break;
        case ConcatRegex:
            MConcatRegex concr = (MConcatRegex) statement;
            res = "(RegexConcat " + statementToString(concr.regex_expression1) + ","
                    + statementToString(concr.regex_expression2) + ")";
            break;
        case FixedLength:
            MFixedLength fixe = (MFixedLength) statement;
            res = "(assert (= (Length " + statementToString(fixe.string_expression1) + ") "
                    + statementToString(fixe.int_expression2) + "))";
            break;
        case AssertInRegex:
            throw new Exception("Error: Z3str can't handle " + statement.type);
        case Alias:
            throw new Exception("Error: Z3str can't handle " + statement.type);
        case Push:
            throw new Exception("Error: Z3str can't handle " + statement.type);
        case Pop:
            throw new Exception("Error: Z3str can't handle " + statement.type);
        case Solve:
            break;
        case SolveAll:
            break;
        case Show:
            break;
        case ShowString:
            break;
        case IsIn:
            throw new Exception("Error: Z3str can't handle " + statement.type);
        case CharAt:
            MCharAt chara = (MCharAt) statement;
            res = "(CharAt " + statementToString(chara.string_expression1) + " "
                    + statementToString(chara.int_expression2) + ")";
            break;
        case Equal:
            MEqual equ = (MEqual) statement;
            res = "(assert (= " + statementToString(equ.expression1) + " " + statementToString(equ.expression2) + "))";
            break;
        case LessThan:
            MLessThan lt = (MLessThan) statement;
            res = "(assert (< " + statementToString(lt.int_expression1) + " " + statementToString(lt.int_expression2)
                    + "))";
            break;
        case GreaterThan:
            MGreaterThan gt = (MGreaterThan) statement;
            res = "(assert (> " + statementToString(gt.int_expression1) + " " + statementToString(gt.int_expression2)
                    + "))";
            break;
        case LessOrEqual:
            MLessOrEqual loe = (MLessOrEqual) statement;
            res = "(assert (<= " + statementToString(loe.int_expression1) + " " + statementToString(loe.int_expression2)
                    + "))";
            break;
        case GreaterOrEqual:
            MGreaterOrEqual goe = (MGreaterOrEqual) statement;
            res = "(assert (>= " + statementToString(goe.int_expression1) + " " + statementToString(goe.int_expression2)
                    + "))";
            break;
        case Substring:
            MSubstring sub = (MSubstring) statement;
            res = "(assert (Substring " + statementToString(sub.string_expression1) + " "
                    + statementToString(sub.int_expression2) + " " + statementToString(sub.int_expression3) + "))";
            break;
        case StartsWith:
            MStartsWith start = (MStartsWith) statement;
            res = "(assert (StartsWith " + statementToString(start.string_expression1) + " "
                    + statementToString(start.string_lit_expression2) + "))";
            break;
        case EndsWith:
            MEndsWith end = (MEndsWith) statement;
            res = "(assert (EndsWith " + statementToString(end.string_expression1) + " "
                    + statementToString(end.string_lit_expression2) + "))";
            break;
        case Contains:
            MContains cont = (MContains) statement;
            res = "(assert (Contains " + statementToString(cont.string_expression1) + " "
                    + statementToString(cont.string_expression2) + "))";
            break;
        case NotContains:
            MNotContains notcont = (MNotContains) statement;
            res = "(assert (not (Contains " + statementToString(notcont.string_expression1) + " "
                    + statementToString(notcont.string_expression2) + ")))";
            break;
        case ConcatString:
            MConcatString concstr = (MConcatString) statement;
            res = "(Concat " + statementToString(concstr.string_expression1) + " "
                    + statementToString(concstr.string_expression2) + ")";
            break;
        case Replace:
            MReplace rep = (MReplace) statement;
            res = "(Replace " + statementToString(rep.string_expression1) + " "
                    + statementToString(rep.string_expression2) + " " + statementToString(rep.string_expression3) + ")";
            break;
        case Length:
            MLength len = (MLength) statement;
            res = "(Length " + statementToString(len.string_expression1) + ")";
            break;
        case IndexOf:
            MIndexOf ind = (MIndexOf) statement;
            res = "(Indexof " + statementToString(ind.string_expression1) + " "
                    + statementToString(ind.string_expression2) + ")";
            break;
        case LastIndexOf:
            MLastIndexOf lind = (MLastIndexOf) statement;
            res = "(LastIndexof " + statementToString(lind.string_expression1) + " "
                    + statementToString(lind.string_expression2) + ")";
            break;
        case ID:
            MID id = (MID) statement;
            res = id.id;
            break;
        case Not:
            MNot not = (MNot) statement;
            res = "(not " + statementToString(not.statement1) + ")";
            break;
        case Plus:
            MPlus plus = (MPlus) statement;
            res = "(+ " + statementToString(plus.int_expression1) + " " + statementToString(plus.int_expression2) + ")";
            break;
        default:
            throw new Exception(statement.toString() + ": " + statement.type);
        }
        return res;
    }
    //
    // private String parseReg(List<MExpression> li, int cur, String func)
    // throws Exception {
    // if (cur + 1 == li.size()) {
    // return statementToString(li.get(cur));
    // } else {
    // return "(" + func + " " + statementToString(li.get(cur)) + " " +
    // parseReg(li, cur + 1, func) + ")";
    // }
    // }

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
            if (str.startsWith("Star")) {
                return "(RegexStar " + (parseRegex(str.substring(str.indexOf("(") + 1, str.length() - 1))) + ")";
            }
            if (numArgument == 1) {
                return parseRegex(str.substring(str.indexOf("(") + 1, str.length() - 1));
            }
            if (str.startsWith("Concat")) {
                return "(RegexConcat " + (parseRegex(str.substring(str.indexOf("(") + 1, firstSplit)) + " "
                        + parseRegex("Concat(" + str.substring(firstSplit + 1))) + ")";
            } else if (str.startsWith("or")) {
                return "(RegexUnion " + (parseRegex(str.substring(str.indexOf("(") + 1, firstSplit)) + " "
                        + parseRegex("Or(" + str.substring(firstSplit + 1))) + ")";
            } else {
                throw new Exception("Invalid format in Z3str buildRegex");
            }
        }
    }
}
