package edu.illinois.cs.iomss.Main;

import java.io.FileNotFoundException;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage;
import edu.illinois.cs.iomss.Solvers.DPRLEParser;
import edu.illinois.cs.iomss.Solvers.HampiParser;
import edu.illinois.cs.iomss.Solvers.Parser;

public class MainWrapper {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        if (args.length != 1) {
            System.out.println("Need only one argument as an input file");
        } else {
            try {
                MainLanguage input = new MainLanguage(args[0]);

                // Generate Hampi constraints
                Parser hampi = new HampiParser(input);
                hampi.parse();
                hampi.outputToFile("hampi_input.hmp");

                // Generate DPRLE constraints
                Parser dprle = new DPRLEParser(input);
                dprle.parse();
                dprle.outputToFile("dprle_input");

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
