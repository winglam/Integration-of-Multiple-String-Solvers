package edu.illinois.cs.iomss.Solvers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage;

public abstract class Parser {

    public static String newLine = System.lineSeparator();

    protected MainLanguage constraints;
    protected List<String> result;
    protected Map<String, String> values; // value of each variable

    public Parser(MainLanguage constraints) {
        this.values = new HashMap<String, String>();
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
