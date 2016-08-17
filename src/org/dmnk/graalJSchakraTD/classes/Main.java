package org.dmnk.graalJSchakraTD.classes;

import org.dmnk.graalJSchakraTD.classes.configProvider.CLIConfigProvider;
import org.dmnk.graalJSchakraTD.classes.configProvider.DefaultConfigProvider;
import org.dmnk.graalJSchakraTD.classes.test.GenericTestEvaluator;
import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.exceptions.TestException;
import org.dmnk.graalJSchakraTD.interfaces.ConfigurationProvider;
import org.dmnk.graalJSchakraTD.interfaces.ListFetcher;
import org.dmnk.graalJSchakraTD.interfaces.TestDriver;
import org.dmnk.graalJSchakraTD.interfaces.TestEvaluator;
import org.dmnk.graalJSchakraTD.interfaces.TestFetcher;

public class Main {

	public static void main (String[] args) {
		
		try {
			Configuration c = setupConfig(args);
			TestFetcher tf = new TestFetcherGeneric(c.getTestsPath());
			ListFetcher lf = new ListFetcherGeneric(c);
			
			TestEvaluator tEval = new GenericTestEvaluator();
	
			//easy access to different implementations:
			int impl = 2;
			TestDriver td;
			switch (impl) {
			case 1:
				//using futures, works for hanging tests like the /Array/array_init.js
				//but [i guess] because of java bug 4770092 the system process itself stays alive
				td = new TestDriverFutures(c, tf, lf, tEval);
				break;
			case 2:
				//the callback gives a nicer user feedback, as one hung test doesn't hang the UI till timeout
				//just at the border between testgroups, the driver waits till all tests in the subgroup finish / timeout
				td = new TestDriverCallback(c, tf, lf, tEval);
				break;
			default:
				//sequential execution
				td = new TestDriverGeneric(c, tf, lf, tEval);
			}
			
			try {
				td.process();
				td.export();
				System.exit(0);
			} catch (TestException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				System.exit(-1);
			}
		} catch (ConfigurationException e){
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(-2);
		}
	}
	
	private static Configuration setupConfig(String [] args) throws ConfigurationException {
		Configuration conf = new Configuration(); 
		
		ConfigurationProvider defConf, cliConf;
		
		//change the class or create a new config provider for your specific setup
		defConf = new DefaultConfigProvider();
		defConf.workOn(conf);
		conf = defConf.getConfig();
		
		// CLIprovider appends/replaces settings
		cliConf = new CLIConfigProvider(args);
		cliConf.workOn(conf);
		conf = cliConf.getConfig();
		
		if(!conf.readyToExec()) {
			throw new ConfigurationException("Configuration is missing essential part(s)!");
		}
		
		return conf;
	}
}
