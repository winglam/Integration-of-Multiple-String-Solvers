package edu.illinois.cs.iomss.Solvers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage;

public abstract class Parser {
	
	protected MainLanguage constraints;
	protected List<String> result;
	
	public Parser(MainLanguage constraints) {
		this.constraints = constraints;
	}
	
	public void parse() throws Exception {
		System.out.println("----------------------------------------------");
	}
	
	public void outputToFile(String filename) throws FileNotFoundException {
		System.out.println("----------------------------------------------");
		System.out.println("Generating: " + filename);
		PrintWriter outfile = new PrintWriter(filename);
		for (String line : result) {
			outfile.println(line);
		}
		outfile.close();
		System.out.println("Done generating: " + filename);
	}
}
