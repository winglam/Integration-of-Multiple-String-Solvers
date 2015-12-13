package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;
import edu.illinois.cs.iomss.util.StringUtil;

public class MStartsWith extends MStatement {

    public MExpression string_expression1;
    public MExpression string_lit_expression2;

    public MStartsWith(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.StartsWith;
        if (parameters.size() != 2) {
            throw new Exception();
        }
        string_expression1 = MStringExpression.parse(parameters.get(0));
        string_lit_expression2 = new MStringLIT(StringUtil.splitString(parameters.get(1)));
    }

    @Override
    public String toString() {
        return type + "(" + string_expression1 + "," + string_lit_expression2 + ")";
    }
}
