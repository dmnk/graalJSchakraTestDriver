package org.dmnk.graalJSchakraTD.classes.test;

import java.io.File;
import java.io.IOException;

import org.dmnk.graalJSchakraTD.classes.DiffUtilsWrapper;
import org.dmnk.graalJSchakraTD.classes.Helper;
import org.dmnk.graalJSchakraTD.enums.FailReason;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestEvaluator;

public class GenericTestEvaluator implements TestEvaluator {
	
	@Override
	public FailReason evaluate(Test t, TestOutput to) {
		if(to.getErrOut().contains("Assert") || to.getStdOut().contains("Assert")) return FailReason.ASSERTION;
		if(to.getErrOut().length()>0) return FailReason.EXCEPTION;
		
		if(!to.getErrOut().isEmpty()) return FailReason.WARNING;
		
		return FailReason.OUTPUT;
		//if execution aborted:
//			return FailReason.CRASH;
	}
	
	@Override
	public boolean passed (Test t, TestOutput to) {
		if(to.getReturnCode() != 0) {
			return false;
		} else if (!to.getErrOut().isEmpty()) {
			return false;
		} else {
			switch(t.getTestType()) {
			case BASELINE: 
				try {
					return Helper.simpleBaselineCompare(new File(t.getBaseline()), to.getStdOut());
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("TT: baseline file not found: "+t.getBaseline());
					return false;
				}
			case PASSSTRING:
				if(to.getStdOut().toLowerCase().equals("passed") 
						|| to.getStdOut().toLowerCase().equals("pass")) {
					return true;
				} else {
					return false;
				}
			default: System.err.println("un-known / set testtype !"); 
				return false;
			}
		}
	}
	
	@Override
	public ExecutedTest determineTestResult(Test t, TestOutput to) {
		//merge baseline and testfile
//		File mf = new File(t.getFilename());
		
		//execute with graal
//		TestOutput to = executeGraalJS(mf);
		
		//decide, based on TestOutput.rc, erroroutput and stdout if and why and where it failed
		if(passed(t, to)) {
			PassedTest pt = new GenericPassedTest(t, to);
			return pt;
		} else {
			//check failreason;
			String diff;
			FailedTest ft;
			FailReason fr = evaluate(t, to);
			switch(t.getTestType()) {
			case BASELINE:
				diff = DiffUtilsWrapper.getDiff(t, to);
				
				ft = new GenericFailedTest(t, to, fr, diff);
				
				return ft;
//				break;
			case PASSSTRING:
//				diff = DiffUtilsWrapper.getDiff("Passed", to.getStdOut());
				// no need to evaluate the exact diff to the pass-string, right?
				// but maybe the testtype should be visible in the result output?
				
				ft = new GenericFailedTest(t, to, fr);
				return ft;
//				break;
			default:
				return new GenericFailedTest(t, to, fr); 
				//TODO: (unknown testtype exception)  or -> blow up testtype to validate "itself" <-
			}			
		}
	}
}
