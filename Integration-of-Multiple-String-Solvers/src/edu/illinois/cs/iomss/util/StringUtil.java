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
        int lvl = 0, start = 0;
        for (int i = 0; i < s.length(); i++) {
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

    public static String splitConcatHampi(String s) {
        s = s.trim();
        if (s.startsWith("\"")) {
            return s;
        }
        int leftParen = s.indexOf("(");
        if (leftParen == -1 || leftParen == 0 || !s.endsWith(")")) {
            return s;
        }
        s = s.substring(leftParen + 1, s.length() - 1);
        int lvl = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                lvl++;
            } else if (c == ')') {
                lvl--;
            } else if (c == ',') {
                if (lvl == 0) {
                    return splitConcatHampi(s.substring(0, i)) + "," + splitConcatHampi(s.substring(i + 1));
                }
            }
        }
        return splitConcatHampi(s);
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

    public static String[] splitCode(String code) {
        int lvl = 0;
        List<Integer> splitLocations = new ArrayList<Integer>();
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '(') {
                lvl++;
            } else if (c == ')') {
                lvl--;
            } else if (c == ';') {
                if (lvl == 0) {
                    splitLocations.add(i);
                }
            }
        }
        lvl = 0;
        String[] ret = new String[splitLocations.size() + 1];
        int pre = 0, ind = 0;
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '(') {
                lvl++;
            } else if (c == ')') {
                lvl--;
            } else if (c == ';') {
                if (lvl == 0) {
                    ret[ind++] = code.substring(pre, i);
                    pre = i + 1;
                }
            }
        }
        ret[ind++] = code.substring(pre, code.length());
        return ret;
    }
}
