package org.dmnk.graalJSchakraTD.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DiffUtilsWrapper {
	// Helper method for get the file content
	private List<String> fileToLines(String filename) {
	    List<String> lines = new LinkedList<String>();
	    String line = "";
	    try {
	        BufferedReader in = new BufferedReader(new FileReader(filename));
	        while ((line = in.readLine()) != null) {
	            lines.add(line);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return lines;
	}
	
	public String getDiff(String s, File f) {
		List<String> sList, fList;
		fList = fileToLines(f.getAbsolutePath());
		sList = new LinkedList<String>();
		sList.add(s);
		
		
		//diff = DiffUtils.diff("Passed", to.get
				//TODO: import https://code.google.com/archive/p/java-diff-utils/
		
		return "";
	}
		
	public String getDiff(File f1, File f2) {
		
		return "";
	}
	
	public String getDiff(String s1, String s2) {
		
		return "";
	}
}
