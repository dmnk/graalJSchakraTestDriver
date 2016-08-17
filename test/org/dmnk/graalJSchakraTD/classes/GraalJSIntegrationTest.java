package org.dmnk.graalJSchakraTD.classes;

import static org.junit.Assert.*;

import org.dmnk.graalJSchakraTD.classes.TestFetcherGeneric;
import org.dmnk.graalJSchakraTD.classes.GenericTestInitiator;
import org.dmnk.graalJSchakraTD.classes.TestDriverGeneric;
import org.dmnk.graalJSchakraTD.classes.export.HTMLResultExporter;
import org.dmnk.graalJSchakraTD.classes.export.TextResultExporter;
import org.dmnk.graalJSchakraTD.classes.test.GenericTestEvaluator;
import org.dmnk.graalJSchakraTD.interfaces.ListFetcher;
import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.TestDriver;
import org.dmnk.graalJSchakraTD.interfaces.TestEvaluator;
import org.dmnk.graalJSchakraTD.interfaces.TestFetcher;
import org.dmnk.graalJSchakraTD.interfaces.TestInitiator;
import org.junit.Before;
import org.junit.Test;

public class GraalJSIntegrationTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void smokeTest() {
		Configuration c = new Configuration();
		TestFetcher tf = new TestFetcherGeneric(c.getTestsPath());
		ListFetcher lf = new ListFetcherGeneric(c);
		TestEvaluator te = new GenericTestEvaluator();

		//sequential execution
		TestDriver td = new TestDriverGeneric(c, tf, lf, te);
	}

}
