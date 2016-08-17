package org.dmnk.graalJSchakraTD.classes;

import java.util.concurrent.Callable;

import org.dmnk.graalJSchakraTD.classes.test.TestOutput;
import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.interfaces.Test;

/**
 * a wrapper for the generic test executor to match the callable interface - for the "future" testdriver
 * @author dominik
 *
 */
public class TestExecutorFuture extends TestExecutorGeneric implements Callable<TestOutput> {
	private final Test t;
	
	/**
	 * 
	 * @param c
	 * @param t
	 * @throws ConfigurationException
	 */
	TestExecutorFuture(Configuration c, Test t) 
			throws ConfigurationException  {
		super(c);
		this.t = t;
		
	}
	
	@Override
	public TestOutput call() throws Exception {
		return launch(t);
	}
}
