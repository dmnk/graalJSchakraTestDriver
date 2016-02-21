package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.enums.FailReason;

public interface Test {
	public FailReason getFailReason();
	
	public String getFilename();
	
	public int getReturncode();
	
	/**
	 * 
	 * @return true if the test was executed
	 */
	public boolean executed();
	
}
