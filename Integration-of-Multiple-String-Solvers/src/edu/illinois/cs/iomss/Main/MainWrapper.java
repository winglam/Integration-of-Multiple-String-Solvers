package edu.illinois.cs.iomss.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.illinois.cs.iomss.MainLanguage.MainLanguage;
import edu.illinois.cs.iomss.Solvers.DPRLEParser;
import edu.illinois.cs.iomss.Solvers.Parser;
import edu.illinois.cs.iomss.util.Command;
import edu.illinois.cs.iomss.util.Globals;

public class MainWrapper {

    public static void main(String[] args) throws Exception {
        // System.out.println(System.getProperties().getProperty("java.class.path",
        // null));
        File binDir = new File(System.getProperties().getProperty("java.class.path", null));
        // System.out.println(binDir.getAbsolutePath());
        List<String> parsedPathArr = Arrays.asList(binDir.getAbsolutePath().split(Globals.fileSep));
        int outerDirIndex = parsedPathArr.indexOf("Integration-of-Multiple-String-Solvers");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= outerDirIndex; i++) {
            sb.append(parsedPathArr.get(i) + Globals.fileSep);
        }
        if (args.length < 1) {
            System.out.println("Need only one argument as an input file");
        } else {
            try {
                MainLanguage input = new MainLanguage(args[0]);
                System.out.println(input);
                boolean parseOnly = (args.length > 1 && args[1].equals("-p"));
                //
                // // Generate HAMPI constraints
                // Parser hampi = new HAMPIParser(input);
                // try {
                // hampi.parse();
                // hampi.outputToFile(args[0] + ".hampi");
                // } catch (Exception e) {
                // System.out.println("Error in generating HAMPI");
                // e.printStackTrace();
                // }
                //
                // if (!parseOnly) {
                // // Executing HAMPI
                // String hampiPath = sb.toString() + "hampi" + Globals.fileSep;
                // System.out.println("Executing: HAMPI\n");
                // List<String> commandList = new LinkedList<String>();
                // commandList.add("java");
                // commandList.add("-cp");
                // commandList.add(hampiPath + "bin" + Globals.pathSep +
                // hampiPath + "lib/*");
                // commandList.add("hampi.Hampi");
                // commandList.add(args[0] + ".hampi");
                // String[] envp = new String[1];
                // envp[0] = "LD_LIBRARY_PATH=" + hampiPath + "lib";
                // executeCommand(commandList, envp);
                // }
                // System.out.println("----------------------------------------------\n");
                //
                // Generate DPRLE constraints
                Parser dprle = new DPRLEParser();
                try {
                    dprle.parse(input);
                    dprle.outputToFile(args[0] + ".dprle");
                } catch (Exception e) {
                    System.out.println("Error in generating DPRLE");
                    e.printStackTrace();
                }

                if (!parseOnly) {
                    // Executing DPRLE
                    System.out.println("----------------------------------------------");
                    System.out.println("Executing: DPRLE\n");
                    List<String> commandList = new LinkedList<String>();
                    commandList.add(sb.toString() + "dprle" + Globals.fileSep + "bin" + Globals.fileSep + "dprle");
                    commandList.add(args[0] + ".dprle");
                    executeCommand(commandList);
                }
                System.out.println("----------------------------------------------\n");
                //
                // // Generate Z3str constraints
                // Parser z3str = new Z3strParser(input);
                // try {
                // z3str.parse();
                // z3str.outputToFile(args[0] + ".z3str");
                // } catch (Exception e) {
                // System.out.println("Error in generating Z3str");
                // e.printStackTrace();
                // }
                //
                // if (!parseOnly) {
                // // Executing Z3str
                // System.out.println("----------------------------------------------");
                // System.out.println("Executing: Z3-str\n");
                // List<String> commandList = new LinkedList<String>();
                // commandList.add(sb.toString() + "Z3-str" + Globals.fileSep +
                // "Z3-str.py");
                // commandList.add("-f");
                // commandList.add(args[0] + ".z3str");
                // executeCommand(commandList);
                //
                // String[] outputFileArr = args[0].split(Globals.fileSep);
                // commandList.clear();
                // commandList.add(sb.toString() + "str");
                // commandList.add("-f");
                // commandList.add(sb.toString() + "tmp" + Globals.fileSep +
                // "z3_str_convert" + Globals.fileSep
                // + outputFileArr[outputFileArr.length - 1] + ".z3str");
                // executeCommand(commandList);
                // }
                // System.out.println("----------------------------------------------\n");
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static void executeCommand(List<String> commandList) {
        executeCommand(commandList, null);
    }

    private static void executeCommand(List<String> commandList, String[] envp) {
        String[] cmdArgs = commandList.toArray(new String[0]);
        System.out.println("Output:");
        long startTime = System.nanoTime();
        Command.exec(cmdArgs, envp);
        long endTime = System.nanoTime();
        double runningTime = (endTime - startTime) * 1e-9;

        System.out.println();
        System.out.println("Command ran: " + commandList);
        System.out.printf("Time taken: %.9f sec", runningTime);
        System.out.println();
    }
}
