package org.dmnk.graalJSchakraTD.classes.export;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.dmnk.graalJSchakraTD.classes.Helper;
import org.dmnk.graalJSchakraTD.enums.FailReason;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestExecutedGroup;

/**
 * exports the executed test groups as single html file, styled with bootstrap and prism.js
 * @deprecated switch to a template engine like {@link http://freemarker.org}
 * @author dominik
 *
 */
public class HTMLResultExporter implements ResultExporter {
	private String exportPath;
	private StringBuilder exportHTML;
	private int exportedGroups = 1;
	private int exportedTests;
	private int testID;
	
	/**
	 * 1: htmlHeader
	 * 0: tbd: run summary
	 * 		used parameters
	 * 		overall statistics
	 * groupN: 
	 * 		htmlTestGroupStart {
	 * 		testN: {
	 * 			htmlTestBegin
	 * 			htmlTestCodePanel (js file)
	 * 			htmlTestCodePanel (diff/output file)
	 * 			htmlTestCode
	 * 		}
	 * 		htmlTestGroupEnd
	 * }
	 * 1: htmlFooter 
	 * 
	 */
	
	private final String htmlHeader = 
			"<!DOCTYPE html>\n"
			+ "<html>\n"
			+ "\t<head>\n"
			+ "\t<meta charset=\"UTF-8\">\n"
			+ "\t<title>graal-JS executing MS-ChakraCore tests: Results</title>\n"		
			+ "\t<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">\n"
			+ "\t<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\" integrity=\"sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r\" crossorigin=\"anonymous\">\n"
			+ "\t<link rel=\"stylesheet\" href=\"css/prism.css\">\n"
			+ "\t<link rel=\"stylesheet\" href=\"css/style.css\">\n"
			+ "</head>\n"
			+ "<body>\n"
			+ "\t<div class=\"container-fluid\">\n"
			+ "<h1>graal-JS executing MS-ChakraCore tests: Results</h1>\n"
			+ "<h2>Test-Results:</h2>\n"
			+"<a href=\"#\" onclick=\"$('#accordion .panel-collapse').collapse('hide');\">Close-All</a>"
			+ "\t<div class=\"panel-group\" id=\"accordion\">\n";
	
	private final String htmlTestGroupStart =
			"<!-- HTML TEST GROUP START -->\n"
			+"<div class=\"panel panel-default\">\n"
			+"<div class=\"panel-heading\">\n"
			+"<a data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#collapse%%GROUPNR%%\">"
        +"<h4 class=\"panel-title\">%%GROUPNAME%%</h4>"
        +"<div class=\"well-sm\">"
        +"<div class=\"progress\">"
        	+"<div class=\"progress-bar progress-bar-success\" role=\"progressbar\" aria-valuenow=\"%%PCT_PASSED%%\" aria-valuemin=\"0\" aria-valuemax=\"%%PCT_TOTAL%%\" style=\"/*min-width: 2em;*/ width: %%PCT_PASSED%%%;\"><abbr title=\"passed\">%%PCT_PASSED%%%</abbr></div>"
	        +"<div class=\"progress-bar progress-bar-danger\" role=\"progressbar\" aria-valuenow=\"%%PCT_RED%%\" aria-valuemin=\"0\" aria-valuemax=\"%%PCT_TOTAL%%\" style=\"/*min-width: 2em;*/ width: %%PCT_RED%%%;\"><abbr title=\"exception\">%%PCT_RED%%%</abbr></div>"
	        +"<div class=\"progress-bar\" role=\"progressbar\" aria-valuenow=\"%%PCT_BLUE%%\" aria-valuemin=\"0\" aria-valuemax=\"%%PCT_TOTAL%%\" style=\"/*min-width: 2em;*/ width: %%PCT_BLUE%%%;\"><abbr title=\"error\">%%PCT_BLUE%%%</abbr></div>"
	        +"<div class=\"progress-bar progress-bar-warning\" role=\"progressbar\" aria-valuenow=\"%%PCT_ORANGE%%\" aria-valuemin=\"0\" aria-valuemax=\"%%PCT_TOTAL%%\" style=\"/*min-width: 2em;*/ width: %%PCT_ORANGE%%%;\"><abbr title=\"output\">%%PCT_ORANGE%%%</abbr></div>"
	        +"<div class=\"progress-bar progress-bar-active\" role=\"progressbar\" aria-valuenow=\"%%PCT_ACTIVE%%\" aria-valuemin=\"0\" aria-valuemax=\"%%PCT_TOTAL%%\" style=\"/*min-width: 2em;*/ width: %%PCT_ACTIVE%%%; background-image: linear-gradient(to bottom,#909090 0,#000 100%);\"><abbr title=\"crash/timeout\"> %%PCT_ACTIVE%%%</abbr></div>"
          +"</div>"
	      +"</div>"
	      +"</a>"
    +"</div>\n"
   +"<div id=\"collapse%%GROUPNR%%\" class=\"panel-collapse collapse in\">\n"
      + "<div class=\"panel-body\">"
      	+"<div class=\"col-md-12\">"
	          +"<table class=\"table table-hover\">"
	            +"<thead>"
	             + "<tr>"
	                +"<th class=\"col-md-1\">#</th>"
	                +"<th class=\"col-md-1\">Result</th>"
	                +"<th class=\"col-md-1\">Name</th>"
	                +"<th class=\"col-md-7\">Files</th>"
	              +"</tr>"
	            +"</thead>\n"
	            +"<tbody>\n";
			
	private final String htmlTestGroupEnd =
			"\t\t\t</tbody>\n"
			+ "\t\t</table>"
			+ "\t</div>"
			+ "</div>"
			+ "</div>"
			+ "</div>";
	
	private final String htmlTestBegin =
			"<!-- HTML TEST BEGIN -->\n"
			+"\t\t<tr class=\"%%TEST_STATUS%%\">\n"
			+ "\t\t\t<td>%%TESTNR%%</td><td><span class=\"label label-default\">%%RESULT%%</span></td>\n"
			+ "\t\t\t<td>%%TESTNAME%%</td>\n"
			+ "\t\t\t<td class=\"code-container\">\n"
			+ "<div class=\"panel-group\">\n";
	
	private final String htmlTestCodePanel = 
			"<!-- HTML TEST CODE PANEL: %%FILE_NAME%% -->\n"
			+"<div class=\"panel panel-default\">\n"
			+"<div class=\"panel-heading\">\n"
			+"<h4 class=\"panel-title\">\n"
			+"<a data-toggle=\"collapse\" href=\"#collapse%%TEST_ID%%_js\">%%FILE_NAME%%</a>\n" //TODO: correct the href
			+"<a href=\"%%FILE_LOCATION%%\" target=\"_blank\">\n"
			+"<span class=\"glyphicon glyphicon-open-file\"></span>\n"
			+"</a>\n"
			+"</h4>\n"
			+"</div>\n"
			+"<div id=\"collapse%%TEST_ID%%_js\" class=\"panel-collapse collapse\">\n" //TODO: same id as href above
			+"<div class=\"panel-body code\">\n"
			+"<pre class=\"line-numbers\" data-src=\"test/%%FILE_LOCATION%%\" data-line=\"%%HIGHLIGHT_LINES%%\"></pre>\n"
//			+"<pre class=\"line-numbers\" data-src=\"%%FILE_NAME%%\"></pre>\n"
			+"</div>\n"
			+"</div>\n"
			+"</div>\n";
	
	private final String htmlTestOutputPanel = 
			"<!-- HTML TEST OUTPUT PANEL: %%FILE_NAME%% -->\n"
//			+"<div class=\"panel-group\">\n"
			+"<div class=\"panel panel-default\">\n"
			+"<div class=\"panel-heading\">\n"
			+"<h4 class=\"panel-title\">\n"
			+"<a data-toggle=\"collapse\" href=\"#collapse%%TEST_ID%%_op\">%%FILE_NAME%%</a>\n" //TODO: correct the href
			/*+"<a href=\"%%FILE_LOCATION%%\" target=\"_blank\">\n"
			+"<span class=\"glyphicon glyphicon-open-file\"></span>\n"
			+"</a>\n"*/
			+"</h4>\n"
			+"</div>\n"
			+"<div id=\"collapse%%TEST_ID%%_op\" class=\"panel-collapse collapse\">\n" //TODO: same id as href above
			+"<div class=\"panel-body code\">\n"
			+"<pre class=\"line-numbers\">"
			+"<code class=\"language-diff\">%%TEST_OUTPUT%%</code>"
			+"</pre>\n"
//			+"<pre class=\"line-numbers\" data-src=\"%%FILE_NAME%%\"></pre>\n"
			+"</div>\n"
//			+"</div>\n"
			+"</div>\n"
			+"</div>\n";
//	<!-- / SRC -->
//	<!--  DIFF -->
//	<div class="panel-group">
//	  <div class="panel panel-default">
//	    <div class="panel-heading">
//	      <h4 class="panel-title">
//	        <a data-toggle="collapse" href="#collapse1_diff">arr_bailout.diff</a>
//	      </h4>
//	    </div>
//	    <div id="collapse1_diff" class="panel-collapse collapse">
//			<div class="panel-body code">
//              <pre class="line-numbers" data-src="arr_bailout.diff"></pre>
//			</div>
//	    </div>
//	  </div>
//	</div>
//	<!--  / DIFF -->
	private final String htmlTestEnd = 
			"</div>"
			+ "</td>"
			+"</tr>";
	
	private final String htmlFooter =
			"</div>"
			+"<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\"></script>"
			+"<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\"></script>"
			+"<script src=\"js/prism.js\"></script>"
	        +"<script src=\"//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.9.1/bootstrap-table.min.js\"></script>"
	        +"<script src=\"//rawgit.com/akottr/dragtable/master/jquery.dragtable.js\"></script>"
			+"</body>"
			+"</html>";
	
	//PLACEHOLDER
	private final String phTestGroup = "%%GROUPNAME%%";
	private final String phTestName = "%%TESTNAME%%";
	private final String phTestGroupNr = "%%GROUPNR%%";
	private final String phTestNr = "%%TESTNR%%"; //unique for the testgroup
	private final String phTestID = "%%TEST_ID%%"; //unique for the whole html
	private final String phFileName = "%%FILE_NAME%%";
	private final String phFileLocation ="%%FILE_LOCATION%%";
	private final String phStatus = "%%TEST_STATUS%%";
	private final String phTestOutput = "%%TEST_OUTPUT%%";
	private final String phResult = "%%RESULT%%"; //what was the outcome
	private final String phErrorLine = "%%HIGHLIGHT_LINES%%"; //where did the error occur
	
	//DIAGRAM
	private final String pctGreen ="%%PCT_PASSED%%";
//	private final String pctExcluded ="%%PCT_EXCLUDED%"; //stays empty per definition ...
	private final String pctOrange = "%%PCT_ORANGE%%";
	private final String pctRed = "%%PCT_RED%%";
//	private final String pctOrange = "%%PCT_ASSERTION%%";
	private final String pctBlue = "%%PCT_BLUE%%"; 
	private final String pctTotal = "%%PCT_TOTAL%%";
	private final String pctActive = "%%PCT_ACTIVE%%"; //GRAYish
	
	private Map<String, String> statusClass;
	
	public HTMLResultExporter(String path) {
		this.exportPath = path;
		this.exportHTML = new StringBuilder();
		
		statusClass = new HashMap<String, String>();
		statusClass.put("passed", "success");
		statusClass.put("excluded", "default text-muted");
		statusClass.put(FailReason.CRASH.toString(), "active");
		statusClass.put(FailReason.ERROR.toString(), "info");
		statusClass.put(FailReason.EXCEPTION.toString(), "danger");
		statusClass.put(FailReason.OUTPUT.toString(), "warning");
	}
	
	@Override
	public void setExportPath(String path) {
		this.exportPath = path;
	}

	@Override
	public void export(List<TestExecutedGroup> testlist) {
		addHTMLHeader();
		
		for (TestExecutedGroup group : testlist) {
			addGroupHeader(group);
//			this.exportedTests = 0; //reset for every group?
			for(Test test : group.getTests()) {
				addTest(test, group);
			}
			addGroupFooter();
		}
		
		addHTMLFooter();
		
		writeResult();
	}

	private void addHTMLHeader() {
		this.exportHTML.append(this.htmlHeader);
	}
	
	private void addHTMLFooter() {
		this.exportHTML.append(this.htmlFooter);
	}
	
	private void addGroupHeader(TestExecutedGroup tg) {
		String tempGroupHeader = this.htmlTestGroupStart;
		
		DecimalFormat df = new DecimalFormat ("#.##");
		
		Float pctPassed = (float)(tg.getPassed())*100/tg.getTotal();
		Float pctCrash = (float)tg.getCrashed()*100/tg.getTotal();
		Float pctException = (float)tg.getException()*100/tg.getTotal();
		Float pctError = (float)tg.getError()*100/tg.getTotal();
		Float pctOutput = (float)tg.getOutput()*100/tg.getTotal();
		
		tempGroupHeader = tempGroupHeader.replaceAll(this.phTestGroup, tg.getGroupName());
		tempGroupHeader = tempGroupHeader.replaceAll(this.phTestGroupNr, ""+this.exportedGroups++);
		tempGroupHeader = tempGroupHeader.replaceAll(this.pctTotal, ""+tg.getTotal());
		tempGroupHeader = tempGroupHeader.replaceAll(this.pctGreen, 	df.format(pctPassed));
//		crash & exception
		tempGroupHeader = tempGroupHeader.replaceAll(this.pctRed, 		df.format(pctException));
		tempGroupHeader = tempGroupHeader.replaceAll(this.pctOrange, 	df.format(pctOutput));
		tempGroupHeader = tempGroupHeader.replaceAll(this.pctBlue, 		df.format(pctError));
		tempGroupHeader = tempGroupHeader.replaceAll(this.pctActive,	df.format(pctCrash));
		this.exportHTML.append(tempGroupHeader);
	}
	
	private void addGroupFooter() {
		this.exportHTML.append(this.htmlTestGroupEnd);
	}
	
	private void addTest(Test t, TestExecutedGroup teg) {
		testID++;
		StringBuilder testExport = new StringBuilder();
		
		String tempTest = this.htmlTestBegin.replaceAll(this.phTestName, t.getTestName().concat(" - type: " + t.getTestType()));
		tempTest = tempTest.replaceAll(this.phTestNr, ""+this.exportedTests++);
		
		String testHighlight;
//		int errorLine = 0;
		
		if(t instanceof FailedTest) {
			FailedTest ft = (FailedTest) t;
			testHighlight = ft.getFailReason().toString();
		} else if (t instanceof PassedTest) {
			testHighlight = "passed";
		} else {
			testHighlight = "excluded";
		}
		
		tempTest = tempTest.replaceAll(phStatus, statusClass.getOrDefault(testHighlight, new String(testHighlight + " not found!!")));
		tempTest = tempTest.replace(phResult, testHighlight);
//		if(errorLine > 0) {
//			tempTest = tempTest.replace(phErrorLine, ""+errorLine);
//		}
		
		this.exportHTML.append(tempTest);
		
		testExport.append(genTestSourcePanel(teg, t, testID));
		
		if(t instanceof FailedTest) {
			testExport.append(genTestOutputPanel((FailedTest) t, testID));
		}
		
		testExport.append(htmlTestEnd);
		this.exportHTML.append(testExport);
	}
	
	private String genTestSourcePanel(TestExecutedGroup tg, Test t, int testID) {
		String tempTest = htmlTestCodePanel.replaceAll(phFileName, t.getTestName());
		tempTest = tempTest.replaceAll(phTestID, ""+testID);
		
		tempTest = tempTest.replaceAll(phFileLocation, tg.getGroupName() + "/" + t.getTestName());
		
		int errorLine =0;
		if(t instanceof FailedTest) {
			errorLine = Helper.numberAfterString(((FailedTest) t).getErrorOutput(), t.getFilename(), ':');
		}
		if(errorLine > 0) {
			tempTest = tempTest.replaceAll(phErrorLine, ""+errorLine);
		}
		return tempTest;
	}
		
	private String genTestOutputPanel(FailedTest ft, int testID) {
		
		String outputCode = "";
		String tempCode ="" ;
			
		if (ft.getErrorOutput().length() > 0) {
			tempCode = htmlTestOutputPanel.replaceAll(phFileName, "ERR-out");
			tempCode = tempCode.replaceAll(phTestID, "e_"+testID);
			outputCode += tempCode.replaceAll(phTestOutput, Matcher.quoteReplacement(ft.getErrorOutput()));
		}
		if (ft.diffIsSet()) {
			tempCode = htmlTestOutputPanel.replaceAll(phFileName, "DIFF");
			tempCode = tempCode.replaceAll(phTestID, "d_"+testID);
			outputCode += tempCode.replaceAll(phTestOutput, Matcher.quoteReplacement(ft.getDiff()));
		} else if (ft.getOutput().length() > 0){
			tempCode = htmlTestOutputPanel.replace(phFileName, "STD-out");
			tempCode = tempCode.replaceAll(phTestID, "s_"+testID);
			outputCode += tempCode.replaceAll(phTestOutput, Matcher.quoteReplacement(ft.getOutput()));
		}

		return outputCode;
	}
	
	private void writeResult() {
		try {
			PrintWriter pw = new PrintWriter(this.exportPath, "UTF-8");
			pw.print(this.exportHTML);
			pw.close();
			
			System.out.println("exported results as HTML in file "+this.exportPath);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
