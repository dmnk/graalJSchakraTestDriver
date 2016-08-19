package org.dmnk.graalJSchakraTD.classes;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.dmnk.graalJSchakraTD.classes.test.GenericTestExecutedGroup;
import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.exceptions.TestException;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.ListFetcher;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestEvaluator;
import org.dmnk.graalJSchakraTD.interfaces.TestExecutedGroup;
import org.dmnk.graalJSchakraTD.interfaces.TestFetcher;

/**
 * extends the {@link TestDriverGeneric} in a way that the testexecutor gets a list it can
 * asynchronously put the results, as they come in, into.
 * therefore just the process function is overridden.
 * 
 * @author dominik
 *
 */
public class TestDriverCallback extends TestDriverGeneric {
	
	private final ScheduledExecutorService exs;
	final private Object lock;
	private AtomicInteger countDownLatch;
	
	public TestDriverCallback (Configuration c, TestFetcher tf, ListFetcher lf, TestEvaluator te) throws ConfigurationException {
		super(c, tf, lf, te);
		
		lock = new Object();
		executedTests = new LinkedList<TestExecutedGroup>();
		exs = Executors.newScheduledThreadPool(c.getMaxThreads());
		countDownLatch = new AtomicInteger(0);
	}

	@Override
	public void process() throws TestException, ConfigurationException {
		//execute the enabled tests
		for(TestGroup tg : tests) {			
			if(tg == null) {
				continue;
				//actually, that shouldn't happen
				// but it does, however this keeps it running
				// TODO: investigate where the null groups come from
			}
			System.out.println("\nGroup: "+tg.getGroupName());
			
			//check if folder is black- / whitelisted
			if(!groupActive(tg)) {
				//back to loop
				continue;
			}
			
//			final ConcurrentHashMap<String, ExecutedTest> chmExecTests = new ConcurrentHashMap<String, ExecutedTest>();
			final List<ExecutedTest> cowList = new CopyOnWriteArrayList<ExecutedTest>();

			TestExecutedGroup etg = new GenericTestExecutedGroup(tg.getGroupName());
			
			for(Test t : tg.getTests()) {
				//check if file is graylisted
				if(fileActive(tg, t)) {
					//handler is just needed for the timeout, therefore no spec. type
					final Future<?> handler = exs.submit(new TestExecutorCallback(conf, t, tEval, countDownLatch, lock, cowList));
					//add a timeout runnable, killing the previous testexecutor
					exs.schedule(new Runnable(){
					     public void run(){
					         handler.cancel(true);
						     } 
					 }, 20, TimeUnit.SECONDS);
				} else {
					//in exclusion list, just add as unexecuted test for completeness
					etg.addTest(t);
					System.out.print(" ");
				}
			}
			//after each testgroup, wait for all tests to finalize and move results from hashmap into list
			synchronized(lock) {
				while(countDownLatch.get() != 0) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				for(ExecutedTest entry : cowList) {
					etg.addTest(entry);
				}
			}

			executedTests.add(etg);			
 		}
		
	}
}
