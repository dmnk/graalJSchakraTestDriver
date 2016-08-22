package org.dmnk.graalJSchakraTD.classes;

import static org.junit.Assert.*;

import org.dmnk.graalJSchakraTD.classes.test.GenericFailedTest;
import org.dmnk.graalJSchakraTD.classes.test.GenericPassedTest;
import org.dmnk.graalJSchakraTD.classes.test.GenericTest;
import org.dmnk.graalJSchakraTD.classes.test.GenericTestExecutedGroup;
import org.dmnk.graalJSchakraTD.enums.FailReason;
import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.exceptions.TestException;
import org.junit.Before;
import org.junit.Test;

public class GraalJSTestExecutedGroupTest {
	private GenericTestExecutedGroup teg;
	
	@Before
	public void setUp() throws Exception {
		teg = new GenericTestExecutedGroup("GroupName");
	}
	
	@Test
	public void testGetOutput() throws TestException {
		GenericFailedTest fto = new GenericFailedTest("IwillFail.js", TestType.BASELINE, 0, "nothing", FailReason.OUTPUT);
		GenericFailedTest ftw = new GenericFailedTest("IwillFailWithWarning.js", TestType.BASELINE, 0, "nothing", FailReason.ERROR);
		teg.addTest(fto);
		teg.addTest(ftw);
		
		if(teg.getOutput() != 1) {
			fail("just one test failed because of his output");
		}
	}
	
	@Test
	public void testGetFailed() throws TestException {
		if(teg.getFailed() != 0) {
			fail("failed tests, even if there are no tests in the group");
		}
		
		GenericFailedTest fto = new GenericFailedTest("IwillFail.js", TestType.BASELINE, 0, "nothing", FailReason.OUTPUT);
		GenericFailedTest ftw = new GenericFailedTest("IwillFailWithWarning.js", TestType.BASELINE, 0, "nothing", FailReason.ERROR);
		teg.addTest(fto);
		
		if(teg.getFailed() != 1) {
			fail("just 1 test failed, " + teg.getFailed() + " returned");
		}
		
		teg.addTest(ftw);
		if(teg.getFailed() != 2) {
			fail("2 tests failed, " + teg.getFailed() + " returned");
		}
		
		if(teg.getOutput() != 1) {
			fail("just one test failed because of his output");
		}
	}
	
	@Test
	public void testSumEqualsParts() throws TestException {
		GenericFailedTest fto = new GenericFailedTest("IwillFail.js", TestType.BASELINE, 0, "nothing", FailReason.OUTPUT);
		GenericFailedTest ftw = new GenericFailedTest("IwillFailWithWarning.js", TestType.BASELINE, 0, "nothing", FailReason.ERROR);
		GenericPassedTest pd = new GenericPassedTest("iwilpass.js",TestType.PASSSTRING, 0, "passed");
		GenericTest excludedTest = new GenericTest("iWontBeTested.js", TestType.PASSSTRING);
		
		teg.addTest(fto);
		teg.addTest(ftw);
		teg.addTest(pd);
		teg.addTest(excludedTest);
		
		int size =  teg.getTests().size();
		
		if(teg.getExcluded() + teg.getPassed() + teg.getFailed() != size) {
			fail("passed + failed NEQ amount of tests in group");
		}
		
		int failSum = teg.getError() + teg.getCrashed() + teg.getException() + teg.getOutput();
		if(failSum != teg.getFailed()) {
			fail("sum of failed tests reasons ("+failSum+") NEQ getFailed() ("+teg.getFailed()+")");
		}
	}

}
