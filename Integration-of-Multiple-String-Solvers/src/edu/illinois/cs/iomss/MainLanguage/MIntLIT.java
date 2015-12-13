package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MIntLIT extends MIntExpression {

    public int number;

    public MIntLIT(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.INT_LIT;
        if (parameters.size() != 1) {
            throw new Exception();
        }

        number = Integer.parseInt(parameters.get(0));
    }

    @Override
    public String toString() {
        return type + "(" + number + ")";
    }
}
