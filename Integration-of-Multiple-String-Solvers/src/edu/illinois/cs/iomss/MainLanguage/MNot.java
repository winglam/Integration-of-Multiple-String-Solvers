package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MNot extends MStatement {
    public MStatement statement1;

    public MNot(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.Not;
        if (parameters.size() != 1) {
            throw new Exception();
        }
        statement1 = MStatement.parse(parameters.get(0));
    }

    @Override
    public String toString() {
        return type + "(" + statement1 + ")";
    }
}
