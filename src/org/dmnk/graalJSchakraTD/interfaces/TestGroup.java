package org.dmnk.graalJSchakraTD.interfaces;

import java.util.List;

public interface TestGroup {

	public String getGroupName ();
	
	public List<Test> getTests();
	public void addTest (Test test);
}
