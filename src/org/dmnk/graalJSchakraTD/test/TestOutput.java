package org.dmnk.graalJSchakraTD.test;

public class TestOutput {
	private int returnCode;
	private String stdOut;
	private String errOut;
	
	TestOutput (int rc, String stdOut, String errOut) {
		this.returnCode = rc;
		this.stdOut = stdOut;
		this.errOut = errOut;
	}
	
	int getReturnCode() {
		return returnCode;
	}
	
	String getStdOut() {
		return stdOut;
	}
	
	String getErrOut() {
		return errOut;
	}	
}
