package org.dmnk.graalJSchakraTD.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import org.dmnk.graalJSchakraTD.classes.Configuration.ExecutableMode;
import org.dmnk.graalJSchakraTD.classes.test.TestOutput;
import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestExecutor;

/**
 * standard {@link TestExecutor} implementation, processing all the tests in sequence.
 * @author dominik
 *
 */
public class TestExecutorGeneric implements TestExecutor {
	protected Configuration c;
	protected File executable;
	
	TestExecutorGeneric(Configuration c) throws ConfigurationException {
		this.c = c;
		
		executable = new File(c.getExec());
		if(c.getExecMode() == ExecutableMode.DIRECT) {
			if(!executable.isFile()) {
				throw new ConfigurationException("provided executable location doesn't point to executable js: \n\t"+executable);
			}
		}

		if(!executable.canExecute()) {
			throw new ConfigurationException("can't execute the provided executable binary, check rights?");
		}	
	}
	
	@Override
	public TestOutput launch(final Test test) {
	    StringBuilder sysOut = new StringBuilder();
	    StringBuilder errOut = new StringBuilder();
	    int rc = -99;
	    
	    File testFile = new File(test.getFilename());
	    
	    try {
	    	ProcessBuilder pb;
	    	//if someone would like to bring back the harness inclusion mode
//	    	if(c.getHarnessMode() == HarnessMode.PARAMETER) {
	    		pb = new ProcessBuilder(executable.getAbsolutePath(), c.getHarnessFile(), testFile.getAbsolutePath());
//	    	} else {
//	    		pb = new ProcessBuilder(executable.getAbsolutePath(), test.getAbsolutePath());
//	    	}
	    	
	    	pb.directory(testFile.getParentFile());
	    	Process p = pb.start();
//	        Process p = Runtime.getRuntime().exec(cmdLine);
	    	String line;
	        BufferedReader input = new BufferedReader
	            (new InputStreamReader(p.getInputStream()));
	        BufferedReader error = new BufferedReader
        		(new InputStreamReader(p.getErrorStream()));
	        while ((line = input.readLine()) != null) {
	            sysOut.append(line + '\n');
	        }
	        while ((line = error.readLine()) != null) {
	        	errOut.append(line + '\n');
	        }
	    	
	        if(p.waitFor(c.getTimeoutValue(), c.getTimeoutUnit())) {
	        	rc = p.exitValue();
	        } else {
	        	//exception is thrown -> continue in the catch clause
	        }
        } catch (InterruptedException ex) {
        	rc = -2;
        	System.err.println("\nTest "+test.getTestName()+" timed out");
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    return new TestOutput(rc,
	    		Helper.removeTrailingNewline(sysOut.toString()), 
	    		Helper.removeTrailingNewline(errOut.toString()));
	}
}
