package org.dmnk.graalJSchakraTD.classes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dmnk.graalJSchakraTD.classes.Configuration.ListMode;
import org.dmnk.graalJSchakraTD.classes.export.HTMLResultExporter;
import org.dmnk.graalJSchakraTD.classes.export.TextResultExporter;
import org.dmnk.graalJSchakraTD.classes.test.GenericTestExecutedGroup;
import org.dmnk.graalJSchakraTD.classes.test.TestOutput;
import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.exceptions.TestException;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
import org.dmnk.graalJSchakraTD.interfaces.ListFetcher;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestDriver;
import org.dmnk.graalJSchakraTD.interfaces.TestEvaluator;
import org.dmnk.graalJSchakraTD.interfaces.TestExecutedGroup;
import org.dmnk.graalJSchakraTD.interfaces.TestExecutor;
import org.dmnk.graalJSchakraTD.interfaces.TestFetcher;

/**
 * standard {@link TestDriver} implementation, processing all the tests in sequence.
 * all the other TestDriver implementations are extending this class, as just the process call is modified.
 * @author dominik
 *
 */
public class TestDriverGeneric implements TestDriver {
	protected List<ResultExporter> resExp;
	protected List<TestGroup> tests;
	protected List<TestExecutedGroup> executedTests;
	protected Map<String, HashMap<String, Integer>> crashlist;
	protected Map<String, Integer> folderList;
	protected Configuration conf;
	protected TestEvaluator tEval;
	
	public TestDriverGeneric(Configuration c, TestFetcher tf, ListFetcher lf, TestEvaluator te) throws ConfigurationException {
		conf = c;
		
		resExp = new LinkedList<ResultExporter>();
		executedTests = new LinkedList<TestExecutedGroup>();
		tEval = te;
		
		setup(tf, lf);
	}
	
	
	private void addResultExporter(ResultExporter re) {
		resExp.add(re);
	}
	
	/**
	 * uses the two fetchers to fill the black / white / graylist objects and adds the
	 * configured {@link ResultExporter} classes
	 * 
	 * @param tf {@link TestFetcher}
	 * @param lf {@link ListFetcher}
	 * @throws ConfigurationException
	 */
	private void setup(TestFetcher tf, ListFetcher lf) throws ConfigurationException {
		fetchTests(tf);
		fetchLists(lf);
		
		//if config says the export is enabled, add it
		if(conf.checkExport("html")) {
			ResultExporter hre = new HTMLResultExporter(conf.getExport("html"));
			addResultExporter(hre);
		}
		if (conf.checkExport("csv")) {
			ResultExporter tre = new TextResultExporter(conf.getExport("csv"));
			addResultExporter(tre);
		}
	}
	
	/**
	 * get available tests from filesystem
	 * @param tf
	 * @throws ConfigurationException 
	 */
	private void fetchTests(TestFetcher tf) throws ConfigurationException {
		tests = tf.fetch(); //FromDir(this.getChakraPath());
	}
	
	/**
	 * process white/black/crashlists
	 * @param lf
	 * @throws javax.naming.ConfigurationException 
	 */
	private void fetchLists(ListFetcher lf) throws ConfigurationException {
		crashlist = lf.fetchFileList(conf.getGrayList());
		
		Helper.debugOut(conf, 1, "TR", crashlist.size()+"");
		for(Entry<String, HashMap<String, Integer>> entry : crashlist.entrySet()) {
			Helper.debugOut(conf, 1, "TR-entry", entry.getKey());
		}
		
		if(conf.getListMode() == ListMode.WHITE) {
			folderList = lf.fetchFolderList(conf.getWhiteList());
		} else {
			folderList = lf.fetchFolderList(conf.getBlackList());
		}
	}

	/**
	 * checks if the tests in the list are in the enabled / disabled group
	 * and passes the enabled ones to the {@link TestExecutor}.
	 * for evaluating the outcome of the test, the registered {@link TestEvaluator} is used
	 */
	public void process() throws TestException, ConfigurationException {		
		//execute the enabled tests
		for(TestGroup tg : tests) {			
			if(tg == null) {
				continue;
				//actually, that shouldn't happen
				// but it does, and this keeps it running, without further negative affects
				// TODO: investigate where the null groups come from
			}
			System.out.println("\nGroup: "+tg.getGroupName());
			
			//check if folder is black- / whitelisted
			if(!groupActive(tg)) {
				//back to loop
				continue;
			}
			
			TestExecutedGroup etg = new GenericTestExecutedGroup(tg.getGroupName());
			executedTests.add(etg);
			
			for(Test t : tg.getTests()) {
				if(fileActive(tg, t)) {
					TestExecutor te = new TestExecutorGeneric(conf);
					TestOutput to = te.launch(t);
					
					ExecutedTest et = tEval.determineTestResult(t, to);
					
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
	}
	
	/**
	 * calls each registered {@link ResultExporter} with the list of executed tests 
	 */
	@Override
	public final void export() {
		for(ResultExporter te : resExp) {
			te.export(executedTests);
		}
	}
	
	/**
	 * checks if the folder is ok (not in black/ in whitelist)
	 * 
	 * @param tg
	 * @return
	 */
	protected final boolean groupActive(TestGroup tg) {
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
	protected final boolean fileActive(TestGroup tg, Test t) {
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
