package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MFixedLength extends MStatement {

    public MExpression string_expression1;
    public MExpression int_expression2;

    public MFixedLength(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.FixedLength;
        if (parameters.size() != 2) {
            throw new Exception();
        }
        string_expression1 = MStringExpression.parse(parameters.get(0));
        int_expression2 = MIntExpression.parse(parameters.get(1));
    }

    @Override
    public String toString() {
        return type + "(" + string_expression1 + "," + int_expression2 + ")";
    }
}