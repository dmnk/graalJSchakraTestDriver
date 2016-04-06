package org.dmnk.graalJSchakraTD.classes;

import static org.junit.Assert.*;

import org.dmnk.graalJSchakraTD.classes.GraalJSFailedTest;
import org.dmnk.graalJSchakraTD.classes.GraalJSPassedTest;
import org.dmnk.graalJSchakraTD.classes.GraalJSTest;
import org.dmnk.graalJSchakraTD.classes.GraalJSTestExecutedGroup;
import org.dmnk.graalJSchakraTD.classes.TestType;
import org.dmnk.graalJSchakraTD.enums.FailReason;
import org.junit.Before;
import org.junit.Test;

public class GraalJSTestExecutedGroupTest {
	private GraalJSTestExecutedGroup teg;
	
	@Before
	public void setUp() throws Exception {
		teg = new GraalJSTestExecutedGroup("GroupName");
	}
	
	@Test
	public void testGetOutput() {
		GraalJSFailedTest fto = new GraalJSFailedTest("IwillFail.js", TestType.BASELINE, 0, "nothing", FailReason.OUTPUT);
		GraalJSFailedTest ftw = new GraalJSFailedTest("IwillFailWithWarning.js", TestType.BASELINE, 0, "nothing", FailReason.WARNING);
		teg.addTest(fto);
		teg.addTest(ftw);
		
		if(teg.getOutput() != 1) {
			fail("just one test failed because of his output");
		}
	}
	
	@Test
	public void testGetFailed() {
		if(teg.getFailed() != 0) {
			fail("failed tests, even if there are no tests in the group");
		}
		
		GraalJSFailedTest fto = new GraalJSFailedTest("IwillFail.js", TestType.BASELINE, 0, "nothing", FailReason.OUTPUT);
		GraalJSFailedTest ftw = new GraalJSFailedTest("IwillFailWithWarning.js", TestType.BASELINE, 0, "nothing", FailReason.WARNING);
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
	public void testSumEqualsParts() {
		GraalJSFailedTest fto = new GraalJSFailedTest("IwillFail.js", TestType.BASELINE, 0, "nothing", FailReason.OUTPUT);
		GraalJSFailedTest ftw = new GraalJSFailedTest("IwillFailWithWarning.js", TestType.BASELINE, 0, "nothing", FailReason.WARNING);
		GraalJSPassedTest pd = new GraalJSPassedTest("iwilpass.js",TestType.PASSSTRING, 0, "passed");
		GraalJSTest excludedTest = new GraalJSTest("iWontBeTested.js", TestType.PASSSTRING);
		
		teg.addTest(fto);
		teg.addTest(ftw);
		teg.addTest(pd);
		teg.addTest(excludedTest);
		
		int size =  teg.getTests().size();
		
		if(teg.getExcluded() + teg.getPassed() + teg.getFailed() != size) {
			fail("passed + failed NEQ amount of tests in group");
		}
		
		int failSum = teg.getAssert() + teg.getCrashed() + teg.getException() + teg.getWarnings() + teg.getOutput();
		if(failSum != teg.getFailed()) {
			fail("sum of failed tests reasons ("+failSum+") NEQ getFailed() ("+teg.getFailed()+")");
		}
	}

}
