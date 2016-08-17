package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.classes.Configuration;
import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;

public interface ConfigurationProvider {
	
	public void workOn(Configuration c);
	
	public Configuration getConfig() throws ConfigurationException;
}
