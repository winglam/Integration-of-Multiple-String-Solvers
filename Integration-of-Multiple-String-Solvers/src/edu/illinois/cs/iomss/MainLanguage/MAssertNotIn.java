package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;
import edu.illinois.cs.iomss.util.StringUtil;

public class MAssertNotIn extends MStatement {
    public MID id1;
    public MID id2;

    public MAssertNotIn(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.AssertNotIn;
        if (parameters.size() != 2) {
            throw new Exception();
        }
        id1 = new MID(StringUtil.splitString(parameters.get(0)));
        id2 = new MID(StringUtil.splitString(parameters.get(1)));
    }

    @Override
    public String toString() {
        return type + "(" + id1.toString() + "," + id2.toString() + ")";
    }

}