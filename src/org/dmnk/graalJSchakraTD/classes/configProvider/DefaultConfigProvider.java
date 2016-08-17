package org.dmnk.graalJSchakraTD.classes.configProvider;

import java.util.concurrent.TimeUnit;

import org.dmnk.graalJSchakraTD.classes.Configuration;
import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.interfaces.ConfigurationProvider;

public class DefaultConfigProvider implements ConfigurationProvider {

	private Configuration c;
	
	public DefaultConfigProvider() {
		c = new Configuration();
	}
	
	@Override
	public Configuration getConfig() throws ConfigurationException {
		setDefaults();
		return c;
	}

	@Override
	public void workOn(Configuration conf) {
		c = conf;
	}
	
	private void setDefaults() {
		c.setExec(Configuration.ExecutableMode.DIRECT, "../../GraalVM-0.10/bin/js");
		c.setTestsPath("../../GraalVM-0.10/chakraTests/test/");
		c.addExport("html", "./data/htmlResult.html");
		c.addExport("csv", "./data/FailPass.csv");
		//spare one processor 
		c.setMaxThreads(Integer.max(Runtime.getRuntime().availableProcessors()-1, 1));
		c.setTimeoutUnit(TimeUnit.MINUTES);
		c.setTimeoutValue(1);
	}
}
