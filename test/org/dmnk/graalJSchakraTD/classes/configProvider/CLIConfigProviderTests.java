package org.dmnk.graalJSchakraTD.classes.configProvider;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.dmnk.graalJSchakraTD.classes.Configuration;
import org.dmnk.graalJSchakraTD.classes.Configuration.ExecutableMode;
import org.dmnk.graalJSchakraTD.classes.Configuration.HarnessMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CLIConfigProviderTests {

	private CLIConfigProvider cp;
	
	// http://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@After
	public void tearDown() throws Exception {
		cp = null;
	}

	@Test
	public void testHelpParameter() throws Exception {
		String[] args = new String[1];
		args[0] = new String("-h");
		
		cp = new CLIConfigProvider(args);
		cp.getConfig();

		assertTrue(outContent.toString().contains("-- usage: --"));
	}

	@Test
	public void testModeWhiteExists() throws Exception {
		String[] args = new String[2];
		args[0] = new String("-m=w");
		args[1] = new String("-l=someList.txt");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();
		assertEquals(c.getWhiteList(), "someList.txt");
	}
	
	@Test(expected=Exception.class)
	public void testModeBlackDoesntExist() throws Exception {
		String[] args = new String[2];
		args[0] = new String("-m=w");
		args[1] = new String("-l=someList.txt");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();
		c.getBlackList();
	}
	
	@Test
	public void testModeBlackExists() throws Exception {
		String[] args = new String[2];
		args[0] = new String("-m=b");
		args[1] = new String("-l=someList.txt");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();
		assertEquals(c.getBlackList(), "someList.txt");
	}
	
	@Test(expected=Exception.class)
	public void testModeWhiteDoesntExist() throws Exception {
		String[] args = new String[2];
		args[0] = new String("-m=b");
		args[1] = new String("-l=someList.txt");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();
		c.getWhiteList();
	}
	
	@Test
	public void testAddOneExport() throws Exception {
		String[] args = new String[1];
		args[0] = new String("-exp=text#/tmp/result.txt");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();
		assertTrue(c.checkExport("text"));
		assertEquals(c.getExport("text"), "/tmp/result.txt");
	}
	
	@Test
	public void testAddTwoExports() throws Exception {
		String[] args = new String[2];
		args[0] = new String("-exp=text#/tmp/result.txt");
		args[1] = new String("-exp=html#/tmp/result.html");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();
		assertTrue(c.checkExport("text"));
		assertEquals(c.getExport("text"), "/tmp/result.txt");
		assertTrue(c.checkExport("html"));
		assertEquals(c.getExport("html"), "/tmp/result.html");
	}
		
	@Test
	public void testGetGraalJSexecModeDirect() throws Exception {
		String[] args = new String[2];
		args[0] = new String("-execMode=d");
		args[1] = new String("-exec=../../GraalVM-0.10/bin/js");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();
		assertEquals(c.getExecMode(), ExecutableMode.DIRECT);
		assertEquals(c.getExec(), "../../GraalVM-0.10/bin/js");
	}
	
	@Test
	public void testGetGraalJSexecModeIndirect() throws Exception {
		String[] args = new String[2];
		args[0] = new String("-execMode=i");
		args[1] = new String("-exec=jjs");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();
		assertEquals(ExecutableMode.INDIRECT, c.getExecMode());
		assertEquals(c.getExec(), "jjs");
	}
	
	@Test(expected=Exception.class)
	public void testGetGraalJSexecModeUnknown() throws Exception {
		String[] args = new String[2];
		args[0] = new String("-execMode=b");
		args[1] = new String("-exec=jjs");
		
		cp = new CLIConfigProvider(args);
		cp.getConfig();
	}
	
	@Test
	public void testGrayList() throws Exception {
		String[] args = new String[1];
		args[0] = new String("-g=someList.txt");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();
		assertEquals(c.getGrayList(), "someList.txt");
	}

	@Test
	public void testHarnessModeInject() throws Exception {
		String[] args = new String[2];
		args[0] = new String("-hnsMode=i");
		args[1] = new String("-hnsFile=ChakraHarness.js");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();
		assertEquals(HarnessMode.INCLUDE, c.getHarnessMode());
		assertEquals(c.getHarnessFile(), "ChakraHarness.js");
	}
	
	@Test
	public void testHarnessModeParameter() throws Exception {
		String[] args = new String[2];
		args[0] = new String("-hnsMode=p");
		args[1] = new String("-hnsFile=ChakraHarness.js");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();
		assertEquals(HarnessMode.PARAMETER, c.getHarnessMode());
		assertEquals(c.getHarnessFile(), "ChakraHarness.js");
	}
	
	@Test(expected=Exception.class)
	public void testHarnessModeUnknown() throws Exception {
		String[] args = new String[2];
		args[0] = new String("-hnsMode=unknown");
		args[1] = new String("-hnsFile=ChakraHarness.js");
		
		cp = new CLIConfigProvider(args);
		cp.getConfig();
	}

	@Test //TODO
	public void testHarnessPredeliveredFile() throws Exception {
		String[] args = new String[2];
		args[0] = new String("-hnsMode=d");
		args[1] = new String("-exec=jjs");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();
		assertEquals(ExecutableMode.INDIRECT, c.getExecMode());
		assertEquals(c.getExec(), "jjs");
	}
	
	@Test
	public void testTestsPath() throws Exception {
		String[] args = new String[1];
		args[0] = new String("-t=/somewhere/the/tests/must/lie_;)");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();
		assertEquals(c.getTestsPath(), "/somewhere/the/tests/must/lie_;)");
	}
	
}
