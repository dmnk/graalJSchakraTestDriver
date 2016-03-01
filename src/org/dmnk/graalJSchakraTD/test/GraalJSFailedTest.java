package org.dmnk.graalJSchakraTD.test;

import org.dmnk.graalJSchakraTD.enums.FailReason;
import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;

public class GraalJSFailedTest extends GraalJSExecutedTest implements FailedTest {
	private FailReason failReason;
	
	public GraalJSFailedTest(String testname, TestType tt, int returnCode, String output, FailReason fr) {
		super(testname, tt, returnCode, output);
		this.failReason = fr;
	}
	
	public GraalJSFailedTest(Test test, int returnCode, String output, FailReason fr) {
		super(test, returnCode, output);
		this.failReason = fr;
	}

	@Override
	public FailReason getFailReason() {
		return failReason;
	}

}
