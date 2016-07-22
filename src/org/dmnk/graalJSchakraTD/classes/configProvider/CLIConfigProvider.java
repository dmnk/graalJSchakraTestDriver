package org.dmnk.graalJSchakraTD.classes.configProvider;

import java.util.HashMap;

import org.dmnk.graalJSchakraTD.classes.Configuration;

/**
 * parses the arguments provided at the CLI and returns a Configuration object  
 * <br><br>
 * of course it would be way better if the config would consist of
 * plugins, where each of those would register it's own help message
 * and handle, so that the help and progress would be split.
 * <br>
 * to match that, even the cli handling should be more sophisticated like:
 * @see http://commons.apache.org/cli/ 
 */
public class CLIConfigProvider {
	
	private String args[];
	
	private Configuration c;

	private HashMap<String, String> tempVals;
	
	public CLIConfigProvider(String args[]) {
		this.args = args;
		tempVals = new HashMap<String,String>();
		c = new Configuration();
	}
	
	public CLIConfigProvider(String args[], Configuration conf) {
		this(args);
		c = conf;
	}
	
	public Configuration getConfig() {
		this.process(args);
		return c;
	}
	
	private void process(String[] args) {
		
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			
			String params[] = arg.split("=");
			
			switch (params[0]) {
			case "-m":
			case "-mode":
				handleBWList("mode", params[1]);
				break;
			case "-l":
			case "-list":
				handleBWList("list", params[1]);
				break;
			case "-exec":
//				handleExec(arg);
				break;
			case "-v":
			case "-vv":
			case "-vvv":
//				setVerbosity(arg);
				break;
			case "-h":
			case "--help":
			case "-?":
				printHelp();
				break;
			default:
				StringBuilder error = new StringBuilder();
				error.append("The argument ").append(arg).append(" wasn't understood");
				error.append("\nPlease refer to the help, available via the -h parameter");
				error.append(" for further information about the possible params!");
//				throw new ConfigProviderException(1, error.toString());
				// 0 -> info, 1 -> warning; 2 -> stop
				//TODO: the ConfigProvider Exception
				break;
			}
		}
		
		checkAsserts();
	}
	
	private void checkAsserts() /*throws ConfigProviderException*/ {
//		if(modeBlackWhite > 0 && (blacklistFile == null && whitelistFile == null)) {
//			throw new ConfigProviderException(2, "mode set to black/whitelist, but nothing provided");
//		}
	}
	
	/**
	 * adds the black or whitelist, once the mode is set to the config
	 * uses a temporary storage at the first call
	 * 
	 * @param item = mode or list, depending from where the call comes
	 * @param value = the actual value
	 */
	private void handleBWList(String item, String value) {
		switch (item) {
		case "m":
		case "mode":
			handleBWListModeEntry(item, value);
			break;
		case "list":
			handleBWListListEntry(item, value);
			break;
		default:
//			throw new Exception("in default branch of handleBWList function, who called with "+item + ".."+value+"?");
		}
	}
	
	private void handleBWListModeEntry(String item, String value) {
		if (!(value.equals("b") || value.equals("w"))) {
//			throw new ConfigProviderException(2, "either b or w for the list-mode! ["+value+"]");
		}
		// the list is already present, can add the values to config
		if(tempVals.containsKey("BWlist")) {
			String list = tempVals.get("BWlist");
			tempVals.remove("BWlist");
			insertBWListToConfig(value, list);
		} else {
			//store the mode in the temp storage
			tempVals.put("BWmode", value);
		}
	}
	
	private void handleBWListListEntry(String item, String value) {
		// the mode is already present, can add the values to config
		if(tempVals.containsKey("BWmode")) {
			String mode = tempVals.get("BWmode");
			tempVals.remove("BWmode");
			insertBWListToConfig(mode, value);
		} else {
			//store the mode in the temp storage
			tempVals.put("BWlist", value);
		}
	}
	
	/**
	 * only to be called from handleBWList[List/Mode]Entry function!
	 * necessary precondition checks are in those two functions.
	 * @param mode
	 * @param list
	 */
	private void insertBWListToConfig(String mode, String list) {
		switch(mode) {
		case "b":
			c.setBlackList(list);
			break;
		case "w":
			c.setWhiteList(list);
			break;
		}
	}

	private void printHelp() {
		System.out.println("-- usage: --");
		
		System.out.println("-mode=[b/w]");
		System.out.println("\t Black or Whitelist Mode, an additional parameter with the file has to be set!");
		System.out.println("\t just the tests included in the whitelist; excluded by the blacklist will executed");
		
		System.out.println("-l=blackOrWhitelistFile.txt");
		System.out.println("\t the Files structure has to be:");
		System.out.println("\t\t for File-Entries: \\folder\\testname");
		System.out.println("\t\t for Directories: \\folder\\");
		
		System.out.println("-g=GreyListFile.txt");
		System.out.println("\t same format as the black/white-lists");
		System.out.println("\t tests will be shown in the summary but not executed - for known to fail tests");
		
		System.out.println("-hns=[0/1] for harness file mode, uses file from parameter -hnsFile");
		System.out.println("\t 0: provide it chakra as parameter");
		System.out.println("\t 1: prepend it into the code of the test");
	}
}
