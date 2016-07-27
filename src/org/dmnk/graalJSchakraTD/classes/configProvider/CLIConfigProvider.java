package org.dmnk.graalJSchakraTD.classes.configProvider;

import java.util.HashMap;

import org.dmnk.graalJSchakraTD.classes.Configuration;
import org.dmnk.graalJSchakraTD.classes.Configuration.ExecutableMode;
import org.dmnk.graalJSchakraTD.classes.Configuration.HarnessMode;
import org.dmnk.graalJSchakraTD.interfaces.ConfigurationProviderInterface;

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
public class CLIConfigProvider implements ConfigurationProviderInterface {
	
	private String args[];
	
	private Configuration c;

	private HashMap<String, String> tempVals;
	
	public CLIConfigProvider(String args[]) {
		this.args = args;
		tempVals = new HashMap<String,String>();
		c = new Configuration();
	}
	
//	public CLIConfigProvider(String args[]) {
//		this(args);
//		c = new Configuration;
//	}
	
	@Override
	public Configuration getConfig() throws Exception {
		this.process(args);
		return c;
	}
	
	private void process(String[] args) throws Exception {
		
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			
			String params[] = arg.split("=");
			
			switch (params[0]) {
			case "-exec":
				handleExec("exec", params[1]);
				break;
			case "-execMode":
				handleExec("execMode", params[1]);
				break;
			case "-exp":
				handleExport(params[1]);
				break;
			case "-g":
				handleGraylist(params[1]);
				break;
			case "-hnsMode":
				handleHarness("mode", params[1]);
				break;
			case "-hnsFile":
				handleHarness("file", params[1]);
				break;
			case "-l":
			case "-list":
				handleBWList("list", params[1]);
				break;
			case "-m":
			case "-mode":
				handleBWList("mode", params[1]);
				break;
			case "-t":
				handleTests(params[1]);
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
				c.notReadyToExec();
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
	
	private void handleTests(String tests) {
		c.setTestsPath(tests);
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	private void handleHarness(String item, String value) throws Exception {
		switch (item) {
		case "mode":
			handleHarnessMode(value);
			break;
		case "file":
			handleHarnessFile(value);
			break;
		}
	}
	
	private void handleHarnessMode(String value) throws Exception {
		if (!(value.equals("i") || value.equals("p"))) {
//			throw new ConfigProviderException(2, "either b or w for the list-mode! ["+value+"]");
		}
		// the list is already present, can add the values to config
		if(tempVals.containsKey("HNfile")) {
			String exec = tempVals.get("HNfile");
			tempVals.remove("HNfile");
			
			insertHarness(value, exec);
		} else {
			//store the mode in the temp storage
			tempVals.put("HNmode", value);
		}
	}
	
	private void handleHarnessFile(String value) throws Exception {
		// the mode is already present, can add the values to config
		if(tempVals.containsKey("HNmode")) {
			String m = tempVals.get("HNmode");
			tempVals.remove("HNmode");
			
			insertHarness(m, value);
		} else {
			//store the mode in the temp storage
			tempVals.put("HNfile", value);
		}
	}
	
	private void insertHarness(String m, String file) throws Exception {
		HarnessMode hm;
		
		switch (m) {
		case "i":
			hm = HarnessMode.INCLUDE;
			break;
		case "p":
			hm = HarnessMode.PARAMETER;
			break;
			default:
				throw new Exception ("unknown executable mode: "+m);
		}
		
		//TODO: internal???, gonna resurrect the 3rd enum for that
		c.setHarness(hm, file);
	}
	
	
	/**
	 * @throws Exception 
	 * 
	 */
	private void handleExec(String item, String value) throws Exception {
		switch (item) {
		case "execMode":
			handleExecModeEntry(item, value);
			break;
		case "exec":
			handleExecCommandEntry(value);
			break;
		}
	}
	
	private void handleExecModeEntry(String item, String value) throws Exception {
		if (!(value.equals("b") || value.equals("w"))) {
//			throw new ConfigProviderException(2, "either b or w for the list-mode! ["+value+"]");
		}
		// the list is already present, can add the values to config
		if(tempVals.containsKey("EMexec")) {
			String exec = tempVals.get("EMexec");
			tempVals.remove("EMexec");
			
			insertExecMode(value, exec);
		} else {
			//store the mode in the temp storage
			tempVals.put("EMmode", value);
		}
	}
	
	private void handleExecCommandEntry(String value) throws Exception {
		// the mode is already present, can add the values to config
		if(tempVals.containsKey("EMmode")) {
			String m = tempVals.get("EMmode");
			tempVals.remove("EMmode");
			
			insertExecMode(m, value);
		} else {
			//store the mode in the temp storage
			tempVals.put("EMexec", value);
		}
	}
	
	private void insertExecMode(String m, String exec) throws Exception {
		ExecutableMode em;
		
		switch (m) {
		case "d":
			em = ExecutableMode.DIRECT;
			break;
		case "i":
			em = ExecutableMode.INDIRECT;
			break;
			default:
				throw new Exception ("unknown executable mode: "+m);
		}
		
		c.setGraalJSexec(em, exec);
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
	
	private void handleGraylist(String list) {
		c.setGrayList(list);
	}
	
	private void handleExport(String export) throws Exception {
		String args[] = export.split("#");
		if(args.length != 2) {
			throw new Exception("Export parameter has to consist of two parts, the type and the export path, split by an #!");	
		}
		
		c.addExport(args[0], args[1]);
	}

	private void printHelp() {
		System.out.println("-- usage: --");
		
		System.out.println("-execMode=[d/i]");
		System.out.println("\t Call the graal executable by path, or global (indirect)");
		
		System.out.println("-exec=name/path");
		System.out.println("\t the path to the tested executable");
		
		System.out.println("-exp=type#location");
		System.out.println("\t where & which type of result export should be stored");
		System.out.println("\t available types: csv, html");

		
		System.out.println("-g=GreyListFile.txt");
		System.out.println("\t same format as the black/white-lists");
		System.out.println("\t tests will be shown in the summary but not executed - for known to fail tests");
		
		System.out.println("-hnsMode=[i/p] for harness file mode, uses file from parameter -hnsFile (or pre-delivered one if not set)");
		System.out.println("\t p: provide it chakra as parameter");
		System.out.println("\t i: inject it into the code of the test");
		
		System.out.println("-hnsFile=HarnessFile.js");
		System.out.println("\t custom harness file, otherwise pre-delivered one will be used");
		
		System.out.println("-l=blackOrWhitelistFile.txt");
		System.out.println("\t the Files structure has to be:");
		System.out.println("\t\t for File-Entries: \\folder\\testname");
		System.out.println("\t\t for Directories: \\folder\\");
		
		System.out.println("-mode=[b/w]");
		System.out.println("\t Black or Whitelist Mode, an additional parameter with the file has to be set!");
		System.out.println("\t just the tests included in the whitelist; excluded by the blacklist will executed");
		
		System.out.println("-t=directory");
		System.out.println("\t Path to the directory where the tests are stored");
		
	}

	@Override
	public void workOn(Configuration conf) {
		c = conf;		
	}
}
