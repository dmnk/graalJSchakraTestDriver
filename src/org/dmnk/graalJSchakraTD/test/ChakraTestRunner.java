package org.dmnk.graalJSchakraTD.test;

import java.util.LinkedList;
import java.util.List;

import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.TestInitiator;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;
import org.dmnk.graalJSchakraTD.interfaces.Test;

public class ChakraTestRunner {
	private String graalPath;
	private String chakraPath;
	private List<ResultExporter> resExp;
	private List<TestGroup> tests;
	private List<TestGroup> executedTests;
	private TestInitiator testInit;

	public static void main(String[] args) {
		ChakraTestRunner ctr = new ChakraTestRunner();
		ResultExporter hre = new HTMLResultExporter("htmlResult.html");
		ResultExporter tre = new TextResultExporter();
		TestInitiator ti = new TestInitiator();
		
		ctr.setGraalPath("./../bin/js");
		ctr.setChakraPath("./chakraTests/test");
		
		ctr.setTestInitiator(ti);
		ctr.addResultExporter(hre);
		ctr.addResultExporter(tre);
		ctr.run(args);
	}
	
	private ChakraTestRunner() {
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
		//process white/blacklists
		//execute the enabled tests
		
		for(TestGroup tg : this.tests) {
			TestGroup etg = new TestGroup(tg.getGroupName());
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
