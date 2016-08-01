package org.dmnk.graalJSchakraTD.classes;

import static org.junit.Assert.*;

import org.dmnk.graalJSchakraTD.classes.GraalJSTestFetcher;
import org.dmnk.graalJSchakraTD.classes.GraalJSTestInitiator;
import org.dmnk.graalJSchakraTD.classes.GraalJSTestRunner;
import org.dmnk.graalJSchakraTD.classes.export.HTMLResultExporter;
import org.dmnk.graalJSchakraTD.classes.export.TextResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.TestFetcher;
import org.dmnk.graalJSchakraTD.interfaces.TestInitiator;
import org.junit.Before;
import org.junit.Test;

public class GraalJSIntegrationTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		GraalJSTestRunner gtr = new GraalJSTestRunner();
	}

}
