package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MStringLIT extends MStringExpression {

    public String s;

    public MStringLIT(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.STR_LIT;
        if (parameters.size() != 1) {
            throw new Exception();
        }

        s = parameters.get(0);
    }

    @Override
    public String toString() {
        return type + "(" + s + ")";
    }
}
