package edu.illinois.cs.iomss.MainLanguage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MainLanguage {

    public enum StatementType {
        BaseType, Concat, Star, Fix, Or, Expression, STR_LIT, INT_LIT, String, Int, Regex, AssertIn, AssertNotIn, ConcatRegex, FixedLength, AssertInRegex, Alias, Push, Pop, Solve, SolveAll, Show, ShowString, IsIn, CharAt, Equal, LessThan, GreaterThan, LessOrEqual, GreaterOrEqual, Substring, StartsWith, EndsWith, Contains, NotContains, ConcatString, Replace, Length, IndexOf, LastIndexOf, ID, Not;
    }

    private List<MStatement> statements;

    public MainLanguage(String inputFile) throws Exception {
        Scanner cin = new Scanner(new File(inputFile));
        statements = new ArrayList<MStatement>();
        String code = "";

        while (cin.hasNextLine()) {
            String line = cin.nextLine();
            int ind = line.indexOf("//");
            if (ind != -1) {
                line = line.substring(0, ind);
            }
            line = line.trim();
            code += line;
        }
        String[] cond = code.split(";");
        for (String subcond : cond) {
            subcond = subcond.trim();
            if (subcond.length() > 0) {
                statements.add(MStatement.parse(subcond));
            }
        }
        cin.close();
    }

    @Override
    public String toString() {
        String ret = "";
        for (MStatement s : statements) {
            ret += s.toString() + System.lineSeparator();
        }
        return ret;
    }

    public List<MStatement> getStatements() {
        return Collections.unmodifiableList(statements);
    }
}
