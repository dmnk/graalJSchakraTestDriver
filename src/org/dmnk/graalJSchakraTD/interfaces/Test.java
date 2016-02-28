package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.enums.TestType;

public interface Test {
	
	public String getFilename();
	
	/**
	 * 
	 * @return if Baseline File exists, the Filename
	 */
	public String getBaseline();
	
	public TestType getTestType();
	
}
