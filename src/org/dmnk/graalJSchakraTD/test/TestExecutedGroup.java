package org.dmnk.graalJSchakraTD.test;

import java.util.LinkedList;
import java.util.List;

import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.test.TestGroup;

public class TestExecutedGroup extends TestGroup {
	private int passed =0, fail=0, warn=0, exception=0, crash=0, assertion=0, excluded=0;
	
	public TestExecutedGroup (String name) {
		super(name);
		this.testList = new LinkedList<Test>();
	}

	@Override
	public void addTest(Test test)  {
		super.addTest(test);
		
		switch (test.getFailReason()) {
		case ASSERTION:
			this.assertion ++;
			break;
		case CRASH:
			this.crash++;
			break;
		case EXCEPTION:
			this.exception++;
			break;
		case WARNING:
			this.warn++;
			break;
		default:
			if(test.executed()) {
				if(test.getReturncode() == 0) {
					this.passed ++;
				} else {
					this.fail++;
				}
			} else {
				this.excluded++;
			}
			
			break;
			//throw new FailReasonException("Not set!");			
		}
	}

}
