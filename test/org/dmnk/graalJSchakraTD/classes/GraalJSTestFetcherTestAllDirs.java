package org.dmnk.graalJSchakraTD.classes;

import static org.junit.Assert.*;

import java.util.List;

import org.dmnk.graalJSchakraTD.classes.TestFetcherGeneric;
import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.exceptions.TestException;
import org.dmnk.graalJSchakraTD.interfaces.TestFetcher;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;
import org.junit.Before;
import org.junit.Test;

public class GraalJSTestFetcherTestAllDirs {
	private List<TestGroup> tg;
	
	@Before
	public void setUp() throws ConfigurationException, TestException {
		TestFetcher gjtf = new TestFetcherGeneric("../../GraalVM-0.10/chakraTests/test/");
		tg = gjtf.fetch(); //FromDir("../../GraalVM-0.10/chakraTests/test/");
	}
	
	@Test
	public void TGCreation() {
		String tgName = tg.get(0).getGroupName();
		
		if(!tgName.equals("ASMJSParser")) {
			fail("First Testgroup has wrong name: " + tgName);
		}
	}
	
	@Test
	public void TGnonEmpty() {
		
		if(tg.isEmpty()) {
			fail("No Testgroup created");
		}
	}
	

	@Test
	public void TGallGroups() {
		
		if(tg.size() != 60) {
			fail("Incorrect amount of TestGroups: " + tg.size());
		}
	}

	@Test
	public void justOneTG() {
		if(tg.size() == 1) {
			fail("Just one TG created");
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
		if(!testName.equals("UnaryPos.js")) {
			fail("First Test in Testgroup has wrong name: " + testName);
		}
	}

}
