package org.dmnk.graalJSchakraTD.classes;

import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.dmnk.graalJSchakraTD.classes.GraalJSFailedTest;
import org.dmnk.graalJSchakraTD.classes.GraalJSPassedTest;
import org.dmnk.graalJSchakraTD.classes.GraalJSTest;
import org.dmnk.graalJSchakraTD.classes.GraalJSTestExecutedGroup;
import org.dmnk.graalJSchakraTD.classes.TestType;
import org.dmnk.graalJSchakraTD.classes.export.HTMLResultExporter;
import org.dmnk.graalJSchakraTD.enums.FailReason;
import org.dmnk.graalJSchakraTD.interfaces.TestExecutedGroup;
import org.junit.Before;
import org.junit.Test;

public class GraalJSResultExporterHTMLTest {
	private HTMLResultExporter hre;
	
	@Before
	public void setUp() throws Exception {
		hre = new HTMLResultExporter("");
	}

	@Test
	public void testSetExportPath() {
		hre.setExportPath("./data/htmlExportJUnitTest.html");
		List<TestExecutedGroup> ltg = new LinkedList<TestExecutedGroup>();
		TestExecutedGroup tg = new GraalJSTestExecutedGroup("The Test Group");
		tg.addTest(new GraalJSTest("theJStest.js", TestType.BASELINE));
		ltg.add(tg);
		
		hre.export(ltg);
		
		File f = new File("./data/htmlExportJUnitTest.html");
		if(!f.exists()) {
			fail("File not created");
		}
	}

	@Test
	public void testExport() {
		hre.setExportPath("./data/htmlExportJUnitTest2.html");
		List<TestExecutedGroup> ltg = new LinkedList<TestExecutedGroup>();
		TestExecutedGroup tg = new GraalJSTestExecutedGroup("The Test Group");
		//
		org.dmnk.graalJSchakraTD.interfaces.Test tEx = new GraalJSTest("arr_bailout.js", TestType.BASELINE);
		org.dmnk.graalJSchakraTD.interfaces.Test t = new GraalJSTest("arr_bailout.js", TestType.BASELINE);
		org.dmnk.graalJSchakraTD.interfaces.Test tPassed = new GraalJSPassedTest(t,0, "Passed");
		org.dmnk.graalJSchakraTD.interfaces.Test tFailedAssert = new GraalJSFailedTest(t,0, "Passed", FailReason.ASSERTION);
		org.dmnk.graalJSchakraTD.interfaces.Test tFailedOutput = new GraalJSFailedTest(t,0, "Passed", FailReason.OUTPUT);
		org.dmnk.graalJSchakraTD.interfaces.Test tFailedCrash = new GraalJSFailedTest(t,0, "Passed", FailReason.CRASH);
		org.dmnk.graalJSchakraTD.interfaces.Test tFailedExc = new GraalJSFailedTest(t,0, "Passed", FailReason.EXCEPTION);
		org.dmnk.graalJSchakraTD.interfaces.Test tFailedWarn = new GraalJSFailedTest(t,0, "Passed", FailReason.WARNING);
		
		tg.addTest(tEx);
//		tg.addTest(t);
		tg.addTest(tPassed);
		tg.addTest(tFailedAssert);
		tg.addTest(tFailedCrash);
		tg.addTest(tFailedOutput);
		tg.addTest(tFailedExc);
		tg.addTest(tFailedWarn);
		
		ltg.add(tg);
		
		tg = new GraalJSTestExecutedGroup("The 2nd Test Group");
		tg.addTest(t);
		tg.addTest(t);
		tg.addTest(t);
		tg.addTest(t);
		tg.addTest(t);
		tg.addTest(t);
		ltg.add(tg);
		
		hre.export(ltg);
		
//		File f = new File("./data/htmlExportJUnitTest2.html");
		
//		fail("Not yet implemented");
	}

}
