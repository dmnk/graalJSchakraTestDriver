package org.dmnk.graalJSchakraTD.test;

public class chakraTestRunner {
	private String graalPath;
	private String chakraPath;
	private ResultExporter resExp;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		chakraTestRunner ctr = new chakraTestRunner();
		ctr.setGraalPath("./../bin/js");
		ctr.setChakraPath("./chakraTests/test");
		
		ctr.run();
		ctr.setResultExporter(new BootstrappedHTMLExporter());
	}

	private String getGraalPath() {
		return graalPath;
	}

	private void setGraalPath(String graalPath) {
		this.graalPath = graalPath;
	}

	private String getChakraPath() {
		return chakraPath;
	}

	private void setChakraPath(String chakraPath) {
		this.chakraPath = chakraPath;
	}

	private void run() {
		
	}
	
}
