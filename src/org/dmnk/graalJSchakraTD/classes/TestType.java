package org.dmnk.graalJSchakraTD.classes;

import java.io.File;
import java.io.IOException;

import org.dmnk.graalJSchakraTD.enums.FailReason;
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
	



}
