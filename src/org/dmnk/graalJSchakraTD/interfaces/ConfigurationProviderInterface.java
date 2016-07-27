package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.classes.Configuration;

public interface ConfigurationProviderInterface {
	
	public void workOn(Configuration c);
	
	public Configuration getConfig() throws Exception;
}
