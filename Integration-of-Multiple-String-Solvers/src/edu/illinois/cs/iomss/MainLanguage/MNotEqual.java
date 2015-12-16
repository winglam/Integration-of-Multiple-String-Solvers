package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MNotEqual extends MStatement {

    public MExpression expression1;
    public MExpression expression2;

    public MNotEqual(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.NotEqual;
        if (parameters.size() != 2) {
            throw new Exception();
        }

        expression1 = MExpression.parse(parameters.get(0));
        expression2 = MExpression.parse(parameters.get(1));
    }

    @Override
    public String toString() {
        return type + "(" + expression1 + "," + expression2 + ")";
    }
}
