package org.dmnk.graalJSchakraTD.classes;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;


/**
 * the central configuration. every setting the other classes
 * rely on, should be controlled through this class.
 * 
 * @author dominik
 *
 */
public class Configuration {
	
	private String executable;
	private ExecutableMode execMode;
	
	/**
	 * signals the environment that during reading
	 * the config, something came up, or was missed
	 * which wasn't a exception. (like called just the help)
	 */
	private Boolean readyToExec;
	
	private String testsPath;
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
	
	private int verbosity;
	
	private int maxThreads;
	
	private int timeoutValue;
	private TimeUnit timeoutUnit;
	
	public Configuration() {
		exports = new HashMap<String,String>();
		readyToExec = true;
		verbosity = 0;
	}
	
	public void notReadyToExec() {
		readyToExec = false;
	}
	
	public Boolean readyToExec() {
		return readyToExec;
	}
	
	public void setVerbosity (int level) {
		verbosity = level;
	}
	
	public int getVerbosity () {
		return verbosity;
	}
	
	public void setMaxThreads(int threads) {
		maxThreads = threads;
	}
	
	public int getMaxThreads() {
		return maxThreads;
	}
	
	public void setTimeoutValue(int value) {
		timeoutValue = value;
	}
	
	public int getTimeoutValue() {
		return timeoutValue;
	}
	
	public void setTimeoutUnit(TimeUnit tu) {
		timeoutUnit = tu;
	}
	
	public TimeUnit getTimeoutUnit() {
		return timeoutUnit;
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
	
	public String getWhiteList() throws ConfigurationException {
		if(listMode == ListMode.WHITE) {
			return list;
		} else {
			throw new ConfigurationException("Tried to set whitelist, mode is " + listMode.toString());
		}
	}
	
	public String getBlackList() throws ConfigurationException {
		if(listMode == ListMode.BLACK) {
			return list;
		} else {
			throw new ConfigurationException ("Tried to set blacklist, mode is " + listMode.toString());
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
	
/* Executable under test command */
	public void setExec(ExecutableMode mode, String exec) {
		execMode = mode;
		executable = exec;
	}
	
	public String getExec() {
		return executable;
	}
	
	public ExecutableMode getExecMode() {
		return execMode;
	}
	
/*  TESTS PATH */
	public void setTestsPath(String path) {
		testsPath = path;
	}
	
	public String getTestsPath() {
		return testsPath;
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
		INCLUDE, PARAMETER;
	}
	
	public static enum ListMode {
		WHITE, BLACK;
	}
}
