package org.dmnk.graalJSchakraTD.interfaces;

public interface TestExecutedGroup extends TestGroup {

	int getOutput();

	int getPassed();

	int getFailed();

	int getWarnings();

	int getException();

	int getExcluded();

	int getCrashed();

	int getAssert();

	int getTotal();

}
