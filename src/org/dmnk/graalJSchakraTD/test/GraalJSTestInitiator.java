package org.dmnk.graalJSchakraTD.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.dmnk.graalJSchakraTD.Exceptions.GraalJSTestException;
import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestInitiator;

public class GraalJSTestInitiator implements TestInitiator {

	private File graalJS;
	private DiffUtilsWrapper duw;
	
	public GraalJSTestInitiator (String graalExecutablePath) throws GraalJSTestException {
		graalJS = new File(graalExecutablePath);
		duw = new DiffUtilsWrapper();
		
		if(!graalJS.isFile()) {
			throw new GraalJSTestException("provided graalJS location doesn't point to executable js: \n\t"+graalExecutablePath);
		}
		if(!graalJS.canExecute()) {
			throw new GraalJSTestException("can't execute the provided graalJS binary, check rights?");
		}
	}
	
	@Override
	public ExecutedTest runTest(Test t) {
		//merge baseline and testfile
		File mf = new File(t.getFilename());
		//execute with graal
		TestOutput to = executeGraalJS(mf);
		//decide, based on TestOutput.rc, erroroutput and stdout if and why and where it failed
		if(passed(t, to)) {
			PassedTest pt = new GraalJSPassedTest(t, to);
			return pt;
		} else {
			//check failreason;
			
			//add diff
			//TODO: add diff at executedTest
			String diff;
			switch(t.getTestType()) {
			case BASELINE:
				break;
			case PASSSTRING:
				diff = duw.getDiff("Passed", to.getStdOut());
			}
		}
		
		return null;
	}
	
	private boolean passed(Test t, TestOutput to) {
		if(to.getReturnCode() != 0) {
			return false;
		} else if (!to.getErrOut().isEmpty()) {
			return false;
		} else {
			switch(t.getTestType()) {
			case BASELINE: 
				//TODO: checks for fail/pass baseline testtype 
				return true;
			case PASSSTRING:
				if(to.getStdOut().equals("Passed")) {
					return true;
				} else {
					return true;
				}
			default: System.err.println("un-known / set testtype !"); 
				return false;
			}
		}
	}
	
	private TestOutput executeGraalJS(File t) {
		TestOutput tc = null;
		
		/**
		 * some workarounds needed as usage of harnes file supresses output on dev pc
		 */
		String[] command =
		    {
		        "sh",
		    };
		    Process p;
			try {
				p = Runtime.getRuntime().exec(command);
				new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			    new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			    PrintWriter stdin = new PrintWriter(p.getOutputStream());
			    stdin.println("dir c:\\ /A /Q");
			    //TODO AAA continue here ;) 
			    // write any other commands you want here
			    stdin.close();
			    tc = new TestOutput(p.waitFor(),"","");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		    
		    
		    return tc;
	}
	
	/**
	 * @author oracle forum
	 * @see http://kr.forums.oracle.com/forums/thread.jspa?messageID=9250051
	 */
	private class SyncPipe implements Runnable	{
		private final OutputStream ostrm_;
		private final InputStream istrm_;
		  
		public SyncPipe(InputStream istrm, OutputStream ostrm) {	
			istrm_ = istrm;
			ostrm_ = ostrm;
		}
		
		public void run() {
			try {
				final byte[] buffer = new byte[1024];
			    for (int length = 0; (length = istrm_.read(buffer)) != -1; ) {
			    	ostrm_.write(buffer, 0, length);
			    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			  
	}
}
