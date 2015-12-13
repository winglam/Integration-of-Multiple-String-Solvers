package edu.illinois.cs.iomss.MainLanguage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import edu.illinois.cs.iomss.util.StringUtil;

public class MainLanguage {

    public enum StatementType {
        BaseType, Concat, Star, Fix, Or, Expression, STR_LIT, INT_LIT, String, Int, Regex, AssertIn, ConcatRegex, FixedLength, AssertInRegex, Alias, Push, Pop, Solve, SolveAll, Show, ShowString, IsIn, CharAt, Equal, LessThan, GreaterThan, LessOrEqual, GreaterOrEqual, Substring, StartsWith, EndsWith, Contains, ConcatString, Replace, Length, IndexOf, LastIndexOf, ID;
    }

    private List<MStatement> statements;

    public MainLanguage(String inputFile) throws Exception {
        Scanner cin = new Scanner(new File(inputFile));
        statements = new ArrayList<MStatement>();
        String code = "";

        while (cin.hasNextLine()) {
            String line = cin.nextLine();
            int ind = line.indexOf("//");
            if (ind != -1) {
                line = line.substring(0, ind);
            }
            line = line.trim();
            code += line;
        }
        String[] cond = code.split(";");
        for (String subcond : cond) {
            subcond = subcond.trim();
            if (subcond.length() > 0) {
                statements.add(parse(subcond));
            }
        }
        cin.close();
    }

    private MStatement parse(String s) throws Exception {
        int leftParen = s.indexOf("(");
        if (leftParen == -1 || leftParen == 0 || !s.endsWith(")")) {
            throw new Exception("Invalid statement: " + s);
        }
        StatementType type = StringUtil.getType(s);

        List<String> parameters = StringUtil.splitString(s);
        switch (type) {
        case String:
            return new MString(parameters);
        case Int:
            return new MInt(parameters);
        case Regex:
            return new MRegex(parameters);
        case AssertIn:
            return new MAssertIn(parameters);
        case AssertInRegex:
            return new MAssertInRegex(parameters);
        case Alias:
            return new MAlias(parameters);
        case Push:
            return new MPush(parameters);
        case Pop:
            return new MPop(parameters);
        case Solve:
            return new MSolve(parameters);
        case SolveAll:
            return new MSolveAll(parameters);
        case Show:
            return new MShow(parameters);
        case ShowString:
            return new MShowString(parameters);
        case IsIn:
            return new MIsIn(parameters);
        case Equal:
            return new MEqual(parameters);
        case LessThan:
            return new MLessThan(parameters);
        case GreaterThan:
            return new MGreaterThan(parameters);
        case LessOrEqual:
            return new MLessOrEqual(parameters);
        case GreaterOrEqual:
            return new MGreaterOrEqual(parameters);
        case Substring:
            return new MSubstring(parameters);
        case StartsWith:
            return new MStartsWith(parameters);
        case EndsWith:
            return new MEndsWith(parameters);
        case Contains:
            return new MContains(parameters);
        case FixedLength:
            return new MFixedLength(parameters);
        default:
            throw new Exception("Invalid statement: " + s);
        }
    }

    @Override
    public String toString() {
        String ret = "";
        for (MStatement s : statements) {
            ret += s.toString() + System.lineSeparator();
        }
        return ret;
    }

    public List<MStatement> getStatements() {
        return Collections.unmodifiableList(statements);
    }
}
