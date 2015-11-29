package edu.illinois.cs.iomss.Solvers;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.Condition;
import edu.illinois.cs.iomss.MainLanguage.MainLanguage;

public class DPRLEParser extends Parser {

	private List<String> solveFor; // variable we want to solve;
	
	public DPRLEParser(MainLanguage conditions) {
		super(conditions);
	}

	@Override
	public void parse() throws Exception {
		super.parse();
		System.out.println("Parsing: DPRLE");
		List<Condition> conditions = this.constraints.getConditions();
		result = new ArrayList<String>();
		for (Condition cond : conditions) {
			String temp = conditionToString(cond);
			if (temp != "")
				result.add(temp + ";");
		}
		
		result.add("solve();");
		for (String var : solveFor) {
			result.add("strings(" + var + ");");
		}
	}

	private String conditionToString(Condition cond) throws Exception {
		String res = "";
		switch(cond.function) {
			case "SolveFor":
				solveFor = new ArrayList<>(cond.parameters);
				break;
			case "Length":
				System.out.println("Warning: DPRLE can't handle constraint length condition");
				break;
			case "Reg":
				res = cond.parameters.get(0) + " < ";
				break;
			case "AssertIn":
				res = cond.parameters.get(0) + " < " + cond.parameters.get(1);
				break;
			default:
				throw new Exception("Unknown function in DPRLE");
		}
		return res;
	}
}
