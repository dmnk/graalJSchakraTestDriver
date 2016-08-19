package org.dmnk.graalJSchakraTD.classes.test;

import java.util.LinkedList;

import org.dmnk.graalJSchakraTD.classes.test.GenericTestGroup;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestExecutedGroup;

/**
 * extends the {@link GenericTestGroup} by implementing the {@link TestExecutedGroup} interface.
 * updates its statistic about amount of tests by failtype / pass on the fly when adding a new test.
 * 
 * @author Dominik
 *
 */
public class GenericTestExecutedGroup extends GenericTestGroup implements TestExecutedGroup {
	private int passed =0, failed=0, warn=0, exception=0, crash=0, assertion=0, excluded=0, output=0;
	
	public GenericTestExecutedGroup (String name) {
		super(name);
		this.testList = new LinkedList<Test>();
	}

	@Override
	public void addTest(Test test)  {
		super.addTest(test);
		
		if(test instanceof ExecutedTest) {
			if (test instanceof PassedTest) {
				passed++;
			} else if (test instanceof FailedTest) {
				FailedTest ft = (FailedTest) test;
				failed++;
				
				switch (ft.getFailReason()) {
				case ASSERTION:
					assertion ++;
					break;
				case CRASH:
					crash++;
					break;
				case EXCEPTION:
					exception++;
					break;
				case WARNING:
					warn++;
					break;
				case OUTPUT:
					output++;
					break;
				default:
					System.err.println("who invented a new failreason and didn't update the statistics in TestExecutedGroup?");
					//TODO: exception instead of error
				}
			} else {
				System.err.println("generic-test-executor: a test of type executed but neither failed nor passed, how could that happen?");
			}
		} else {
			excluded++;
		}
	}
	
	//TODO: status enum, extending failtype enum??
	@Override
	public int getPassed() {
		return this.passed;
	}

	@Override
	public int getFailed() {
		return this.failed;
	}

	@Override
	public int getWarnings() {
		return this.warn;
	}

	@Override
	public int getException() {
		return this.exception;
	}

	@Override
	public int getExcluded() {
		return this.excluded;
	}

	@Override
	public int getCrashed() {
		return this.crash;
	}

	@Override
	public int getAssert() {
		return this.assertion;
	}

	@Override
	public int getOutput() {
		return output;
	}
	
	@Override
	public int getTotal() {
		return super.getTests().size();
	}
}
