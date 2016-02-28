package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.enums.FailReason;

public interface FailedTest extends ExecutedTest {

	public FailReason getFailReason();
}
