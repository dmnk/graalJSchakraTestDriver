package org.dmnk.graalJSchakraTD.classes.configProvider;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.dmnk.graalJSchakraTD.classes.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Categories.ExcludeCategory;

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
	public void testHelpParameter() {
		String[] args = new String[1];
		args[0] = new String("-h");
		
		cp = new CLIConfigProvider(args);
		Configuration c = cp.getConfig();

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
}
