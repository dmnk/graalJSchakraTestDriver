package org.dmnk.graalJSchakraTD.classes.test;

import java.io.File;

import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.Test;

public class GenericTest implements Test {
	private File testFile;
	private TestType testType;
	
	public GenericTest(String file, TestType tt) {
		this.testFile = new File(file);
		this.testType = tt;
	}
	
	public GenericTest(Test t) {
		this.testFile = new File(t.getFilename());
		this.testType = t.getTestType();
	}
	
	@Override
	public String getFilename() {
		return testFile.getAbsolutePath(); //.concat(".js");
	}
	
	@Override
	public String getTestName() {
		return testFile.getName();
	}
	
	@Override
	public String getBaseline() {
		if(testType == TestType.BASELINE){
			//TODO: might be externalized, or at least done in a more secure way
			// is needed in the testfetcher class too
			return testFile.getAbsolutePath().replaceAll(".js", ".baseline");
//			return testFile.getAbsolutePath().substring(-3).concat(".baseline");
		} else return "";
	}
	
	@Override
	public TestType getTestType() {
		return this.testType;
	}

}
