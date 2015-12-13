package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MString extends MStatement {

    public String id;

    public MString(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.String;
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
