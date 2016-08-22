package org.dmnk.graalJSchakraTD.interfaces;

/**
 * just around because the TestInitiator class has some harness code parts (merging with test-code, and similar)
 * which might be useful if such functionality is needed.
 * isn't used anymore, as the TestInitiator was split into the {@link TestDriver} and the {@link TestExecutor} for better understanding and code segregation.
 * 
 * @deprecated
 * @author Dominik
 *
 */
public interface TestInitiator {
	public ExecutedTest runTest(Test t);

}
