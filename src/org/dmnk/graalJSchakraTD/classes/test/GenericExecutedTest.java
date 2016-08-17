package org.dmnk.graalJSchakraTD.classes.test;

import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;

public abstract class GenericExecutedTest extends GenericTest implements ExecutedTest {

//	private String output;
//	private int returnCode;
	protected TestOutput tOut;
	
	public GenericExecutedTest(String testname, TestType tt, int returnCode, String output) {
		super(testname, tt);
		this.tOut = new TestOutput(returnCode,output,"");
//		this.returnCode = returnCode;
//		this.output = output;
	}
	
	public GenericExecutedTest(Test plannedTest, int returnCode, String output) {
		super(plannedTest.getFilename(), plannedTest.getTestType());
//		this.returnCode = returnCode;
//		this.output = output;

		this.tOut = new TestOutput(returnCode,output,"");
	}
	
	public GenericExecutedTest(ExecutedTest eTest) {
		super(eTest);
//		this.returnCode = eTest.getReturncode();
//		this.output = eTest.getOutput();
		this.tOut = new TestOutput(eTest.getReturncode(), eTest.getOutput(), "");//TODO: expand with getErrOut();
	}
	
	public GenericExecutedTest(Test plannedTest, TestOutput tOut) {
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
