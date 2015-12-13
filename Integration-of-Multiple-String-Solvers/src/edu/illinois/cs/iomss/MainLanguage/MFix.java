package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;
import edu.illinois.cs.iomss.util.StringUtil;

public class MFix extends MExpression {

    public MID id1;
    public MIntLIT number2;

    public MFix(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.Fix;
        if (parameters.size() != 2) {
            throw new Exception();
        }
        id1 = new MID(StringUtil.splitString(parameters.get(0)));
        number2 = new MIntLIT(StringUtil.splitString(parameters.get(1)));
    }

    @Override
    public String toString() {
        return type + "(" + id1 + "," + number2 + ")";
    }

}
