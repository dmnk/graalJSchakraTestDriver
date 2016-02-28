package org.dmnk.graalJSchakraTD.test;

import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;

public class GraalJSExecutedTest extends GraalJSTest implements ExecutedTest {

	private String output;
	
	public GraalJSExecutedTest(String testname, TestType tt) {
		super(testname, tt);
	}

	@Override
	public String getFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBaseline() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestType getTestType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getReturncode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getOutput() {
		return output;
	}

}
