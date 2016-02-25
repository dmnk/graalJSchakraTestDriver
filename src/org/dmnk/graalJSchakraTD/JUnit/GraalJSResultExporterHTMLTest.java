package org.dmnk.graalJSchakraTD.JUnit;

import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.dmnk.graalJSchakraTD.interfaces.TestGroup;
import org.dmnk.graalJSchakraTD.test.GraalJSTest;
import org.dmnk.graalJSchakraTD.test.GraalJSTestGroup;
import org.dmnk.graalJSchakraTD.test.HTMLResultExporter;
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
		List<TestGroup> ltg = new LinkedList<TestGroup>();
		TestGroup tg = new GraalJSTestGroup("The Test Group");
		tg.addTest(new GraalJSTest("theJStest.js"));
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
		List<TestGroup> ltg = new LinkedList<TestGroup>();
		TestGroup tg = new GraalJSTestGroup("The Test Group");
		tg.addTest(new GraalJSTest("theJStest.js"));
		tg.addTest(new GraalJSTest("theJStest.js"));
		tg.addTest(new GraalJSTest("theJStest.js"));
		ltg.add(tg);
		
		tg = new GraalJSTestGroup("The 2nd Test Group");
		tg.addTest(new GraalJSTest("theJStest.js"));
		tg.addTest(new GraalJSTest("theJStest.js"));
		tg.addTest(new GraalJSTest("theJStest.js"));
		tg.addTest(new GraalJSTest("theJStest.js"));
		tg.addTest(new GraalJSTest("theJStest.js"));
		tg.addTest(new GraalJSTest("theJStest.js"));
		ltg.add(tg);
		
		hre.export(ltg);
		
		File f = new File("./data/htmlExportJUnitTest2.html");
		
		fail("Not yet implemented");
	}

}
