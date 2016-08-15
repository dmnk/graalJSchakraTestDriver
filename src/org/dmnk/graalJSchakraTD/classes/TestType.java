package org.dmnk.graalJSchakraTD.classes;

import java.io.File;
import java.io.IOException;

import org.dmnk.graalJSchakraTD.enums.FailReason;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;


public enum TestType {
	BASELINE, PASSSTRING;
	
	public static FailReason evaluate(Test t, TestOutput to) {
		if(to.getErrOut().contains("Assert")) return FailReason.ASSERTION;
		if(to.getErrOut().length()>0) return FailReason.EXCEPTION;
		
		if(!to.getErrOut().isEmpty()) return FailReason.WARNING;
		
		return FailReason.OUTPUT;
		//if execution aborted:
//			return FailReason.CRASH;
	}
	
	public static boolean passed (Test t, TestOutput to) {
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
	
	
	public static ExecutedTest determineTestResult(Test t, TestOutput to) {
		//merge baseline and testfile
//		File mf = new File(t.getFilename());
		
		//execute with graal
//		TestOutput to = executeGraalJS(mf);
		
		//decide, based on TestOutput.rc, erroroutput and stdout if and why and where it failed
		if(TestType.passed(t, to)) {
			PassedTest pt = new GraalJSPassedTest(t, to);
			return pt;
		} else {
			//check failreason;
			String diff;
			FailedTest ft;
			FailReason fr = evaluate(t, to);
			switch(t.getTestType()) {
			case BASELINE:
				diff = DiffUtilsWrapper.getDiff(t, to);
				
				ft = new GraalJSFailedTest(t, to, fr, diff);
				
				return ft;
//				break;
			case PASSSTRING:
//				diff = DiffUtilsWrapper.getDiff("Passed", to.getStdOut());
				// no need to evaluate the exact diff to the pass-string, right?
				// but maybe the testtype should be visible in the result output?
				
				ft = new GraalJSFailedTest(t, to, fr);
				return ft;
//				break;
			default:
				return new GraalJSFailedTest(t, to, fr); 
				//TODO: (unknown testtype exception)  or -> blow up testtype to validate "itself" <-
			}			
		}
	}


}
