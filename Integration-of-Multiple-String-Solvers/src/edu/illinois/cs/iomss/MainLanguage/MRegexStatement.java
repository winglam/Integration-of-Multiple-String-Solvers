package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;
import edu.illinois.cs.iomss.util.StringUtil;

public class MRegexStatement extends MExpression {

    public MRegexStatement(List<String> parameters) {
        super(parameters);
        // TODO Auto-generated constructor stub
    }

    public static MExpression parse(String s) throws Exception {
        StatementType type = StringUtil.getType(s);
        List<String> parameters = StringUtil.splitString(s);
        switch (type) {
        case Concat:
            return new MConcat(parameters);
        case Or:
            return new MOr(parameters);
        case Star:
            return new MStar(parameters);
        case Fix:
            return new MFix(parameters);
        case ID:
            return new MID(parameters);
        case STR_LIT:
            return new MStringLIT(parameters);
        default:
            throw new Exception();
        }
    }
}
