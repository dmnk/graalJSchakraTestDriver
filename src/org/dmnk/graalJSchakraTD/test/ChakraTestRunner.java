package org.dmnk.graalJSchakraTD.test;

import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.TestInitiator;

public class ChakraTestRunner {
	private String graalPath;
	private String chakraPath;
	private ResultExporter resExp;
	private TestInitiator testInit;

	public static void main(String[] args) {
		ChakraTestRunner ctr = new ChakraTestRunner();
		ResultExporter hre = new HTMLResultExporter();
		TestInitiator ti = new TestInitiator();
		
		ctr.setGraalPath("./../bin/js");
		ctr.setChakraPath("./chakraTests/test");
		
		ctr.setTestInitiator(ti);
		ctr.setResultExporter(hre);
		ctr.run(args);
	}
	
	private void setResultExporter(ResultExporter re) {
		this.resExp = re;
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
		//export the result
	}
	
}
