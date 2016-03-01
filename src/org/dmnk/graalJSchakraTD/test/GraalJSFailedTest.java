package org.dmnk.graalJSchakraTD.test;

import org.dmnk.graalJSchakraTD.enums.FailReason;
import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;

public class GraalJSFailedTest extends GraalJSExecutedTest implements FailedTest {

	public GraalJSFailedTest(String testname, TestType tt) {
		super(testname, tt);
		// TODO Auto-generated constructor stub
	}

	@Override
	public FailReason getFailReason() {
		// TODO Auto-generated method stub
		return null;
	}

}
