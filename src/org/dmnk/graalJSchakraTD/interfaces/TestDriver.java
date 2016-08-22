package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.exceptions.TestException;

/**
 * the classes implementing this interface got the most work. they cooperate with the classes implementing {@link ListFetcher},
 * {@link TestFetcher}, {@link TestDriver} and the {@link TestEvaluator} interfaces, to get, in/exclude and execute the tests, and with 
 * {@link ResultExporter} implementations to produce some meaningful output.
 * this is where everything comes together.
 * @author dominik
 *
 */
public interface TestDriver {
	public void process() throws TestException, ConfigurationException;

	public void export();
}
