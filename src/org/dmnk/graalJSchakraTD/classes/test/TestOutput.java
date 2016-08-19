package org.dmnk.graalJSchakraTD.classes.test;

/**
 * stores the output and return code produced by the executable under test
 * during a test-run
 * 
 * @author Dominik
 *
 */
public class TestOutput {
	private int returnCode;
	private String stdOut;
	private String errOut;
	
	public TestOutput (int rc, String stdOut, String errOut) {
		this.returnCode = rc;
		this.stdOut = stdOut;
		this.errOut = errOut;
	}
	
	public int getReturnCode() {
		return returnCode;
	}
	
	public String getStdOut() {
		return stdOut;
	}
	
	public String getErrOut() {
		return errOut;
	}	
}
