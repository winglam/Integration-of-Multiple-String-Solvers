package edu.illinois.cs.iomss.MainLanguage;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class MConcat extends MExpression {

    public List<MExpression> list;

    public MConcat(List<String> parameters) throws Exception {
        super(parameters);
        this.type = StatementType.Concat;
        if (parameters.size() < 1) {
            throw new Exception();
        }
        list = new ArrayList<MExpression>();
        for (String s : parameters) {
            list.add(MRegexStatement.parse(s));
        }
    }

    @Override
    public String toString() {
        String ret = type + "(" + list.get(0);
        for (int i = 1; i < list.size(); i++) {
            ret += "," + list.get(i);
        }
        ret += ")";
        return ret;
    }
}
