package org.dmnk.graalJSchakraTD.classes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

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
	
	public static boolean simpleBaselineCompare(File baselineFile, String output) throws IOException {
		return output.equals(fileToString(baselineFile));
	}
	
	private static String fileToString(File filename) throws IOException {
		List<String> fileContent = fileToStringList(filename);
		StringBuilder sb = new StringBuilder();
		
		for(String s : fileContent) {
			sb.append(s);
			sb.append('\n');
		}
		
		sb.deleteCharAt(sb.length()-1);
		
		return sb.toString();
	}
	
	public static List<String> fileToStringList(File filename) throws IOException {
		List<String> fileContent;
		fileContent = Files.readAllLines(filename.toPath(), StandardCharsets.UTF_8);
		return fileContent;
	}
	
	public static String removeTrailingNewline(String old) {
		StringBuilder n = new StringBuilder(old);
		if(n.length() > 0 && n.charAt(n.length()-1) == '\n') {
			n.deleteCharAt(n.length()-1);
		}
		
		return n.toString().trim();
	}
	
	
}
