package org.dmnk.graalJSchakraTD.classes;

import static org.junit.Assert.*;

import java.util.List;

import org.dmnk.graalJSchakraTD.classes.GraalJSTestFetcher;
import org.dmnk.graalJSchakraTD.interfaces.TestFetcher;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;
import org.junit.Before;
import org.junit.Test;

public class GraalJSTestFetcherTestOneDir {
	private List<TestGroup> tg;
	
	@Before
	public void setUp() {
		TestFetcher gjtf = new GraalJSTestFetcher("../../GraalVM-0.10/chakraTests/test/Array");
		tg = gjtf.fetch(); //FromDir("../../GraalVM-0.10/chakraTests/test/Array");
	}
	
	@Test
	public void TGCreation() {
		if(!tg.get(0).getGroupName().equals("Array")) {
			fail("Testgroup has wrong name");
		}
	}
	
	@Test
	public void TGnonEmpty() {
		if(tg.isEmpty()) {
			fail("No Testgroup created");
		}
	}

	@Test
	public void justOneTG() {
		if(tg.size() != 1) {
			fail("Too many testgroups created");
		}
	}
	
	@Test
	public void TGcontainsTests() {
		if(tg.get(0).getTests().size() == 0) {
			fail("testgroup contains no tests!");
		}
	}

	@Test
	public void TGtestnameOK() {
		String testName = tg.get(0).getTests().get(0).getFilename();
		if(!testName.equals("ArrayBtreeBadCodeGen.js")) {
			fail("First Test in Testgroup has wrong name: " + testName);
		}
	}
}
