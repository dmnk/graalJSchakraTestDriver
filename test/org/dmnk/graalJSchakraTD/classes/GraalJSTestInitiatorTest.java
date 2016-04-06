package org.dmnk.graalJSchakraTD.classes;

import static org.junit.Assert.*;

import java.io.File;

import org.dmnk.graalJSchakraTD.classes.GraalJSTest;
import org.dmnk.graalJSchakraTD.classes.GraalJSTestInitiator;
import org.dmnk.graalJSchakraTD.classes.TestOutput;
import org.dmnk.graalJSchakraTD.classes.TestType;
import org.junit.Before;
import org.junit.Test;

public class GraalJSTestInitiatorTest {
	private GraalJSTestInitiator ti;
	@Before
	public void setUp() throws Exception {
		ti = new GraalJSTestInitiator("../../GraalVM-0.10/bin/js");
	}

	@Test
	public void testExecuteGraalJSTest() {
		/**
		 * in special if file with reference error still has this error when harness file  is leading
		 */
		GraalJSTest t = new GraalJSTest("data/arr_bailout.js", TestType.PASSSTRING);
		TestOutput to = ti.executeGraalJS(new File(t.getFilename()));
		
		if(to.getErrOut().length() != 0) {
			fail("errors happened: " + to.getErrOut());
		}
		if(to.getReturnCode() != 0) {
			fail("not successfully executed! RC: "+ to.getReturnCode());
		}
		if(!to.getStdOut().trim().equals("Passed")) {
			fail("Output should be 'Passed', was: " + to.getStdOut());
		}
		
	}

	@Test
	public void testLaunchGraal() {
		TestOutput to = ti.launchGraal(new File("data/arr_bailout.js"));
		if(to.getStdOut().length() != 0) {
			fail("sysout :" + to.getStdOut());
		}
		if(to.getErrOut().length() != 0) {
			fail("errors happened: " + to.getErrOut());
		}
		if(to.getReturnCode() != 0) {
			fail("not successfully executed! RC: "+ to.getReturnCode());
		}
		
		fail("Not yet implemented");
	}

}
