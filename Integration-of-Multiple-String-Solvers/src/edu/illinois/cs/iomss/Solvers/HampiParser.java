package edu.illinois.cs.iomss.Solvers;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.Condition;
import edu.illinois.cs.iomss.MainLanguage.MainLanguage;

public class HampiParser extends Parser {

    private String solveFor; // variable we want to solve;

    public HampiParser(MainLanguage conditions) {
        super(conditions);
    }

    @Override
    public void parse() throws Exception {
        super.parse();
        System.out.println("Parsing: HAMPI");
        List<Condition> conditions = this.constraints.getConditions();
        result = new ArrayList<String>();
        for (Condition cond : conditions) {
            String temp = conditionToString(cond);
            if (temp != "")
                result.add(temp + ";");
        }
    }

    private String conditionToString(Condition cond) throws Exception {
        switch (cond.function) {
        case SolveFor:
            solveFor = cond.parameters.get(0);
            return "";
        case Reg:
            return "reg " + cond.parameters.get(0) + " := " + cond.parameters.get(1);
        case AssertIn:
            return "assert " + cond.parameters.get(0) + " in " + cond.parameters.get(1);
        case Length:
            String var = cond.parameters.get(0);
            if (!var.equals(solveFor)) {
                throw new Exception();
            }
            return "var " + solveFor + " : " + cond.parameters.get(1);
        case Int:
            System.out.println("Warning: Hampi can't handle int function");
            return "";
        case String:
            System.out.println("Warning: Hampi can't handle string function");
            return "";
        default:
            throw new Exception("Unknown function in Hampi");
        }
    }
}
