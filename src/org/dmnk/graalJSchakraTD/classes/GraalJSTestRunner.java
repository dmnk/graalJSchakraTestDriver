package org.dmnk.graalJSchakraTD.classes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dmnk.graalJSchakraTD.classes.Configuration.ListMode;
import org.dmnk.graalJSchakraTD.classes.configProvider.CLIConfigProvider;
import org.dmnk.graalJSchakraTD.classes.configProvider.DefaultConfigProvider;
import org.dmnk.graalJSchakraTD.classes.export.HTMLResultExporter;
import org.dmnk.graalJSchakraTD.classes.export.TextResultExporter;
import org.dmnk.graalJSchakraTD.exceptions.GraalJSTestException;
import org.dmnk.graalJSchakraTD.interfaces.ConfigurationProviderInterface;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
import org.dmnk.graalJSchakraTD.interfaces.ListFetcher;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.TestInitiator;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestExecutedGroup;
import org.dmnk.graalJSchakraTD.interfaces.TestFetcher;

public class GraalJSTestRunner {
	private List<ResultExporter> resExp;
	private List<TestGroup> tests;
	private List<TestExecutedGroup> executedTests;
	private Map<String, HashMap<String, Integer>> crashlist;
	private Map<String, Integer> folderList;
	private TestInitiator testInit;
	private TestFetcher tf;
	private ListFetcher lf;
	private Configuration conf;

	public static void main(String[] args) {
		
		GraalJSTestRunner ctr = new GraalJSTestRunner();
		
		try {
			ctr.setup(args);
			ctr.run();
		} catch (GraalJSTestException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		System.exit(0);
	}
	
	public GraalJSTestRunner() {
		resExp = new LinkedList<ResultExporter>();
		executedTests = new LinkedList<TestExecutedGroup>();
	}
	
	private void setupConfig(String [] args) {
		conf = new Configuration(); //place for default config, CLIprovider should append/replace settings
		ConfigurationProviderInterface defConf, cliConf;
		defConf = new DefaultConfigProvider();
		defConf.workOn(conf);
		try {
			conf = defConf.getConfig();
		} catch (Exception e) {
			//e.printStackTrace(); //def conf can't thow an exception
		}
		
		cliConf = new CLIConfigProvider(args);
		cliConf.workOn(conf);
		
		try {
			conf =cliConf.getConfig();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		if(!conf.readyToExec()) {
			System.exit(0);
		}
	}
	
	private void addResultExporter(ResultExporter re) {
		resExp.add(re);
	}
	
	private void setTestInitiator(TestInitiator ti) {
		testInit = ti;
	}
	
	private void setTestFetcher(TestFetcher tf) {
		this.tf = tf;
	}
	
	private void setListFetcher(ListFetcher lf) {
		this.lf = lf;
	}
	
	private void setup(String[] args) throws GraalJSTestException {
		setupConfig(args);

		TestInitiator ti = new GraalJSTestInitiator(conf.getExec());
		TestFetcher tf = new GraalJSTestFetcher(conf.getTestsPath());
		ListFetcher lf = new GraalJSListFetcher(conf);
					
		setTestInitiator(ti);
		setTestFetcher(tf);
		setListFetcher(lf);
		
		fetchLists();
		fetchTests();
		
		if(conf.checkExport("html")) {
			ResultExporter hre = new HTMLResultExporter(conf.getExport("html"));
			addResultExporter(hre);
		}
		if (conf.checkExport("csv")) {
			ResultExporter tre = new TextResultExporter(conf.getExport("csv"));
//			ctr.addResultExporter(tre);
		}
	}
	
	private void fetchTests() {
		//get available tests from filesystem
		tests = tf.fetch(); //FromDir(this.getChakraPath());
	}
	
	private void fetchLists() {
		//process white/black/crashlists
		crashlist = lf.fetchFileList(conf.getGrayList());
		
		Helper.debugOut(conf, 1, "TR", crashlist.size()+"");
		for(Entry<String, HashMap<String, Integer>> entry : crashlist.entrySet()) {
			Helper.debugOut(conf, 1, "TR-entry", entry.getKey());
		}
		
		try {
			if(conf.getListMode() == ListMode.WHITE) {
				folderList = lf.fetchFolderList(conf.getWhiteList());
			} else {
				folderList = lf.fetchFolderList(conf.getBlackList());
			}
		} catch (Exception e) {
			//...
		}
	}
	

	private void run() {
		//check preconditions (propably in the constructors of TestFetcher, TestRunner, ...)

		//execute the enabled tests
		for(TestGroup tg : tests) {
			if(tg == null) {
				continue;
				//actually, that shouldn't happen
			}
			
			//check if folder is black- / whitelisted
			if(!groupActive(tg)) {
				//back to loop
				continue;
			}
			
			TestExecutedGroup etg = new GraalJSTestExecutedGroup(tg.getGroupName());
			executedTests.add(etg);
			
			for(Test t : tg.getTests()) {
				//check if file is graylisted
				if(fileActive(tg, t)) {
					ExecutedTest et = testInit.runTest(t);
					etg.addTest(et);
					
					if(et instanceof PassedTest) {
						System.out.print(".");
					} else if (et instanceof FailedTest) {
						System.out.print("f");
					} 
				} else {
					//in exclusion list, just add as unexecuted test for completeness
					etg.addTest(t);
					System.out.print(" ");
				}
				
			}
			
 		}
		
		//export the result
		for(ResultExporter te : this.resExp) {
			te.export(this.executedTests);
		}
	}
	
	/**
	 * checks if the folder is ok (not in black/ in whitelist)
	 * 
	 * @param tg
	 * @return
	 */
	private boolean groupActive(TestGroup tg) {
		switch (conf.getListMode()) {
		case WHITE:
			if(folderList.containsKey(tg.getGroupName())) {
				return true;
			} else {
				return false;
			}
//			break;
		case BLACK:
			if(folderList.containsKey(tg.getGroupName())) {
				return false;
			} else {
				return true;
			}
		default: return false;
		}
	}
	
	/**
	 * checks if the folder is ok (not in black/ in whitelist)
	 * and if the test is ok (not graylisted)
	 * @param tg
	 * @param t
	 * @return should the test be executed
	 */
	private boolean fileActive(TestGroup tg, Test t) {
		if(!groupActive(tg)) {
			return false;
		}
		if(crashlist.containsKey(tg.getGroupName())) {
			HashMap<String, Integer> grayFolder = crashlist.get(tg.getGroupName());
			if(grayFolder.containsKey(t.getTestName())) {
				return false;
			} else {
				//other files in the folder are greylisted
				return true;
			}
		} else {
			return true;
		}
	}
		
}
