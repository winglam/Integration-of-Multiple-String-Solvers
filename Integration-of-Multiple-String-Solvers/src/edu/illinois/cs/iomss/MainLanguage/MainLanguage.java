package edu.illinois.cs.iomss.MainLanguage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MainLanguage {

    public enum Function {
        SolveFor,
        Reg,
        AssertIn,
        Length,
        Int,
        String;
    }

    private List<Condition> conditions;

    public MainLanguage(String inputFile) throws Exception {
        Scanner cin = new Scanner(new File(inputFile));
        conditions = new ArrayList<Condition>();
        while (cin.hasNextLine()) {
            String line = cin.nextLine();
            line.trim();
            if (line.startsWith("//") || line.length() == 0)
                continue;
            String[] cond = line.split(";");
            for (String subcond : cond) {
                subcond.trim();
                conditions.add(new Condition(subcond));
            }
        }
        cin.close();
    }

    public List<Condition> getConditions() {
        return Collections.unmodifiableList(conditions);
    }
}
