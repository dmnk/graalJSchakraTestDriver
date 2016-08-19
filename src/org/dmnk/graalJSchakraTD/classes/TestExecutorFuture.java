package org.dmnk.graalJSchakraTD.classes;

import java.util.concurrent.Callable;

import org.dmnk.graalJSchakraTD.classes.test.TestOutput;
import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.interfaces.Test;

/**
 * implements the {@link Callable} interface as extension of the {@link TestExecutorGeneric} class
 * therefore it can be submitted to an {@link ExecutorService} and run in parallel.
 * this is the more lightweight approach (seen from the code side), as it's just a wrapper to comply
 * with the Callable interface.
 * @see TestExecutorCallback
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
