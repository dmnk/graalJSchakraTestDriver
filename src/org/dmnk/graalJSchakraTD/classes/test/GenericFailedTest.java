package org.dmnk.graalJSchakraTD.classes.test;

import org.dmnk.graalJSchakraTD.enums.FailReason;
import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;

public class GenericFailedTest extends GenericExecutedTest implements FailedTest {
	private FailReason failReason;
	private String diff;
	
	public GenericFailedTest(String testname, TestType tt, int returnCode, String output, FailReason fr) {
		super(testname, tt, returnCode, output);
		failReason = fr;
	}
	
	public GenericFailedTest(Test test, int returnCode, String output, FailReason fr) {
		super(test, returnCode, output);
		failReason = fr;
	}
	
	public GenericFailedTest(Test test, TestOutput to, FailReason fr) {
		super (test, to);
		failReason = fr;
	}

	public GenericFailedTest(Test test, TestOutput to, FailReason fr, String differences) {
		this(test, to, fr);
		diff = differences;
	}
	
	@Override
	public FailReason getFailReason() {
		return failReason;
	}
	
	@Override
	public String getDiff() {
		if(diffIsSet()) {
			return diff;
		} else {
			return "NO DIFF";
		}
	}
	
	@Override
	public boolean diffIsSet() {
		return this.diff != null;
	}
}
