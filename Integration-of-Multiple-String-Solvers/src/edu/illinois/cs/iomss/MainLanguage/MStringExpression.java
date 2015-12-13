package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;
import edu.illinois.cs.iomss.util.StringUtil;

public class MStringExpression extends MExpression {

    public MStringExpression(List<String> parameters) throws Exception {
        super(parameters);
        // TODO Auto-generated constructor stub
    }

    public static MExpression parse(String s) throws Exception {
        StatementType type = StringUtil.getType(s);
        List<String> parameters = StringUtil.splitString(s);
        switch (type) {
        case ConcatString:
            return new MConcatString(parameters);
        case CharAt:
            return new MCharAt(parameters);
        case Replace:
            return new MReplace(parameters);
        case ID:
            return new MID(parameters);
        case STR_LIT:
            return new MStringLIT(parameters);
        default:
            throw new Exception();
        }
    }
}
