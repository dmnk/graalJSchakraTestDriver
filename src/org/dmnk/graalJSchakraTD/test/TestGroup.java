package org.dmnk.graalJSchakraTD.test;

import java.util.LinkedList;
import java.util.List;

import org.dmnk.graalJSchakraTD.interfaces.Test;

public class TestGroup implements org.dmnk.graalJSchakraTD.interfaces.TestGroup {
	protected List<Test> testList;
	private String groupName;
	
	public TestGroup (String name) {
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
