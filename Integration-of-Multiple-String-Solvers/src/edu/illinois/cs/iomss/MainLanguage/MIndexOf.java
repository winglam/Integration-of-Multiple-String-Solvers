package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MIndexOf extends MIntExpression {

    public MExpression string_expression1;
    public MExpression string_expression2;

    public MIndexOf(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.IndexOf;
        if (parameters.size() != 2) {
            throw new Exception();
        }

        string_expression1 = MStringExpression.parse(parameters.get(0));
        string_expression2 = MStringExpression.parse(parameters.get(1));
    }

    @Override
    public String toString() {
        return type + "(" + string_expression1 + "," + string_expression2 + ")";
    }
}
