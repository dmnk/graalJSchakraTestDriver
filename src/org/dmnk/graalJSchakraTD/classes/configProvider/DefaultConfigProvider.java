package org.dmnk.graalJSchakraTD.classes.configProvider;

import org.dmnk.graalJSchakraTD.classes.Configuration;
import org.dmnk.graalJSchakraTD.interfaces.ConfigurationProviderInterface;

public class DefaultConfigProvider implements ConfigurationProviderInterface {

	private Configuration c;
	
	public DefaultConfigProvider() {
		c = new Configuration();
	}
	
	public Configuration getConfig() throws Exception {
		setDefaults();
		return c;
	}

	@Override
	public void workOn(Configuration conf) {
		c = conf;
	}
	
	private void setDefaults() {
		c.setGraalJSexec(Configuration.ExecutableMode.DIRECT, "../../GraalVM-0.10/bin/js");
		c.setTestsPath("../../GraalVM-0.10/chakraTests/test/");
		c.addExport("html", "./data/htmlResult.html");
		c.addExport("csv", "./data/FailPass.csv");
	}
}
