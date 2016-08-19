package org.dmnk.graalJSchakraTD.classes.test;

import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;

/**
 * if the executable under test passes a test, it will be stored in an entity from this class.
 * there are no additions compared to the {@link ExecutedTest} interface.
 * @author Dominik
 *
 */
public class GenericPassedTest extends GenericExecutedTest implements PassedTest {

	public GenericPassedTest(String testname, TestType tt, int returnCode, String output) {
		super(testname, tt, returnCode, output);
	}
	
	public GenericPassedTest(Test Test, int returnCode, String output) {
		super(Test, returnCode, output);
	}

	public GenericPassedTest(Test t, TestOutput to) {
		super(t, to);
	}
}
