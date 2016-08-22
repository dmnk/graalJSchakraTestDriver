package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.enums.TestType;

/**
 * the base-case for all tests.
 * 
 * @author dominik
 *
 */
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
