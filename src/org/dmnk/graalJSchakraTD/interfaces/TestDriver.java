package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.exceptions.TestException;

public interface TestDriver {
	public void process() throws TestException, ConfigurationException;

	public void export();
}
