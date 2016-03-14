package org.dmnk.graalJSchakraTD.JUnit;

import static org.junit.Assert.*;

import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.TestFetcher;
import org.dmnk.graalJSchakraTD.interfaces.TestInitiator;
import org.dmnk.graalJSchakraTD.test.GraalJSTestFetcher;
import org.dmnk.graalJSchakraTD.test.GraalJSTestInitiator;
import org.dmnk.graalJSchakraTD.test.GraalJSTestRunner;
import org.dmnk.graalJSchakraTD.test.HTMLResultExporter;
import org.dmnk.graalJSchakraTD.test.TextResultExporter;
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
