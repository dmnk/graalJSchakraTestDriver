package org.dmnk.graalJSchakraTD.test;

import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;

public abstract class GraalJSExecutedTest extends GraalJSTest implements ExecutedTest {

	private String output;
	private int returnCode;
	
	public GraalJSExecutedTest(String testname, TestType tt, int returnCode, String output) {
		super(testname, tt);
		this.returnCode = returnCode;
		this.output = output;
	}
	
	public GraalJSExecutedTest(Test plannedTest, int returnCode, String output) {
		super(plannedTest.getFilename(), plannedTest.getTestType());
		this.returnCode = returnCode;
		this.output = output;
	}
	
	public GraalJSExecutedTest(ExecutedTest eTest) {
		super(eTest);
		this.returnCode = eTest.getReturncode();
		this.output = eTest.getOutput();
	}

	@Override
	public int getReturncode() {
		return returnCode;
	}

	@Override
	public String getOutput() {
		return output;
	}

}
