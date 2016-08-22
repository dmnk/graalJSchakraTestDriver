package org.dmnk.graalJSchakraTD.interfaces;

import java.util.List;

import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.exceptions.TestException;

public interface TestFetcher {

	List<TestGroup> fetch() throws ConfigurationException, TestException;

}
