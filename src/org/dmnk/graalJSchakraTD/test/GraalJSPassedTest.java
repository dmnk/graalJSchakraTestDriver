package org.dmnk.graalJSchakraTD.test;

import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;

public class GraalJSPassedTest extends GraalJSExecutedTest implements PassedTest {

	public GraalJSPassedTest(String testname, TestType tt) {
		super(testname, tt);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getBaseline() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getReturncode() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public TestType getTestType() {
		// TODO Auto-generated method stub
		return null;
	}



}
