package org.dmnk.graalJSchakraTD.interfaces;

import org.dmnk.graalJSchakraTD.classes.Configuration;
import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;

/**
 * provides / extends a {@link Configuration} instance for the TestDriver 
 * @author dominik
 *
 */
public interface ConfigurationProvider {
	
	public void workOn(Configuration c);
	
	public Configuration getConfig() throws ConfigurationException;
}
