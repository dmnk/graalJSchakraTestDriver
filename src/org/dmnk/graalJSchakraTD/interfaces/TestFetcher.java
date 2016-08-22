package org.dmnk.graalJSchakraTD.interfaces;

import java.util.List;

import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.exceptions.TestException;

/**
 * 
 * searches in the configured folder for files, which match the criteria to qualify as tests.
 * 
 * @author dominik
 *
 */
public interface TestFetcher {

	List<TestGroup> fetch() throws ConfigurationException, TestException;

}
