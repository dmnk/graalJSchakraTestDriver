package org.dmnk.graalJSchakraTD.classes;

import org.dmnk.graalJSchakraTD.classes.test.GenericFailedTest;
import org.dmnk.graalJSchakraTD.classes.test.GenericPassedTest;
import org.dmnk.graalJSchakraTD.classes.test.TestOutput;
import org.dmnk.graalJSchakraTD.enums.FailReason;
import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestEvaluator;
import org.dmnk.graalJSchakraTD.interfaces.TestInitiator;

/**
 * @deprecated
 * @author Dominik
 *
 */
public class GenericTestInitiator implements TestInitiator {

	private Configuration c;
	private TestEvaluator te;
	
	public GenericTestInitiator (Configuration conf, TestEvaluator te)  {
		c = conf;
		this.te = te;
	}
	
	/**
	 * @throws ConfigurationException 
	 * @deprecated
	 */
	@Override
	public ExecutedTest runTest(Test t) {
		//merge baseline and testfile
//		File mf = new File(t.getFilename());
		
		//execute with graal
		TestOutput to = null;
		try {
			to = executeGraalJS(t);
		} catch (ConfigurationException e) {
			
		}
		
		//decide, based on TestOutput.rc, erroroutput and stdout if and why and where it failed
		if(te.passed(t, to)) {
			PassedTest pt = new GenericPassedTest(t, to);
			return pt;
		} else {
			//check failreason;
			String diff;
			FailedTest ft;
			FailReason fr = te.evaluate(t, to);
			switch(t.getTestType()) {
			case BASELINE:
				diff = DiffUtilsWrapper.getDiff(t, to);
				
				ft = new GenericFailedTest(t, to, fr, diff);
				
				return ft;
//				break;
			case PASSSTRING:
//				diff = DiffUtilsWrapper.getDiff("Passed", to.getStdOut());
				// no need to evaluate the exact diff to the pass-string, right?
				// but maybe the testtype should be visible in the result output?
				
				ft = new GenericFailedTest(t, to, fr);
				return ft;
//				break;
			default:
				return new GenericFailedTest(t, to, fr); 
				//TODO: (unknown testtype exception)  or -> blow up testtype to validate "itself" <-
			}			
		}
	}
	
	/**
	 * @deprecated
	 * @param t
	 * @return
	 * @throws ConfigurationException 
	 */
	public TestOutput executeGraalJS(Test t) throws ConfigurationException {
		TestOutput to = null;
		
		//combine test and harness ? 
//		if(c.getHarnessMode() == HarnessMode.INCLUDE) {
//			File test;
//			test = (c.getHarnessMode() == HarnessMode.INCLUDE) ? concatTestHarnes(t) : t;
//			to = launchGraal(test);
//			test.delete(); 
//		} else {
		TestExecutorGeneric gte;
			gte = new TestExecutorGeneric(c);
			to = gte.launch(t);
//		}
		
		return to;
	}
	
	/**
	 * @return the path to the combined file
	 */
//	private File concatTestHarnes(File t) {
//		//TODO: check if harness file already exists & is newer than plain js file (skip execution otherwise)
//		//TODO: use alternative temp path to store harnessed js files
//		//NOTE: the JS-extensions are defined here https://github.com/Microsoft/ChakraCore/blob/master/bin/ch/WScriptJsrt.cpp
//		// and https://github.com/Microsoft/ChakraCore/blob/master/lib/Runtime/Base/ScriptContext.cpp # 1740
//		String helperFunction = "GJCTHelper = {}\n"
//				+"GJCTHelper.load = function(filename) {\n"
//				+"try{load(filename)}\n"
//				+"catch (e) {\n"
////				+"print(e);"
//				+" load(filename.replace(/String.fromCharCode(0x5C)/g,\"/\"))\n"
//				+"}\n"
//				+"}\n";
//
//		
//		String harness = helperFunction + "WScript = {};\nWScript.Echo = print;\n"
//				+ "WScript.LoadScriptFile = load;\n"
//				+ "WScript.LoadModuleFile = load;\n"
//				+ "WScript.Arguments = new Array();\n";
////		harness += "var BufferedReader = java.io.BufferedReader; \n var File = java.io.File;\n "
////				+ "var FileReader = java.io.FileReader; \n function loadScriptFile(fileName) {\n"
////				+ " try { \n var reader = new FileReader(new File(fileName));\n var bufferedReader = new BufferedReader(reader);"
////				+ "var line;\n var code; \n while ((line = bufferedReader.readLine()) != null) {"
////				+ " code += line; \n }\n  eval.call(this, code); execScript(code); } catch(e) { \n print('file not found')\n"
////				+ " } if (reader) { reader.close(); } \n } \n WScript.loadScriptFile = loadScriptFile;";
//		
//		File nFile = new File(t.getAbsolutePath().replace(".js", ".HNS.js"));
////		new TemporaryFile
//		List <String> test;
//		
//		try {
//			PrintWriter pw = new PrintWriter(nFile, "UTF-8");
//			test = java.nio.file.Files.readAllLines(t.toPath(), java.nio.charset.StandardCharsets.UTF_8);
//			pw.print(harness);
//			for(String line : test) {
//				pw.println(line);
//			}
//			pw.close();
//		} catch (FileNotFoundException | UnsupportedEncodingException e) {
//			e.printStackTrace();
//			System.err.println("GJTI-Problem :" + e.getMessage());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return nFile;
//	}
	
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
