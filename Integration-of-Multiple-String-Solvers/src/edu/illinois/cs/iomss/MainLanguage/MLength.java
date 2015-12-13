package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MLength extends MIntExpression {

    public MExpression string_expression1;

    public MLength(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.Length;
        if (parameters.size() != 1) {
            throw new Exception();
        }

        string_expression1 = MStringExpression.parse(parameters.get(0));
    }

    @Override
    public String toString() {
        return type + "(" + string_expression1 + ")";
    }
}
