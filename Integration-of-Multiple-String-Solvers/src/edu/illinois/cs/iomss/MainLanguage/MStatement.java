package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MStatement {
    public MainLanguage.StatementType type;
    public List<String> parameters;

    public MStatement(List<String> parameters) {
        type = StatementType.BaseType;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        String res = type + "(";
        boolean first = true;
        for (String s : parameters) {
            if (!first) {
                res += ",";
            }
            res += s;
            first = false;
        }
        res += ")";
        return res;
    }
}
