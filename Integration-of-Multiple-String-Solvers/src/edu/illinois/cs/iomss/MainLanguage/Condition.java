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
		cond = cond.substring(leftParen + 1, cond.length() - 1);
		int lvl = 0, start = 0, i = 0;
		parameters = new ArrayList<String>();
		for (i = 0; i < cond.length(); i++) {
			char c = cond.charAt(i);
			if (c == '(') lvl++;
			else if (c == ')') lvl--;
			else if (c == ',') {
				if (lvl == 0) {
					parameters.add(cond.substring(start, i).trim());
					start = i + 1;
				}
			}
		}
		parameters.add(cond.substring(start).trim());
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
