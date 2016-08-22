package org.dmnk.graalJSchakraTD.interfaces;

/**
 * test that has been processed by the TestDriver / executable under test
 * @author dominik
 *
 */
public abstract interface ExecutedTest extends Test {
	
	public int getReturncode();
	public String getOutput();
	public String getErrorOutput();
}
