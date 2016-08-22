package org.dmnk.graalJSchakraTD.interfaces;

/**
 * extends the {@link TestGroup} interface, by providing statistics about the tests managed in the group
 * @author dominik
 *
 */
public interface TestExecutedGroup extends TestGroup {

	int getOutput();

	int getPassed();

	int getFailed();

	int getException();

	int getExcluded();

	int getCrashed();

	int getTotal();

	int getError();

}
