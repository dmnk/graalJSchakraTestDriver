package org.dmnk.graalJSchakraTD.classes;

import java.util.HashMap;

public class Configuration {
	
	private String graalJSexec;
	private ExecutableMode graalJSexecMode;
	
	private String chakraTestsPath;
	/**
	 * 1st string = type of export
	 * 2nd string = place to put the file
	 */
	private HashMap<String, String> exports;
	
	private String harnessFile;
	private HarnessMode harnessMode;
	
	private ListMode listMode;
	private String list;
	
	private String graylist;
	
	public Configuration() {
		exports = new HashMap<String,String>();
	}
	
/* WHITE / GRAY / BLACKLISTS */
	public void setWhiteList(String file) {
		listMode =  ListMode.WHITE;
		list = file;
	}
	
	public void setBlackList(String file) {
		listMode = ListMode.BLACK;
		list = file;
	}
	
	public void setGrayList(String file) {
		graylist = file;
	}
	
	public ListMode getListMode() {
		return listMode;
	}
	
	public String getWhiteList() throws Exception {
		if(listMode == ListMode.WHITE) {
			return list;
		} else {
			throw new Exception ("Tried to fetch whitelist, mode is " + listMode.toString());
		}
	}
	
	public String getBlackList() throws Exception {
		if(listMode == ListMode.BLACK) {
			return list;
		} else {
			throw new Exception ("Tried to fetch blacklist, mode is " + listMode.toString());
		}
	}
	
	public String getGrayList() {
		return graylist;
	}
	
/* RESULT EXPORTS */
	public void addExport(String type, String path) {
		exports.put(type, path);
	}
	
	public boolean checkExport(String type) {
		return exports.containsKey(type);
	}
	
	public String getExport (String type) {
		return exports.get(type);
	}
	
/* GRAAL JS EXE */
	public void setGraalJSexec(ExecutableMode mode, String exec) {
		graalJSexecMode = mode;
		graalJSexec = exec;
	}
	
	public String getGraalJSexec() {
		return graalJSexec;
	}
	
	public ExecutableMode getGraalJSExecMode() {
		return graalJSexecMode;
	}
	
/*  CHAKRA TESTS PATH */
	public void setChakraTestsPath(String path) {
		chakraTestsPath = path;
	}
	
	public String getChakraTestsPath() {
		return chakraTestsPath;
	}
	
/* HARNESS SETUP */
	public void setHarness(HarnessMode mode, String file) {
		harnessMode = mode;
		harnessFile = file;
	}
	
	public HarnessMode getHarnessMode() {
		return harnessMode;
	}
	
	public String getHarnessFile() {
		return harnessFile;
	}
	
/* ENUM DECLARATIONS */
	public static enum ExecutableMode {
		DIRECT, INDIRECT;
	}
	
	public static enum HarnessMode {
		INTERNAL, INCLUDE, PARAMETER;
	}
	
	public static enum ListMode {
		WHITE, BLACK;
	}
}
