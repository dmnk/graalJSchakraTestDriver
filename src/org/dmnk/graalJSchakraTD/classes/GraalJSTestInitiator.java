package org.dmnk.graalJSchakraTD.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.time.chrono.JapaneseChronology;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.dmnk.graalJSchakraTD.enums.FailReason;
import org.dmnk.graalJSchakraTD.exceptions.GraalJSTestException;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
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
//		System.err.println("Testing " + t.getTestName() + " as " +t.getTestType().toString());
		//merge baseline and testfile
		File mf = new File(t.getFilename());
		//execute with graal
		TestOutput to = executeGraalJS(mf);
		
		//decide, based on TestOutput.rc, erroroutput and stdout if and why and where it failed
		if(TestType.passed(t, to)) {
			PassedTest pt = new GraalJSPassedTest(t, to);
			return pt;
		} else {
			//check failreason;
			
			//add diff
			//TODO: add diff at executedTest
			String diff;
			FailedTest ft;
			FailReason fr = TestType.evaluate(t, to);
			switch(t.getTestType()) {
			case BASELINE:
				if(to.getErrOut().length()>0) {
					ft = new GraalJSFailedTest(t, to, FailReason.EXCEPTION);
				} else {
					ft = new GraalJSFailedTest(t, to, FailReason.OUTPUT);
				}
				return ft;
//				break;
			case PASSSTRING:
				diff = duw.getDiff("Passed", to.getStdOut());
				
				//TODO: just a placeholder, more elaborated failreason detections!
				if(to.getErrOut().length()>0) {
					ft = new GraalJSFailedTest(t, to, FailReason.EXCEPTION);
				} else {
					ft = new GraalJSFailedTest(t, to, FailReason.OUTPUT);
				}
				return ft;
//				break;
			default:
				return new GraalJSFailedTest(t, to, FailReason.WARNING); //TODO: (unknown testtype exception)  or -> blow up testtype to validate "itself" <-
			}
			//TODO: blow up the failed tests to contain failing lines
			
		}
	}
	
	public TestOutput executeGraalJS(File t) {
		//combine test and harness
		File test = concatTestHarnes(t);
		//execute test
		TestOutput to = launchGraal(test);
		test.delete(); //remove the temporary concated file
		
		return to;
	}
	
	/**
	 * @return the path to the combined file
	 */
	private File concatTestHarnes(File t) {
		//TODO: check if harness file already exists & is newer than plain js file (skip execution otherwise)
		//TODO: use alternative temp path to store harnessed js files
		//NOTE: the JS-extensions are defined here https://github.com/Microsoft/ChakraCore/blob/master/bin/ch/WScriptJsrt.cpp
		// and https://github.com/Microsoft/ChakraCore/blob/master/lib/Runtime/Base/ScriptContext.cpp # 1740
		String harness = "WScript = {};\nWScript.Echo = print;\n";
		harness += "var BufferedReader = java.io.BufferedReader; \n var File = java.io.File;\n "
				+ "var FileReader = java.io.FileReader; \n function loadScriptFile(fileName) {\n"
				+ " try { \n var reader = new FileReader(new File(fileName));\n var bufferedReader = new BufferedReader(reader);"
				+ "var line;\n var code; \n while ((line = bufferedReader.readLine()) != null) {"
				+ " code += line; \n }\n  eval.call(this, code); execScript(code); } catch(e) { \n print('file not found')\n"
				+ " } if (reader) { reader.close(); } \n } \n WScript.loadScriptFile = loadScriptFile;";
//		+ " } if (reader) { reader.close(); } \n } \n WScript.loadScriptFile = evalFile;";
		
		File nFile = new File(t.getAbsolutePath().replace(".js", ".HNS.js"));
		List <String> test;
		
		try {
			PrintWriter pw = new PrintWriter(nFile, "UTF-8");
			test = java.nio.file.Files.readAllLines(t.toPath(), java.nio.charset.StandardCharsets.UTF_8);
			pw.print(harness);
			for(String line : test) {
				pw.println(line);
			}
			pw.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return nFile;
	}
	
	public TestOutput launchGraal(File test) {
		String cmdLine = graalJS + " " +test.getAbsolutePath();
 	    String line;
	    String sysOut = "";
	    String errOut = "";
	    int rc = -99;
	    
	    try {
	    	//TODO: use ProcessBuilder like described http://stackoverflow.com/questions/6811522/changing-the-working-directory-of-command-from-java
	    	ProcessBuilder pb = new ProcessBuilder(graalJS.getAbsolutePath(), test.getAbsolutePath());
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
	    
	    return new TestOutput(rc, removeTrailingNewline(sysOut), removeTrailingNewline(errOut));
	}
	
	private String removeTrailingNewline(String old) {
		StringBuilder n = new StringBuilder(old);
		if(n.length() > 0 && n.charAt(n.length()-1) == '\n') {
			n.deleteCharAt(n.length()-1);
		}
		
		return n.toString().trim();
	}
	
	/** try a more condensed example */
//	private TestOutput executeGraalJS(File t) {
//		TestOutput tc = null;
//		
//		/**
//		 * some workarounds needed as usage of harnes file supresses output on dev pc
//		 */
//		String[] command =
//		    {
//		        "sh",
//		    };
//		    Process p;
//			try {
//				p = Runtime.getRuntime().exec(command);
//				new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
//			    new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
//			    PrintWriter stdin = new PrintWriter(p.getOutputStream());
//			    stdin.println("dir c:\\ /A /Q");
//			    //TODO AAA continue here ;) 
//			    // write any other commands you want here
//			    stdin.close();
//			    tc = new TestOutput(p.waitFor(),"","");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		    
//		    
//		    return tc;
//	}
//	
//	/**
//	 * @author oracle forum
//	 * @see http://kr.forums.oracle.com/forums/thread.jspa?messageID=9250051
//	 */
//	private class SyncPipe implements Runnable	{
//		private final OutputStream ostrm_;
//		private final InputStream istrm_;
//		  
//		public SyncPipe(InputStream istrm, OutputStream ostrm) {	
//			istrm_ = istrm;
//			ostrm_ = ostrm;
//		}
//		
//		public void run() {
//			try {
//				final byte[] buffer = new byte[1024];
//			    for (int length = 0; (length = istrm_.read(buffer)) != -1; ) {
//			    	ostrm_.write(buffer, 0, length);
//			    }
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//			  
//	}
}
