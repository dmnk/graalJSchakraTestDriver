package org.dmnk.graalJSchakraTD.test;

import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;

public abstract class GraalJSExecutedTest extends GraalJSTest implements ExecutedTest {

//	private String output;
//	private int returnCode;
	protected TestOutput tOut;
	
	public GraalJSExecutedTest(String testname, TestType tt, int returnCode, String output) {
		super(testname, tt);
		this.tOut = new TestOutput(returnCode,output,"");
//		this.returnCode = returnCode;
//		this.output = output;
	}
	
	public GraalJSExecutedTest(Test plannedTest, int returnCode, String output) {
		super(plannedTest.getFilename(), plannedTest.getTestType());
//		this.returnCode = returnCode;
//		this.output = output;

		this.tOut = new TestOutput(returnCode,output,"");
	}
	
	public GraalJSExecutedTest(ExecutedTest eTest) {
		super(eTest);
//		this.returnCode = eTest.getReturncode();
//		this.output = eTest.getOutput();
		this.tOut = new TestOutput(eTest.getReturncode(), eTest.getOutput(), "");//TODO: expand with getErrOut();
	}
	
	public GraalJSExecutedTest(Test plannedTest, TestOutput tOut) {
		super(plannedTest);
		this.tOut = tOut;
	}

	@Override
	public int getReturncode() {
		assert tOut != null;
		return tOut.getReturnCode();
	}

	@Override
	public String getOutput() {
		assert tOut != null;
		return tOut.getStdOut();
	}
	

}
