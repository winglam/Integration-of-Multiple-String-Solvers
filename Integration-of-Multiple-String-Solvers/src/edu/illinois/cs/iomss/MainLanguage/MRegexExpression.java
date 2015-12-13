package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;
import edu.illinois.cs.iomss.util.StringUtil;

public class MRegexExpression extends MExpression {

    public MRegexExpression(List<String> parameters) throws Exception {
        super(parameters);
        // TODO Auto-generated constructor stub
    }

    public static MExpression parse(String s) throws Exception {
        StatementType type = StringUtil.getType(s);
        List<String> parameters = StringUtil.splitString(s);
        switch (type) {
        case ConcatRegex:
            return new MConcatRegex(parameters);
        case ID:
            return new MID(parameters);
        default:
            throw new Exception();
        }
    }
}
