package edu.illinois.cs.iomss.Main;

public class MainWrapper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
       	System.out.println("java.library.path = " + System.getProperty("java.library.path"));
       	System.setProperty("java.library.path", System.getProperty("java.library.path") + "blahh");
       	System.out.println("java.library.path = " + System.getProperty("java.library.path"));
	}

}
