package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MConcatRegex extends MExpression {

    public MExpression regex_expression1;
    public MExpression regex_expression2;

    public MConcatRegex(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.ConcatRegex;
        if (parameters.size() != 2) {
            throw new Exception();
        }
        regex_expression1 = MRegexExpression.parse(parameters.get(0));
        regex_expression2 = MRegexExpression.parse(parameters.get(1));
    }

    @Override
    public String toString() {
        return type + "(" + regex_expression1 + "," + regex_expression2 + ")";
    }
}