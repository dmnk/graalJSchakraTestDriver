package org.dmnk.graalJSchakraTD.classes;

import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;

public class GraalJSPassedTest extends GraalJSExecutedTest implements PassedTest {

	public GraalJSPassedTest(String testname, TestType tt, int returnCode, String output) {
		super(testname, tt, returnCode, output);
	}
	
	public GraalJSPassedTest(Test Test, int returnCode, String output) {
		super(Test, returnCode, output);
	}

	public GraalJSPassedTest(Test t, TestOutput to) {
		super(t, to);
	}
}
