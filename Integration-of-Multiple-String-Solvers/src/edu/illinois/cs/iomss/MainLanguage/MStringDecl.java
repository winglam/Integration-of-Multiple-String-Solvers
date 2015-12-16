package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;
import edu.illinois.cs.iomss.util.StringUtil;

public class MStringDecl extends MStatement {

    public MID id1;
    public MExpression string_expression2;

    public MStringDecl(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.StringDecl;
        if (parameters.size() != 2) {
            throw new Exception();
        }
        id1 = new MID(StringUtil.splitString(parameters.get(0)));
        string_expression2 = MStringExpression.parse(parameters.get(1));
    }

    @Override
    public String toString() {
        return type + "(" + id1.toString() + "," + string_expression2 + ")";
    }
}
