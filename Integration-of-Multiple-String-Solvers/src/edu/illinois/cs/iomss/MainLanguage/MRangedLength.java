package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;
import edu.illinois.cs.iomss.util.StringUtil;

public class MRangedLength extends MStatement {
    public MID id1;
    public MIntLIT int2;
    public MIntLIT int3;

    public MRangedLength(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.RangedLength;
        if (parameters.size() != 3) {
            throw new Exception();
        }
        id1 = new MID(StringUtil.splitString(parameters.get(0)));
        int2 = new MIntLIT(StringUtil.splitString(parameters.get(1)));
        int3 = new MIntLIT(StringUtil.splitString(parameters.get(2)));
    }

    @Override
    public String toString() {
        return type + "(" + id1 + "," + int2 + "," + int3 + ")";
    }
}
