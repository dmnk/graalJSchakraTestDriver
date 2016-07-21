package org.dmnk.graalJSchakraTD.classes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dmnk.graalJSchakraTD.classes.Configuration.ExecutableMode;
import org.dmnk.graalJSchakraTD.classes.configProvider.CLIConfigProvider;
import org.dmnk.graalJSchakraTD.classes.export.HTMLResultExporter;
import org.dmnk.graalJSchakraTD.classes.export.TextResultExporter;
import org.dmnk.graalJSchakraTD.exceptions.GraalJSTestException;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
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
	private TestInitiator testInit;
	private TestFetcher tf;

	public static void main(String[] args) {
		
		Configuration conf = new CLIConfigProvider(args).getConfig();
		
		conf.setGraalJSexec(Configuration.ExecutableMode.DIRECT, "../../GraalVM-0.10/bin/js");
		conf.setChakraTestsPath("../../GraalVM-0.10/chakraTests/test/");
		conf.addExport("html", "./data/htmlResult.html");
		conf.addExport("csv", "./data/FailPass.csv");
		
		GraalJSTestRunner ctr = new GraalJSTestRunner();
		try {
			if(conf.checkExport("html")) {
				ResultExporter hre = new HTMLResultExporter(conf.getExport("html"));
				ctr.addResultExporter(hre);
			}
			if (conf.checkExport("csv")) {
				ResultExporter tre = new TextResultExporter(conf.getExport("csv"));
//				ctr.addResultExporter(tre);
			}
			TestInitiator ti = new GraalJSTestInitiator(conf.getGraalJSexec());
			TestFetcher tf = new GraalJSTestFetcher(conf.getChakraTestsPath());
						
			ctr.setTestInitiator(ti);
			ctr.setTestFetcher(tf);
			ctr.run(args);
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
	
	private void addResultExporter(ResultExporter re) {
		resExp.add(re);
	}
	
	private void setTestInitiator(TestInitiator ti) {
		testInit = ti;
	}
	
	private void setTestFetcher(TestFetcher tf) {
		this.tf = tf;
	}

	private void run(String[] args) {
		//check preconditions (propably in the constructors of TestFetcher, TestRunner, ...)

		//get available tests from filesystem
		tests = tf.fetch(); //FromDir(this.getChakraPath());
		
		//process white/black/crashlists
		Map<String, HashMap<String, Integer>> crashlist = new HashMap<String, HashMap<String, Integer>>();
		HashMap<String, Integer> arrayexclusionlist = new HashMap<String, Integer>();
		arrayexclusionlist.put("array_init.js", 1);
		arrayexclusionlist.put("CopyOnAccessArray_cache_index_overflow.js", 1);
		crashlist.put("Array", arrayexclusionlist);
		
		Map<String, Integer> folderList = new HashMap<String, Integer>();
		folderList.put("Array", 0);
//		folderList.put("Basics", 0);
//		folderList.put("Closures", 0);
//		folderList.put("ControlFlow", 0);
//		folderList.put("Date", 0);
		
		String mode = "WL"; //BL
		
		//execute the enabled tests
		for(TestGroup tg : tests) {
			if(tg == null) {
				continue;
				//actually, that shouldn't happen
			}
			switch (mode) {
			case "WL":
				if(!folderList.containsKey(tg.getGroupName())) {
					continue;
				}
				break ;
			case "BL":
				if(folderList.containsKey(tg.getGroupName())) {
					continue;
				}
				break;				
			}
			TestExecutedGroup etg = new GraalJSTestExecutedGroup(tg.getGroupName());
			executedTests.add(etg);
			
			for(Test t : tg.getTests()) {
				
				if(!arrayexclusionlist.containsKey(t.getTestName())) {
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
	
	
}
