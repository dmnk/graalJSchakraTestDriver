package org.dmnk.graalJSchakraTD.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.dmnk.graalJSchakraTD.classes.Configuration.ExecutableMode;
//import org.dmnk.graalJSchakraTD.classes.Configuration.HarnessMode;
import org.dmnk.graalJSchakraTD.exceptions.GraalJSTestException;
import org.dmnk.graalJSchakraTD.interfaces.TestExecutor;

public class GenericTestExecutor implements TestExecutor {
	private Configuration c;
	private File executable;
	
	GenericTestExecutor(Configuration c) throws GraalJSTestException {
		this.c = c;
		executable = new File(c.getExec());
		if(c.getExecMode() == ExecutableMode.DIRECT) {
			if(!executable.isFile()) {
				throw new GraalJSTestException("provided executable location doesn't point to executable js: \n\t"+executable);
			}
		}

		if(!executable.canExecute()) {
			throw new GraalJSTestException("can't execute the provided executable binary, check rights?");
		}	
	}
	
	@Override
	public TestOutput launch(File test) {
//		String cmdLine = graalJS + " " +test.getAbsolutePath();
 	    String line;
	    String sysOut = "";
	    String errOut = "";
	    int rc = -99;
	    
	    try {
	    	//TODO: use ProcessBuilder like described http://stackoverflow.com/questions/6811522/changing-the-working-directory-of-command-from-java
	    	ProcessBuilder pb;
//	    	if(c.getHarnessMode() == HarnessMode.PARAMETER) {
	    		pb = new ProcessBuilder(executable.getAbsolutePath(), c.getHarnessFile(), test.getAbsolutePath());
//	    	} else {
//	    		pb = new ProcessBuilder(executable.getAbsolutePath(), test.getAbsolutePath());
//	    	}
	    	
	    	pb.directory(test.getParentFile());
	    	Process p = pb.start();
//	        Process p = Runtime.getRuntime().exec(cmdLine);
	        BufferedReader input = new BufferedReader
	            (new InputStreamReader(p.getInputStream()));
	        BufferedReader error = new BufferedReader
        		(new InputStreamReader(p.getErrorStream()));
	        while ((line = input.readLine()) != null) {
	            sysOut += (line + '\n');
	        }
	        while ((line = error.readLine()) != null) {
	        	errOut += (line + '\n');
	        }
	        
	        //TODO: make timeout configurable
	        if(p.waitFor(5, TimeUnit.SECONDS)) {
	        	rc = p.exitValue();
	        } else {
	        	p.destroy();
	        	rc = -2;
	        }
	        	
//	        rc = p.exitValue();
	        input.close();
	        error.close();
	        }
	    catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    
	    return new TestOutput(rc, 
	    		Helper.removeTrailingNewline(sysOut), 
	    		Helper.removeTrailingNewline(errOut));
	}
}
