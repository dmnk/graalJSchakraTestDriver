package org.dmnk.graalJSchakraTD.interfaces;

public abstract interface ExecutedTest extends Test {
	
	public int getReturncode();
	public String getOutput();
	public String getErrorOutput();
}
