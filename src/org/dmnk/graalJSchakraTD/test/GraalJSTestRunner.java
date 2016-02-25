package org.dmnk.graalJSchakraTD.test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.TestInitiator;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;
import org.dmnk.graalJSchakraTD.interfaces.Test;

public class GraalJSTestRunner {
	private String graalPath;
	private String chakraPath;
	private List<ResultExporter> resExp;
	private List<TestGroup> tests;
	private List<TestGroup> executedTests;
	private TestInitiator testInit;

	public static void main(String[] args) {
		GraalJSTestRunner ctr = new GraalJSTestRunner();
		ResultExporter hre = new HTMLResultExporter("htmlResult.html");
		ResultExporter tre = new TextResultExporter("FailPass.csv");
		TestInitiator ti = new GraalJSTestInitiator();
		
		ctr.setGraalPath("./../bin/js");
		ctr.setChakraPath("./chakraTests/test");
		
		ctr.setTestInitiator(ti);
		ctr.addResultExporter(hre);
		ctr.addResultExporter(tre);
		ctr.run(args);
	}
	
	public GraalJSTestRunner() {
		this.resExp = new LinkedList<ResultExporter>();
	}
	
	private void addResultExporter(ResultExporter re) {
		this.resExp.add(re);
	}

	private String getGraalPath() {
		return graalPath;
	}

	private void setGraalPath(String graalPath) {
		this.graalPath = graalPath;
	}

	private String getChakraPath() {
		return chakraPath;
	}

	private void setChakraPath(String chakraPath) {
		this.chakraPath = chakraPath;
	}
	
	private void setTestInitiator(TestInitiator ti) {
		this.testInit = ti;
	}

	private void run(String[] args) {

		//get available tests from filesystem
		GraalJSTestFetcher gjtf = new GraalJSTestFetcher();
		this.tests = gjtf.fetchFromDir(this.chakraPath);
		
		//process white/blacklists
		
		//execute the enabled tests
		for(TestGroup tg : this.tests) {
			TestGroup etg = new GraalJSTestGroup(tg.getGroupName());
			this.executedTests.add(etg);
			for(Test t : tg.getTests()) {
				etg.addTest(this.testInit.runTest(t, this.chakraPath));
			}
			
 		}
		
		//export the result
		for(ResultExporter te : this.resExp) {
			te.export(this.executedTests);
		}
	}
	
	
}
