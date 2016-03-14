package org.dmnk.graalJSchakraTD.test;

import java.io.File;

import org.dmnk.graalJSchakraTD.interfaces.Test;

public class GraalJSTest implements Test {
	private File testFile;
	private TestType testType;
	
	public GraalJSTest(String file, TestType tt) {
		this.testFile = new File(file);
		this.testType = tt;
	}
	
	public GraalJSTest(Test t) {
		this.testFile = new File(t.getFilename());
		this.testType = t.getTestType();
	}
	
	@Override
	public String getFilename() {
		return this.testFile.getAbsolutePath(); //.concat(".js");
	}
	
	@Override
	public String getTestName() {
		return testFile.getName();
	}

	public String getBaseline() {
		if(testType == TestType.BASELINE){
			return testFile.getAbsolutePath().replaceAll(".js", ".baseline");
//			return testFile.getAbsolutePath().substring(-3).concat(".baseline");
		} else return "";
	}
	
	@Override
	public TestType getTestType() {
		return this.testType;
	}

}
