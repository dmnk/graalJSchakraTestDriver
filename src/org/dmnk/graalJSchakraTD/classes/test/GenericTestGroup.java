package org.dmnk.graalJSchakraTD.classes.test;

import java.util.LinkedList;
import java.util.List;

import org.dmnk.graalJSchakraTD.exceptions.TestException;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;

/**
 * represents a folder in the test root directory. all the tests found
 * by the {@link TestFetcher} during setup in such a folder
 * are added to a group.
 * 
 * implements {@link TestGroup}
 * 
 * @author Dominik
 *
 */
public class GenericTestGroup implements TestGroup {
	protected List<Test> testList;
	private String groupName;
	
	public GenericTestGroup (String name) {
		groupName = name;
		testList = new LinkedList<Test>();
	}

	@Override
	public String getGroupName() {
		return groupName;
	}

	@Override
	public List<Test> getTests() {
		return testList;
	}

	@Override
	public void addTest(Test test) throws TestException {
		testList.add(test);
	}

}
