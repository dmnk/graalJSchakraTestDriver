package org.dmnk.graalJSchakraTD.classes;

import org.dmnk.graalJSchakraTD.enums.FailReason;
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
	
	public GraalJSFailedTest(Test test, TestOutput to, FailReason fr) {
		super (test, to);
		this.failReason = fr;
	}

	@Override
	public FailReason getFailReason() {
		return failReason;
	}

//	@Override
	public String getErrOut() {
		return tOut.getErrOut();
	}
}
