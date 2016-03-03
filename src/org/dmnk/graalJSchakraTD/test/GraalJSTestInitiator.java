package org.dmnk.graalJSchakraTD.test;

import java.io.File;

import org.dmnk.graalJSchakraTD.Exceptions.GraalJSTestException;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestInitiator;

public class GraalJSTestInitiator implements TestInitiator {

	private File graalJS;
	
	public GraalJSTestInitiator (String graalExecutablePath) throws GraalJSTestException {
		graalJS = new File(graalExecutablePath);
		
		if(!graalJS.isFile()) {
			throw new GraalJSTestException("provided graalJS location doesn't point to executable js: \n\t"+graalExecutablePath);
		}
		if(!graalJS.canExecute()) {
			throw new GraalJSTestException("can't execute the provided graalJS binary, check rights?");
		}
	}
	
	@Override
	public Test runTest(Test t) {
		// TODO Auto-generated method stub
		return null;
	}

}
