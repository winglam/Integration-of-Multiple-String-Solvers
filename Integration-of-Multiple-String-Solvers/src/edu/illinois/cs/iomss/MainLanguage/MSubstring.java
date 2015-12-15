package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MSubstring extends MStringExpression {

    public MExpression string_expression1;
    public MExpression int_expression2;
    public MExpression int_expression3;

    public MSubstring(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.Substring;
        if (parameters.size() != 3) {
            throw new Exception();
        }
        string_expression1 = MStringExpression.parse(parameters.get(0));
        int_expression2 = MIntExpression.parse(parameters.get(1));
        int_expression3 = MIntExpression.parse(parameters.get(2));
    }

    @Override
    public String toString() {
        return type + "(" + string_expression1 + "," + int_expression2 + "," + int_expression3 + ")";
    }
}
