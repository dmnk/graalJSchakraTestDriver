package org.dmnk.graalJSchakraTD.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;

import org.dmnk.graalJSchakraTD.classes.test.GenericTestExecutedGroup;
import org.dmnk.graalJSchakraTD.classes.test.TestOutput;
import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.exceptions.TestException;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
import org.dmnk.graalJSchakraTD.interfaces.ListFetcher;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestEvaluator;
import org.dmnk.graalJSchakraTD.interfaces.TestExecutedGroup;
import org.dmnk.graalJSchakraTD.interfaces.TestFetcher;

/**
 * places the test-calls in a future list, gradually filled by the executorservices "call"-calls
 * @author dominik
 *
 */
public class TestDriverFutures extends TestDriverGeneric {
	private ExecutorService e;
	
	public TestDriverFutures(Configuration c, TestFetcher tf, ListFetcher lf, TestEvaluator te) {
		super(c, tf, lf, te);

		e = Executors.newFixedThreadPool(c.getMaxThreads());
	}
	
	@Override
	public void process() throws TestException, ConfigurationException {		
		//execute the enabled tests
		for(TestGroup tg : tests) {	
			if(tg == null) {
				continue;
				//actually, that shouldn't happen
				// but it does, and this keeps it running, without further negative affects
				// TODO: investigate where the null groups come from
			}
			System.out.println("\nGroup: "+tg.getGroupName());
			
			if(!groupActive(tg)) {//check if folder is black- / whitelisted
				//back to loop
				continue;
			}
			
			TestExecutedGroup etg = new GenericTestExecutedGroup(tg.getGroupName());
			executedTests.add(etg);
			Map<Test, Future<TestOutput>> lfto = new HashMap<Test, Future<TestOutput>>();
			
			for(Test t : tg.getTests()) {
				if(fileActive(tg, t)) {
					FutureTask<TestOutput> ft = new FutureTask<TestOutput>(new TestExecutorFuture(conf, t));
					e.execute(ft);
					lfto.put(t, ft);
				} else {
					//in exclusion list, just add as unexecuted test for completeness
					etg.addTest(t);
					System.out.print(" ");
				}
			}
			
			for(Entry<Test, Future<TestOutput>> ftoe : lfto.entrySet()) {
				Test t = ftoe.getKey();
				Future<TestOutput> fto = ftoe.getValue();
				
				try {
					TestOutput to = fto.get(conf.getTimeoutValue(), conf.getTimeoutUnit());
					ExecutedTest et = tEval.determineTestResult(t, to);
					
					etg.addTest(et);
					
					if(et instanceof PassedTest) {
						System.out.print(".");
					} else if (et instanceof FailedTest) {
						System.out.print("f");
					} 
				} catch (InterruptedException | ExecutionException | TimeoutException e1) {
					System.err.println("TestDriverFuture: Timeout at "+ t.getTestName());
					System.err.println("continuing after the process is stopped");
					fto.cancel(true);
					while(!fto.isCancelled()) {
						try {
							wait(100);
						} catch (InterruptedException e2) {
						}
					}
				}
			}
 		}
	}
}
