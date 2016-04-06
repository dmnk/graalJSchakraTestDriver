package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.classes.TestType;

public interface Test {
	
	public String getFilename();
	
	public String getTestName();
	
	/**
	 * 
	 * @return if Baseline File exists, the Filename
	 */
	public String getBaseline();
	
	public TestType getTestType();
	
}
