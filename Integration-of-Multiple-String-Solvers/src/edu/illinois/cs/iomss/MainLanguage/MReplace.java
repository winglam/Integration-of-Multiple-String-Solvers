package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MReplace extends MStringExpression {

    public MExpression string_expression1;
    public MExpression string_expression2;
    public MExpression string_expression3;

    public MReplace(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.Replace;
        if (parameters.size() != 3) {
            throw new Exception();
        }

        string_expression1 = MStringExpression.parse(parameters.get(0));
        string_expression2 = MStringExpression.parse(parameters.get(1));
        string_expression3 = MStringExpression.parse(parameters.get(2));
    }

    @Override
    public String toString() {
        return type + "(" + string_expression1 + "," + string_expression2 + "," + string_expression3 + ")";
    }
}
