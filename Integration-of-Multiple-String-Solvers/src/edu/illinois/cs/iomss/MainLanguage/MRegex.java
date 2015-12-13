package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MRegex extends MStatement {

    public String id;
    public MExpression regex_statement;

    public MRegex(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.Regex;
        if (parameters.size() != 2) {
            throw new Exception();
        }
        id = parameters.get(0);
        regex_statement = MRegexStatement.parse(parameters.get(1));
    }

    @Override
    public String toString() {
        return type + "(" + id + "," + regex_statement + ")";
    }
}
