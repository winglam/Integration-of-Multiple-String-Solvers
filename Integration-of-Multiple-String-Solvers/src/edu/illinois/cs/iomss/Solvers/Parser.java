package edu.illinois.cs.iomss.Solvers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage;

public abstract class Parser {

    public static String newLine = System.lineSeparator();

    protected List<String> result;
    protected Map<String, String> values; // value of each variable

    public Parser() {
        this.values = new HashMap<String, String>();
    }

    public void parse(MainLanguage input) throws Exception {
        System.out.println("----------------------------------------------");
    }

    public void outputToFile(String filename) throws FileNotFoundException {
        System.out.println("----------------------------------------------");
        System.out.println("Generating: " + filename);
        PrintWriter outfile = new PrintWriter(filename);
        for (String line : result) {
            System.out.println(line);
            outfile.println(line);
        }
        outfile.close();
        System.out.println("Done generating: " + filename);
    }
}
