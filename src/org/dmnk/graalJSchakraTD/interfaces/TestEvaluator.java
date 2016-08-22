package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.classes.test.TestOutput;
import org.dmnk.graalJSchakraTD.enums.FailReason;

/**
 * uses the tests type and the observed output, to check if and why a test failed a test 
 * and returns either boolean if a tests was passed, or as encapsulated in a new {@link ExecutedTest} object the {@link FailReason}
 * 
 * @author dominik
 *
 */
public interface TestEvaluator {

	public FailReason evaluate(Test t, TestOutput to);

	public ExecutedTest determineTestResult(Test t, TestOutput to);

	public boolean passed(Test t, TestOutput to);

}
