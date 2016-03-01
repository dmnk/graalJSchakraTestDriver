package org.dmnk.graalJSchakraTD.test;

import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.Test;

public class GraalJSTest implements Test {
	private String testName;
	private TestType testType;
	
	public GraalJSTest(String testname, TestType tt) {
		this.testName = testname;
		this.testType = tt;
	}
	
	public GraalJSTest(Test t) {
		this.testName = t.getFilename();
		this.testType = t.getTestType();
	}
	
	@Override
	public String getFilename() {
		return this.testName; //.concat(".js");
	}

	public String getBaseline() {
		if(testType == TestType.BASELINE){
			return this.testName.substring(-3).concat(".baseline");
		} else return "";
	}
	
	@Override
	public TestType getTestType() {
		return this.testType;
	}

}
