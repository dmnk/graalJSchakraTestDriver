package org.dmnk.graalJSchakraTD.test;

import java.util.LinkedList;

import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.test.GraalJSTestGroup;

public class TestExecutedGroup extends GraalJSTestGroup {
	private int passed =0, failed=0, warn=0, exception=0, crash=0, assertion=0, excluded=0;
	
	public TestExecutedGroup (String name) {
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
				default:
					System.err.println("who invented a new failreason and didn't update the statistics in TestExecutedGroup?");
					//TODO: exception instead of error
				}
			} else {
				System.err.println("how could that happen?");
				//TODO: evaluate the need for an exception
			}
		} else {
			excluded++;
		}
	}
	
	public int getPassed() {
		return this.passed;
	}
	
	public int getFailed() {
		return this.failed;
	}

	public int getWarnings() {
		return this.warn;
	}
	
	public int getException() {
		return this.exception;
	}
	
	public int getExcluded() {
		return this.excluded;
	}
	
	public int getCrashed() {
		return this.crash;
	}
	
	public int getAssert() {
		return this.assertion;
	}
}
