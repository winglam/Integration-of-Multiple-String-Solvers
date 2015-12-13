package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MInt extends MStatement {
    public String id;

    public MInt(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.Int;
        if (parameters.size() != 1) {
            throw new Exception();
        }

        id = parameters.get(0);
    }

    @Override
    public String toString() {
        return type + "(" + id + ")";
    }
}
