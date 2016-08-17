package org.dmnk.graalJSchakraTD.classes.test;

import java.util.LinkedList;
import java.util.List;

import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;

public class GenericTestGroup implements TestGroup {
	protected List<Test> testList;
	private String groupName;
	
	public GenericTestGroup (String name) {
		this.groupName = name;
		this.testList = new LinkedList<Test>();
	}

	@Override
	public String getGroupName() {
		return this.groupName;
	}

	@Override
	public List<Test> getTests() {
		return this.testList;
	}

	@Override
	public void addTest(Test test) {
		this.testList.add(test);
	}

}
