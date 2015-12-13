package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MStar extends MExpression {

    public MExpression regex_statement;

    public MStar(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.Star;
        if (parameters.size() != 1) {
            throw new Exception();
        }
        regex_statement = MRegexStatement.parse(parameters.get(0));
    }

    @Override
    public String toString() {
        return type + "(" + regex_statement + ")";
    }
}
