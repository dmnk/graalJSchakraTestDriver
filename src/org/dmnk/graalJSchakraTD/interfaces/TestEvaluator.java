package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.classes.test.TestOutput;
import org.dmnk.graalJSchakraTD.enums.FailReason;

public interface TestEvaluator {

	public FailReason evaluate(Test t, TestOutput to);

	public ExecutedTest determineTestResult(Test t, TestOutput to);

	public boolean passed(Test t, TestOutput to);

}
