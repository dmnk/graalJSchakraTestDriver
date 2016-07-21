package org.dmnk.graalJSchakraTD.classes.configProvider;

import org.dmnk.graalJSchakraTD.classes.Configuration;

public class CLIConfigProvider {
	private String blacklistFile;
	private int modeBlackWhite = 0;
	private String whitelistFile;
	private String executable = "jjs";
	
	private String args[];
	
	public CLIConfigProvider(String args[]) {
		this.args = args;
	}
	
	public Configuration getConfig() {
		Configuration c = new Configuration();
		return c;
	}
	
	public void fetch(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			
			switch (arg.toLowerCase().split("=")[0]) {
			case "mode":
//				handleMode();
				break;
			case "exec":
//				handleExec(arg);
				break;
			case "h":
			case "help":
			case "?":
				printHelp();
				break;
			}
		}
		
		checkAsserts();
	}
	
	private void checkAsserts() /*throws ConfigProviderException*/ {
		if(modeBlackWhite > 0 && (blacklistFile == null && whitelistFile == null)) {
//			throw new ConfigProviderException("mode set to black/whitelist, but nothing provided");
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
	}
}
