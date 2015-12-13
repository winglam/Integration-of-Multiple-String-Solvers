package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MLessThan extends MStatement {

    public MExpression int_expression1;
    public MExpression int_expression2;

    public MLessThan(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.LessThan;
        if (parameters.size() != 2) {
            throw new Exception();
        }
        int_expression1 = MIntExpression.parse(parameters.get(0));
        int_expression2 = MIntExpression.parse(parameters.get(1));
    }

    @Override
    public String toString() {
        return type + "(" + int_expression1 + "," + int_expression2 + ")";
    }
}
