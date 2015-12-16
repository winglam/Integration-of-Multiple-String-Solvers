package edu.illinois.cs.iomss.Solvers;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MAssertIn;
import edu.illinois.cs.iomss.MainLanguage.MAssertNotIn;
import edu.illinois.cs.iomss.MainLanguage.MCFG;
import edu.illinois.cs.iomss.MainLanguage.MConcat;
import edu.illinois.cs.iomss.MainLanguage.MConcatRegex;
import edu.illinois.cs.iomss.MainLanguage.MConcatString;
import edu.illinois.cs.iomss.MainLanguage.MContains;
import edu.illinois.cs.iomss.MainLanguage.MEqual;
import edu.illinois.cs.iomss.MainLanguage.MExpression;
import edu.illinois.cs.iomss.MainLanguage.MFix;
import edu.illinois.cs.iomss.MainLanguage.MFixedLength;
import edu.illinois.cs.iomss.MainLanguage.MID;
import edu.illinois.cs.iomss.MainLanguage.MIntLIT;
import edu.illinois.cs.iomss.MainLanguage.MNotContains;
import edu.illinois.cs.iomss.MainLanguage.MNotEqual;
import edu.illinois.cs.iomss.MainLanguage.MOr;
import edu.illinois.cs.iomss.MainLanguage.MRangedLength;
import edu.illinois.cs.iomss.MainLanguage.MRegex;
import edu.illinois.cs.iomss.MainLanguage.MStar;
import edu.illinois.cs.iomss.MainLanguage.MStatement;
import edu.illinois.cs.iomss.MainLanguage.MStringDecl;
import edu.illinois.cs.iomss.MainLanguage.MStringLIT;
import edu.illinois.cs.iomss.MainLanguage.MSubstring;
import edu.illinois.cs.iomss.MainLanguage.MainLanguage;
import edu.illinois.cs.iomss.util.StringUtil;

public class HAMPIParser extends Parser {

    public HAMPIParser() {
        super();
    }

    @Override
    public void parse(MainLanguage input) throws Exception {
        super.parse(input);
        System.out.println("Parsing: HAMPI");
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
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case Concat:
            MConcat conc = (MConcat) statement;
            List<MExpression> li = conc.list;
            res = "concat(" + statementToString(li.get(0));
            for (int i = 1; i < li.size(); i++) {
                res += "," + statementToString(li.get(i));
            }
            res += ")";
            break;
        case Star:
            MStar star = (MStar) statement;
            res = "star(" + statementToString(star.regex_statement) + ")";
            break;
        case Fix:
            MFix fix = (MFix) statement;
            res = "fix(" + statementToString(fix.id1) + "," + statementToString(fix.number2) + ")";
            break;
        case Or:
            MOr or = (MOr) statement;
            List<MExpression> li_or = or.list;
            res = "or(" + statementToString(li_or.get(0));
            for (int i = 1; i < li_or.size(); i++) {
                res += "," + statementToString(li_or.get(i));
            }
            res += ")";
            break;
        case Expression:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
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
            MRegex reg = (MRegex) statement;
            res = "reg " + reg.id + " := " + statementToString(reg.regex_statement);
            //
            // String val = parseRegex(cond.parameters.get(1));
            // values.put(cond.parameters.get(0), val);
            // return "reg " + cond.parameters.get(0) + " := " + val;
            break;
        case AssertIn:
            MAssertIn asse = (MAssertIn) statement;
            res = "assert " + statementToString(asse.id1) + " in " + statementToString(asse.id2);
            break;
        case AssertNotIn:
            MAssertNotIn assenot = (MAssertNotIn) statement;
            res = "assert " + statementToString(assenot.id1) + " not in " + statementToString(assenot.id2);
            break;
        case ConcatRegex:
            MConcatRegex concr = (MConcatRegex) statement;
            res = "concat(" + statementToString(concr.regex_expression1) + ","
                    + statementToString(concr.regex_expression2) + ")";
            break;
        case FixedLength:
            MFixedLength fixe = (MFixedLength) statement;
            res = "var " + statementToString(fixe.string_expression1) + " : " + statementToString(fixe.int_expression2);
            break;
        case RangedLength:
            MRangedLength range = (MRangedLength) statement;
            res = "var " + statementToString(range.id1) + " : " + statementToString(range.int2) + " .. "
                    + statementToString(range.int3);
            break;
        case AssertInRegex:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
            // MAssertInRegex assertin = (MAssertInRegex) statement;
            // res = "assert " + statementToString(assertin.regex_expression1) +
            // " in "
            // + statementToString(assertin.regex_expression2);
        case Alias:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case Push:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case Pop:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case Solve:
            break;
        case SolveAll:
            break;
        case Show:
            break;
        case ShowString:
            break;
        case IsIn:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case CharAt:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case Equal:
            MEqual eq = (MEqual) statement;
            res = "assert " + statementToString(eq.expression1) + " equals " + statementToString(eq.expression2);
            break;
        case NotEqual:
            MNotEqual neq = (MNotEqual) statement;
            res = "assert " + statementToString(neq.expression1) + " not equals " + statementToString(neq.expression2);
            break;
        case LessThan:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case GreaterThan:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case LessOrEqual:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case GreaterOrEqual:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case Substring:
            MSubstring sub = (MSubstring) statement;
            res = statementToString(sub.string_expression1) + "[" + statementToString(sub.int_expression2) + ":"
                    + statementToString(sub.int_expression3) + "]";
            break;
        case StartsWith:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case EndsWith:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case Contains:
            MContains cont = (MContains) statement;
            res = "assert " + statementToString(cont.string_expression1) + " contains "
                    + statementToString(cont.string_expression2);
            break;
        case NotContains:
            MNotContains notcont = (MNotContains) statement;
            res = "assert " + statementToString(notcont.string_expression1) + " not contains "
                    + statementToString(notcont.string_expression2);
            break;
        case ConcatString:
            MConcatString concstr = (MConcatString) statement;
            res = "concat(" + statementToString(concstr.string_expression1) + ","
                    + StringUtil.splitConcatHampi(statementToString(concstr.string_expression2)) + ")";
            break;
        case Replace:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case Length:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case IndexOf:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case LastIndexOf:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case ID:
            MID id = (MID) statement;
            res = id.id;
            break;
        case Not:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case Plus:
            throw new Exception("Error: HAMPI can't handle " + statement.type);
        case CFG:
            MCFG cfg = (MCFG) statement;
            res = "cfg " + statementToString(cfg.id1) + " := " + cfg.cfg_def;
            break;
        case StringDecl:
            MStringDecl strd = (MStringDecl) statement;
            res = "val " + statementToString(strd.id1) + " := " + statementToString(strd.string_expression2);
            break;
        default:
            throw new Exception(statement.toString() + ": " + statement.type);
        }
        return res;
    }
    //
    // private String parseRegex(String str) throws Exception {
    // str = str.trim();
    // if (str.startsWith("\"")) { // string literal
    // return str;
    // } else if (!str.contains("(")) { // must be a variable name
    // if (!values.containsKey(str)) {
    // throw new Exception("Undefined variable: " + str);
    // } else {
    // return values.get(str);
    // }
    // } else {
    // int numArgument = 1;
    // int lvl = 0;
    // int firstSplit = -1;
    // for (int i = 0; i < str.length(); i++) {
    // char c = str.charAt(i);
    // if (c == '(') {
    // lvl++;
    // } else if (c == ')') {
    // lvl--;
    // } else if (c == ',') {
    // if (lvl == 1) {
    // numArgument++;
    // if (firstSplit == -1) {
    // firstSplit = i;
    // }
    // }
    // }
    // }
    // if (str.startsWith("star")) {
    // return "star(" + parseRegex(str.substring(str.indexOf("(") + 1,
    // str.length() - 1)) + ")";
    // }
    // if (numArgument == 1) {
    // return parseRegex(str.substring(str.indexOf("(") + 1, str.length() - 1));
    // }
    // if (str.startsWith("concat")) {
    // return "concat(" + (parseRegex(str.substring(str.indexOf("(") + 1,
    // firstSplit)) + ", "
    // + parseRegex("concat(" + str.substring(firstSplit + 1))) + ")";
    // } else if (str.startsWith("or")) {
    // return "or(" + (parseRegex(str.substring(str.indexOf("(") + 1,
    // firstSplit)) + ", "
    // + parseRegex("or(" + str.substring(firstSplit + 1))) + ")";
    // } else {
    // throw new Exception("Invalid format in HAMPI buildRegex");
    // }
    // }
    // }
}
