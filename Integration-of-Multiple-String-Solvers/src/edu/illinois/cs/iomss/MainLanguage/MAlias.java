package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;
import edu.illinois.cs.iomss.util.StringUtil;

public class MAlias extends MStatement {
    public MID id;
    public MExpression regex_expression;

    public MAlias(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.Alias;
        if (parameters.size() != 2) {
            throw new Exception();
        }
        id = new MID(StringUtil.splitString(parameters.get(0)));
        regex_expression = MRegexExpression.parse(parameters.get(1));
    }

    @Override
    public String toString() {
        return type + "(" + id.toString() + "," + regex_expression.toString() + ")";
    }
}
