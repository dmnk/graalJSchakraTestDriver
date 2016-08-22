package org.dmnk.graalJSchakraTD.classes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HelperTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNumberAfterStringSuccess() {
		String hay = "ReferenceError: /home/dominik/workspace/graalJSchakraTestDriver/../../GraalVM-0.10/chakraTests/test/Closures/bug_OS_2525694.js:9:6 Invalid left hand side for assignment";
		String needle = "bug_OS_2525694.js";
		char sep = ':';
		
		int result = Helper.numberAfterString(hay, needle, sep);
		assertEquals(9, result);
	}
	
	@Test
	public void testNumberAfterStringMultiline() {
		String hay = "TypeError: Cannot read property 'obj' of undefined\n	at  (/home/dominik/workspace/graalJSchakraTestDriver/../../GraalVM-0.10/chakraTests/test/Basics/cross_site_accessor_main.js:8:17)";
		String needle = "cross_site_accessor_main.js";
		char sep = ':';
		
		int result = Helper.numberAfterString(hay, needle, sep);
		assertEquals(8, result);
	}
	
}
