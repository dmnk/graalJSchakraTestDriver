package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.enums.FailReason;

/**
 * an {@link ExecutedTest} that failed because of {@link FailReason}
 * @author dominik
 *
 */
public interface FailedTest extends ExecutedTest {

	public FailReason getFailReason();
	public String getDiff();
	public boolean diffIsSet();
}
