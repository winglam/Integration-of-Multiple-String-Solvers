package edu.illinois.cs.iomss.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MainWrapper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
       	if (args.length != 1) {
       		System.out.println("Need only one argument as an input file");
       	} else {
       		try {
				Scanner cin = new Scanner(new File(args[0]));
				List<String> lines = new ArrayList<String>();
				while (cin.hasNextLine()) {
					lines.add(cin.nextLine());
				}
				
				cin.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       	}
	}
	
	public static void outputHAMPI(List<String> lines) throws FileNotFoundException {
		String filename = "hampi_input.hmp";
		PrintWriter out = new PrintWriter(new File(filename));
		for (String line : lines) {
			out.println(line);
		}
		System.out.println("Done generating an input for HAMPI");
	}
	
	

}
