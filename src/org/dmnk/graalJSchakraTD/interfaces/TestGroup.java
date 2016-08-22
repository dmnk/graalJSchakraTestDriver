package org.dmnk.graalJSchakraTD.interfaces;

import java.util.List;

import org.dmnk.graalJSchakraTD.exceptions.TestException;

/**
 * all tests are hold either by a TestGroup, or the extending TestExecutedGroup.
 * @author dominik
 *
 */
public interface TestGroup {

	public String getGroupName ();
	
	public List<Test> getTests();
	public void addTest (Test test) throws TestException;
}
