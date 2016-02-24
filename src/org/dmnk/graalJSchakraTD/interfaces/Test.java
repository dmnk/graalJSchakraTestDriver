package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.enums.FailReason;

public interface Test {
	public FailReason getFailReason();
	
	public String getFilename();
	
	/**
	 * 
	 * @return if Baseline File exists, the Filename
	 */
	public String getBaseline();
	
	public int getReturncode();
	
	public boolean getFailed();
	
	/**
	 * 
	 * @return true if the test was executed
	 */
	public boolean executed();
	
}
