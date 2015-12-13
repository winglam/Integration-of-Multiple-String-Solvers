package edu.illinois.cs.iomss.util;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage;
import edu.illinois.cs.iomss.MainLanguage.MainLanguage.StatementType;

public class StringUtil {

    public static List<String> splitString(String s) {
        List<String> ret = new ArrayList<String>();
        int leftParen = s.indexOf("(");
        if (leftParen == -1 || leftParen == 0 || !s.endsWith(")")) {
            ret.add(s);
            return ret;
        }
        s = s.substring(leftParen + 1, s.length() - 1);
        int lvl = 0, start = 0, i = 0;

        for (i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                lvl++;
            } else if (c == ')') {
                lvl--;
            } else if (c == ',') {
                if (lvl == 0) {
                    String temp = s.substring(start, i).trim();
                    if (temp.length() > 0) {
                        ret.add(temp);
                    }
                    start = i + 1;
                }
            }
        }
        String temp = s.substring(start).trim();
        if (temp.length() > 0) {
            ret.add(temp);
        }
        return ret;
    }

    public static StatementType getType(String s) {
        if (s.startsWith("\"")) {
            return StatementType.STR_LIT;
        }
        try {
            Integer.parseInt(s);
            return StatementType.INT_LIT;
        } catch (Exception e) {
            int leftParen = s.indexOf("(");
            if (leftParen == -1 || leftParen == 0 || !s.endsWith(")")) {
                return StatementType.ID;
            } else {
                StatementType type = MainLanguage.StatementType.valueOf(s.substring(0, leftParen));
                return type;
            }
        }
    }
}
