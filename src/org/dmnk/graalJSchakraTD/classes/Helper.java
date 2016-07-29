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

public class Helper {
	/**
	 * returns -1 if no Configuration isn't set
	 * @param c
	 * @return
	 */
	public static int getVerbosity(Configuration c) {
		if(c == null) {
			return -1;
		} else {
			return c.getVerbosity();
		}
	}
	
	public static void debugOut(Configuration c, int reqLevel, String area, String message) {
		if(getVerbosity(c) > 0) {
			int vb = getVerbosity(c);
			if(vb >= reqLevel) {
				System.out.println("DEBUG from ["+area+"]: "+message);
			}
		}
	}
	
	public static boolean simpleBaselineCompare(File baselineFile, String output) {
		return output.equals(fileToString(baselineFile));
	}
	
	private static String fileToString(File filename) {
		List<String> fileContent;
		try {
			fileContent = Files.readAllLines(filename.toPath(), StandardCharsets.UTF_8);
			StringBuilder sb = new StringBuilder();
			for(String s : fileContent) {
				sb.append(s);
				sb.append('\n');
			}
			sb.deleteCharAt(sb.length()-1);
			
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "FAIL at opening File " + filename.getName();
		}
		
	}
	
	public static String getDiff(Test t, TestOutput to) {
		try {
			List<String> expected =  Files.readAllLines(new File(t.getBaseline()).toPath(), StandardCharsets.UTF_8);
			
			List<String> actual = new LinkedList<String>();
			String[] testOutput = to.getStdOut().split("\\n");
			for(String line : testOutput) {
				actual.add(line);
			}
			
			Patch p = DiffUtils.diff(expected, actual);
			List<String> uPatch = DiffUtils.generateUnifiedDiff("expected baseline", "computed output", expected, p, 1);
			StringBuilder sb = new StringBuilder();
			for(String s : uPatch) {
				sb.append(s+"\n");
			}
			
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "FAIL at opening File " + t.getBaseline();
		}
	}
}
