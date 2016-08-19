package org.dmnk.graalJSchakraTD.classes.test;

import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;

public abstract class GenericExecutedTest extends GenericTest implements ExecutedTest {

	protected TestOutput tOut;
	
	public GenericExecutedTest(String testname, TestType tt, int returnCode, String output) {
		super(testname, tt);
		
		tOut = new TestOutput(returnCode,output,"");
	}
	
	public GenericExecutedTest(Test plannedTest, int returnCode, String output) {
		super(plannedTest.getFilename(), plannedTest.getTestType());

		tOut = new TestOutput(returnCode,output,"");
	}
	
	public GenericExecutedTest(ExecutedTest eTest) {
		super(eTest);
		
		tOut = new TestOutput(eTest.getReturncode(), eTest.getOutput(), eTest.getErrorOutput());
	}
	
	public GenericExecutedTest(Test plannedTest, TestOutput tOutput) {
		super(plannedTest);
		
//		if(tOutput == null) {
//			throw new TestException("can't create an Executed test without a TestOutput");
//		}
		
		tOut = tOutput;
	}

	@Override
	public int getReturncode() {
		return tOut.getReturnCode();
	}

	@Override
	public String getOutput() {
		return tOut.getStdOut();
	}
	
	@Override
	public String getErrorOutput() {
		return tOut.getErrOut();
	}

}
