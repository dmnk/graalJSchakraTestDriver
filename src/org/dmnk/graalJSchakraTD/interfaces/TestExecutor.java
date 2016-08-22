package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.classes.test.TestOutput;

/**
 * is the interface to the actual executable under test, returning the observed output as
 * {@link TestOutput}
 * @author dominik
 *
 */
public interface TestExecutor {
	public TestOutput launch (Test t);
}
