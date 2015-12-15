package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;
import edu.illinois.cs.iomss.util.StringUtil;

public class MExpression extends MStatement {

    public MExpression(List<String> parameters) {
        super(parameters);
        // TODO Auto-generated constructor stub
    }

    public static MExpression parse(String s) throws Exception {
        StatementType type = StringUtil.getType(s);
        List<String> parameters = StringUtil.splitString(s);
        switch (type) {
        case ConcatRegex:
            return new MConcatRegex(parameters);
        case ConcatString:
            return new MConcatString(parameters);
        case CharAt:
            return new MCharAt(parameters);
        case Replace:
            return new MReplace(parameters);
        case Length:
            return new MLength(parameters);
        case IndexOf:
            return new MIndexOf(parameters);
        case LastIndexOf:
            return new MLastIndexOf(parameters);
        case ID:
            return new MID(parameters);
        case STR_LIT:
            return new MStringLIT(parameters);
        case INT_LIT:
            return new MIntLIT(parameters);
        case Plus:
            return new MPlus(parameters);

        default:
            throw new Exception();
        }
    }

}
