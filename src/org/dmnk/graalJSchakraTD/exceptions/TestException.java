package org.dmnk.graalJSchakraTD.exceptions;

public class TestException extends Exception {

	/**
	 * thrown when a problem with a test, or during testing occurs, thats not
	 * related to the configuration
	 * @author Dominik
	 */
	private static final long serialVersionUID = 1L;

	public TestException (String error) {
		super(error);
	}
}
