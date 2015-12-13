package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MSolveAll extends MStatement {

    public MSolveAll(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.SolveAll;
        if (parameters.size() != 0) {
            throw new Exception();
        }
    }

    @Override
    public String toString() {
        return type + "()";
    }
}
