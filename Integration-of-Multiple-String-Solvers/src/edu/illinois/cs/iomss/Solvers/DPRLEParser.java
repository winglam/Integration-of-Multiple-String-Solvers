package edu.illinois.cs.iomss.Solvers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.illinois.cs.iomss.MainLanguage.Condition;
import edu.illinois.cs.iomss.MainLanguage.MainLanguage;
import edu.illinois.cs.iomss.RegDFS.Alt;
import edu.illinois.cs.iomss.RegDFS.Dfa;
import edu.illinois.cs.iomss.RegDFS.MyRegex;
import edu.illinois.cs.iomss.RegDFS.Nfa;
import edu.illinois.cs.iomss.RegDFS.Seq;
import edu.illinois.cs.iomss.RegDFS.Star;
import edu.illinois.cs.iomss.RegDFS.Sym;

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
		switch (cond.function) {
		case "SolveFor":
			solveFor = new ArrayList<>(cond.parameters);
			break;
		case "Length":
			System.out.println("Warning: DPRLE can't handle constraint length condition");
			break;
		case "Reg":
			res = cond.parameters.get(0) + " < " + regToString(cond.parameters.get(1));
			break;
		case "AssertIn":
			res = cond.parameters.get(0) + " < " + cond.parameters.get(1);
			break;
		default:
			throw new Exception("Unknown function in DPRLE");
		}
		return res;
	}

	private String regToString(String str) throws Exception {
		String res = "[" + System.getProperty("line.separator");
		MyRegex regex = buildRegex(str);
		Nfa nfa = regex.mkNfa(new Nfa.NameSource());
		Dfa dfa = nfa.toDfa();
		res += "s: n" + dfa.getStart() + System.getProperty("line.separator");
		res += "f: acc_state" + System.getProperty("line.separator");
		res += "d:" + System.getProperty("line.separator");
		for (int end : dfa.getAccept()) {
			res += "n" + end + " -> acc_state on epsilon" + System.getProperty("line.separator");
		}
		
		// The transitions
		Map<Integer, Map<String, Integer>> transitions = dfa.getTrans();
		for (Integer s1 : transitions.keySet()) {
			Map<String, Integer> s1Trans = transitions.get(s1);
			for (String lab : s1Trans.keySet()) {
				Integer s2 = s1Trans.get(lab);
				lab = lab.replace('"', '\'');
				res += "n" + s1 + " -> n" + s2 + " on {" + lab + "}" + System.getProperty("line.separator");
			}
		}
		res += "]" + System.getProperty("line.separator");
		return res;
	}
	
	private MyRegex buildRegex(String str) throws Exception {
		str = str.trim();
		if (str.startsWith("\"")) { // string literal
			return new Sym(str);
		}
		int numArgument = 1;
		int lvl = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '(')
				lvl++;
			else if (c == ')')
				lvl--;
			else if (c == ',') {
				if (lvl == 1)
					numArgument++;
			}
		}
		if (str.startsWith("star")) {
			return new Star(buildRegex(str.substring(str.indexOf("(") + 1, str.length() - 1)));
		}
		if (numArgument == 1) {
			return buildRegex(str.substring(str.indexOf("(") + 1, str.length() - 1));
		}
		if (str.startsWith("concat")) {
			return new Seq(buildRegex(str.substring(str.indexOf("(") + 1, str.indexOf(","))),
					buildRegex("concat(" + str.substring(str.indexOf(",") + 1)));
		} else if (str.startsWith("or")) {
			return new Alt(buildRegex(str.substring(str.indexOf("(") + 1, str.indexOf(","))),
					buildRegex("or(" + str.substring(str.indexOf(",") + 1)));
		} else {
			throw new Exception("Invalid format in DPRLE buildRegex");
		}
	}
}
