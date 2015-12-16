package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;
import edu.illinois.cs.iomss.util.StringUtil;

public class MCFG extends MStatement {

    public MID id1;
    public String cfg_def;

    public MCFG(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.CFG;
        if (parameters.size() != 2) {
            throw new Exception();
        }
        id1 = new MID(StringUtil.splitString(parameters.get(0)));
        cfg_def = parameters.get(1);
    }

    @Override
    public String toString() {
        return type + "(" + id1.toString() + "," + cfg_def + ")";
    }
}
