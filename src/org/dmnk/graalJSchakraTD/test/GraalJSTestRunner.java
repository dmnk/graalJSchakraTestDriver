package org.dmnk.graalJSchakraTD.test;

import java.util.LinkedList;
import java.util.List;

import org.dmnk.graalJSchakraTD.Exceptions.GraalJSTestException;
import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.TestInitiator;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestExecutedGroup;
import org.dmnk.graalJSchakraTD.interfaces.TestFetcher;

public class GraalJSTestRunner {
//	private String graalPath;
//	private String chakraPath;
	private List<ResultExporter> resExp;
	private List<TestGroup> tests;
	private List<TestExecutedGroup> executedTests;
	private TestInitiator testInit;
	private TestFetcher tf;

	public static void main(String[] args) {
		
		String graalJSpath = "./../bin/js";
		String chakraTestsPath = "./chakraTests/test";
		//TODO: parameter parser
		
		GraalJSTestRunner ctr = new GraalJSTestRunner();
		try {
			ResultExporter hre = new HTMLResultExporter("htmlResult.html");
			ResultExporter tre = new TextResultExporter("FailPass.csv");
			TestInitiator ti = new GraalJSTestInitiator(graalJSpath);
			TestFetcher tf = new GraalJSTestFetcher(chakraTestsPath);
			
//			ctr.setGraalPath("./../bin/js");
//			ctr.setChakraPath("./chakraTests/test");
			
			ctr.setTestInitiator(ti);
			ctr.setTestFetcher(tf);
			ctr.addResultExporter(hre);
			ctr.addResultExporter(tre);
			ctr.run(args);
		} catch (GraalJSTestException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}		
	}
	
	public GraalJSTestRunner() {
		this.resExp = new LinkedList<ResultExporter>();
	}
	
	private void addResultExporter(ResultExporter re) {
		this.resExp.add(re);
	}

//	private String getGraalPath() {
//		return graalPath;
//	}

//	private void setGraalPath(String graalPath) {
//		this.graalPath = graalPath;
//	}

//	private String getChakraPath() {
//		return chakraPath;
//	}

//	private void setChakraPath(String chakraPath) {
//		this.chakraPath = chakraPath;
//	}
	
	private void setTestInitiator(TestInitiator ti) {
		this.testInit = ti;
	}
	
	private void setTestFetcher(TestFetcher tf) {
		this.tf = tf;
	}

	private void run(String[] args) {
		//check preconditions (propably in the constructors of TestFetcher, TestRunner, ...)

		//get available tests from filesystem
		tests = tf.fetch(); //FromDir(this.getChakraPath());
		
		//process white/black/crashlists
		
		//execute the enabled tests
		for(TestGroup tg : tests) {
			TestExecutedGroup etg = new GraalJSTestExecutedGroup(tg.getGroupName());
			executedTests.add(etg);
			
			for(Test t : tg.getTests()) {
				etg.addTest(testInit.runTest(t));
			}
			
 		}
		
		//export the result
		for(ResultExporter te : this.resExp) {
			te.export(this.executedTests);
		}
	}
	
	
}
