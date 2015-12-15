package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;
import edu.illinois.cs.iomss.util.StringUtil;

public class MStatement {
    public MainLanguage.StatementType type;
    public List<String> parameters;

    public MStatement(List<String> parameters) {
        type = StatementType.BaseType;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        String res = type + "(";
        boolean first = true;
        for (String s : parameters) {
            if (!first) {
                res += ",";
            }
            res += s;
            first = false;
        }
        res += ")";
        return res;
    }

    public static MStatement parse(String s) throws Exception {
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
        case AssertNotIn:
            return new MAssertNotIn(parameters);
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
        case NotContains:
            return new MNotContains(parameters);
        case FixedLength:
            return new MFixedLength(parameters);
        default:
            throw new Exception("Invalid statement: " + s);
        }
    }
}
