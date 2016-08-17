package org.dmnk.graalJSchakraTD.classes;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.dmnk.graalJSchakraTD.classes.test.TestOutput;
import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestEvaluator;

public class TestExecutorCallback extends TestExecutorGeneric implements Runnable {
	private final List<ExecutedTest> executedTests;
	private final AtomicInteger countDownLatch;
	private final Object lock;
	private final Test t;
	private final TestEvaluator tEval;
	
	/**
	 * 
	 * @param c
	 * @param t
	 * @param countDownLatch
	 * @param lock
	 * @param executedTests has to be a concurrent aware implementation, eg copyonwrite
	 * @throws ConfigurationException
	 */
	TestExecutorCallback(Configuration c, Test t, TestEvaluator te, AtomicInteger countDownLatch, Object lock, List<ExecutedTest> executedTests) 
			throws ConfigurationException  {
		super(c);
		this.t = t;
		this.countDownLatch = countDownLatch;
		this.countDownLatch.incrementAndGet();
		this.lock = lock;
		this.executedTests = executedTests;
		tEval = te;
	}
	
	public void run() {
		TestOutput to = launch(t);
		ExecutedTest et = tEval.determineTestResult(t, to);

		executedTests.add(et);

		if(et instanceof PassedTest) {
			System.out.print(".");
		} else if (et instanceof FailedTest) {
			System.out.print("f");
		} 

		countDownLatch.decrementAndGet();
		
		synchronized(lock) {
			lock.notifyAll();
		}
	}
}
