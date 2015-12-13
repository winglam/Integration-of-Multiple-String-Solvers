package edu.illinois.cs.iomss.MainLanguage;

import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;
import edu.illinois.cs.iomss.util.StringUtil;

public class MShowString extends MStatement {

    public MID id1;
    public boolean empty;

    public MShowString(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.ShowString;
        if (parameters.size() > 1) {
            throw new Exception();
        }
        empty = false;
        if (parameters.size() == 1) {
            id1 = new MID(StringUtil.splitString(parameters.get(0)));
        } else {
            empty = true;
        }
    }

    @Override
    public String toString() {
        if (empty) {
            return type + "()";
        } else {
            return type + "(" + id1 + ")";
        }
    }
}
