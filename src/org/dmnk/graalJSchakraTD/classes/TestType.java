package org.dmnk.graalJSchakraTD.classes;

import java.io.File;

import org.dmnk.graalJSchakraTD.enums.FailReason;
import org.dmnk.graalJSchakraTD.interfaces.Test;


public enum TestType {
	BASELINE, PASSSTRING;
	
	public static FailReason evaluate(Test t, TestOutput to) {
		if(to.getErrOut().contains("Assert")) return FailReason.ASSERTION;
		if(!to.getErrOut().isEmpty()) return FailReason.WARNING;
		
		return FailReason.OUTPUT;
//		switch(t.getTestType()) {
//		case BASELINE:
//			return FailReason.OUTPUT;
//		case PASSSTRING:
//			if()
//			return FailReason.CRASH;
//		default:
//			return FailReason.OUTPUT;
//		}
	}
	
	public static boolean passed (Test t, TestOutput to) {
		if(to.getReturnCode() != 0) {
			return false;
		} else if (!to.getErrOut().isEmpty()) {
			return false;
		} else {
			switch(t.getTestType()) {
			case BASELINE: 
				return Helper.simpleBaselineCompare(new File(t.getBaseline()), to.getStdOut());
				//TODO: checks for fail/pass baseline testtype 
				//return false;
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
	



}
