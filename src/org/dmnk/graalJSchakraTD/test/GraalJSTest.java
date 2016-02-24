package org.dmnk.graalJSchakraTD.test;

import org.dmnk.graalJSchakraTD.enums.FailReason;

public class GraalJSTest implements org.dmnk.graalJSchakraTD.interfaces.Test {
	private FailReason fr;
	private String testName;
	private int retCode;
	private boolean executed;
	private boolean failed;
	private String output;
	
	public GraalJSTest(String testname) {
		this.testName = testname;
		this.executed = false;
	}

	@Override
	public FailReason getFailReason() {
		return this.fr;
	}

	@Override
	public String getFilename() {
		return this.testName; //.concat(".js");
	}

	public String getBaseline() {
		//TODO if file exists
		return this.testName.substring(-3).concat(".baseline");
	}

	@Override
	public int getReturncode() {
		return this.retCode;
	}

	@Override
	public boolean executed() {
		return this.executed;
	}
	
	protected void setReturncode(int rc) {
		this.retCode = rc;
		this.executed = true;
	}
	
	protected void setOutput (String output) {
		this.output = output;
	}
	
	public String getOutput () {
		return this.output;
	}
	
	public boolean getFailed() {
		return this.failed;
	}

}
