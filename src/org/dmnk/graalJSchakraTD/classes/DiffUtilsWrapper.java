package org.dmnk.graalJSchakraTD.classes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import org.dmnk.graalJSchakraTD.interfaces.Test;

import difflib.DiffUtils;
import difflib.Patch;

public class DiffUtilsWrapper {
	
	public static String getDiff(Test t, TestOutput to) {
		try {
			List<String> expected =  Files.readAllLines(new File(t.getBaseline()).toPath(), StandardCharsets.UTF_8);
			
			List<String> actual = new LinkedList<String>();
			String[] testOutput = to.getStdOut().split("\\n");
			for(String line : testOutput) {
				actual.add(line);
			}
			return getDiff(expected, actual);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "FAIL at opening File " + t.getBaseline();
		}
	}
	
	public static String getDiff(String s, File f) throws IOException {
		List<String> sList, fList;
		fList = Helper.fileToStringList(new File(f.getAbsolutePath()));
		sList = new LinkedList<String>();
		sList.add(s);
		
		return getDiff(sList, fList);
	}
	
	public static String getDiff(String s1, String s2) {
		List<String> ls1, ls2;
		ls1 = new LinkedList<String>();
		ls1.add(s1);
		ls2 = new LinkedList<String>();
		ls2.add(s2);
		
		return getDiff(ls1, ls2);
	}
		
	public String getDiff(File f1, File f2) throws IOException {
		List<String> sList, fList;
		sList = Helper.fileToStringList(f1);
		fList = Helper.fileToStringList(f2);
		
		return getDiff(sList, fList);
	}
	
	public static String getDiff(List<String> expected, List<String> current) {
		Patch p = DiffUtils.diff(expected, current);
		List<String> uPatch = DiffUtils.generateUnifiedDiff("expected baseline", "computed output", expected, p, 1);
		StringBuilder sb = new StringBuilder();
		for(String s : uPatch) {
			sb.append(s+"\n");
		}
		
		return sb.toString();
	}
}
