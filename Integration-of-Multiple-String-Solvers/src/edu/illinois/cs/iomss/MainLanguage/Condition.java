package edu.illinois.cs.iomss.MainLanguage;

import java.util.ArrayList;
import java.util.List;

public class Condition {
	public String function;
	public List<String> parameters;
	
	public Condition(String cond) throws Exception {
		int leftParen = cond.indexOf("(");
		if (leftParen == -1 || leftParen == 0 || !cond.endsWith(")")) {
			throw new Exception("Invalid condition: " + cond);
		}
		function = cond.substring(0, leftParen);
		String[] temp = cond.substring(leftParen + 1, cond.length() - 1).split(",");
		parameters = new ArrayList<String>();
		for (String s : temp) {
			parameters.add(s.trim());
		}
	}
	
	@Override
	public String toString() {
		String res = function + "(";
		boolean first = true;
		for (String s : parameters) {
			if (!first) res += ",";
			res += s;
			first = false;
		}
		res += ");";
		return res;
	}
}
