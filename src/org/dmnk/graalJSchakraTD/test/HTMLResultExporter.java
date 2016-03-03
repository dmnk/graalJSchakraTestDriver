package org.dmnk.graalJSchakraTD.test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dmnk.graalJSchakraTD.enums.FailReason;
import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.FailedTest;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;

public class HTMLResultExporter implements ResultExporter {
	private String exportPath;
	private StringBuilder exportHTML;
	private int exportedGroups = 1;
	private int exportedTests;
	
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
			+ "\t<div class=\"panel-group\" id=\"accordion\">\n";
	
	private final String htmlTestGroupStart =
			"<!-- HTML TEST GROUP START -->\n"
			+"<div class=\"panel panel-default\">\n"
			+"<div class=\"panel-heading\">\n"
			+"<a data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#collapse%%GROUPNR%%\">"
        +"<h4 class=\"panel-title\">%%GROUPNAME%%</h4>"
        +"<div class=\"well-sm\">"
        +"<div class=\"progress\">"
	        +"<div class=\"progress-bar progress-bar-success\" style=\"width: 35%\"><span class=\"sr-only\">35% Complete (success)</span></div>"
	        +"<div class=\"progress-bar\" role=\"progressbar\" aria-valuenow=\"2\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"min-width: 2em; width: 2%;\"> 2%</div>"
	        +"<div class=\"progress-bar progress-bar-warning\" style=\"width: 20%\"><span class=\"sr-only\">20% Complete (warning)</span></div>"
	        +"<div class=\"progress-bar progress-bar-danger\" style=\"width: 10%\"><span class=\"sr-only\">10% Complete (danger)</span></div>"
	      +"</div>"
	      +"</div>"
	      +"</a>"
    +"</div>\n"
   +"<div id=\"collapse%%GROUPNR%%\" class=\"panel-collapse collapse in\">\n"
      + "<div class=\"panel-body\">"
      	+"<div class=\"col-md-12\">"
	          +"<table class=\"table table-bordered\">"
	            +"<thead>"
	             + "<tr>"
	                +"<th class=\"col-md-1\">#</th>"
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
			+ "\t\t\t<td>%%TESTNR%%<span class=\"label label-default\">output</span></td>\n"
			+ "\t\t\t<td>%%TESTNAME%%</td>\n"
			+ "\t\t\t<td class=\"code-container\">\n";
	
	private final String htmlTestCodePanel = 
			"<!-- HTML TEST CODE PANEL: %%FILE_NAME%% -->\n"
			+"<div class=\"panel-group\">\n"
			+"<div class=\"panel panel-default\">\n"
			+"<div class=\"panel-heading\">\n"
			+"<h4 class=\"panel-title\">\n"
			+"<a data-toggle=\"collapse\" href=\"#collapse1_js\">%%FILE_NAME%%</a>\n" //TODO: correct the href
			+"<a href=\"%%FILE_LOCATION%%\" target=\"_blank\">\n"
			+"<span class=\"glyphicon glyphicon-open-file\"></span>\n"
			+"</a>\n"
			+"</h4>\n"
			+"</div>\n"
			+"<div id=\"collapse1_js\" class=\"panel-collapse collapse\">\n" //TODO: same id as href above
			+"<div class=\"panel-body code\">\n"
			+"<pre class=\"line-numbers\" data-src=\"%%FILE_LOCATION%%\" data-line=\"%%HIGHLIGHT_LINES%%\"></pre>\n"
			+"</div>\n"
			+"</div>\n"
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
			"</td>"
			+"</tr>";
	
	private final String htmlFooter =
			"</div>"
			+"<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\"></script>"
			+"<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\"></script>"
			+"<script src=\"js/prism.js\"></script>"
			+"</body>"
			+"</html>";
	
	//PLACEHOLDER
	private final String phTestGroup = "%%GROUPNAME%%";
	private final String phTestName = "%%TESTNAME%%";
	private final String phTestGroupNr = "%%GROUPNR%%";
	private final String phTestNr = "%%TESTNR%%";
	private final String phFileName = "%%FILE_NAME%%";
	private final String phStatus = "%%TEST_STATUS%%";
	private Map<String, String> statusClass;
	
	public HTMLResultExporter(String path) {
		this.exportPath = path;
		this.exportHTML = new StringBuilder();
		
		statusClass = new HashMap<String, String>();
		statusClass.put("passed", "success");
		statusClass.put("excluded", "default text-muted");
		statusClass.put(FailReason.WARNING.toString(), "info");
		statusClass.put(FailReason.CRASH.toString(), "danger");
		statusClass.put(FailReason.ASSERTION.toString(), "warning");
		statusClass.put(FailReason.EXCEPTION.toString(), "danger");
	}
	
	@Override
	public void setExportPath(String path) {
		this.exportPath = path;
	}

	@Override
	public void export(List<TestGroup> testlist) {
		addHTMLHeader();
		
		for (TestGroup group : testlist) {
			addGroupHeader(group);
//			this.exportedTests = 0; //reset for every group?
			for(Test test : group.getTests()) {
				addTest(test);
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
	
	private void addGroupHeader(TestGroup tg) {
		String tempGroupHeader = this.htmlTestGroupStart;
		tempGroupHeader = tempGroupHeader.replaceAll(this.phTestGroup, tg.getGroupName());
		tempGroupHeader = tempGroupHeader.replaceAll(this.phTestGroupNr, ""+this.exportedGroups++);
		this.exportHTML.append(tempGroupHeader);
	}
	
	private void addGroupFooter() {
		this.exportHTML.append(this.htmlTestGroupEnd);
	}
	
	private void addTest(Test t) {
		StringBuilder testExport = new StringBuilder();
		
		String tempTest = this.htmlTestBegin.replaceAll(this.phTestName, t.getFilename());
		tempTest = tempTest.replaceAll(this.phTestNr, ""+this.exportedTests++);
		//TODO: color depending on test status
		String testHighlight;
		if(t instanceof FailedTest) {
			FailedTest ft = (FailedTest) t;
			testHighlight = ft.getFailReason().toString();
		} else if (t instanceof PassedTest) {
			testHighlight = "passed";
		} else {
			testHighlight = "excluded";
		}
		tempTest = tempTest.replaceAll(phStatus, statusClass.getOrDefault(testHighlight, new String(testHighlight + " not found!!")));
		this.exportHTML.append(tempTest);
		tempTest = htmlTestCodePanel.replaceAll(phFileName, t.getFilename());
		
		
		
//		tempTest = tempTest.replaceAll(phFileLocation, new File(t.getFilename());
//		tempTest = tempTest.replaceAll(phFileLineNumbers, t.getErrorLines());
		testExport.append(tempTest);
		//TODO: Output/output diff
		if(t.getTestType() == TestType.BASELINE) {
//			tempTest = htmlTestCodePanel.replaceAll(phFileName, t.getDiffFile());
	//		tempTest = tempTest.replaceAll(phFileLocation, ...);
	//		tempTest = tempTest.replaceAll(phFileLineNumbers, "");
			testExport.append(tempTest);
		}
		testExport.append(htmlTestEnd);
		this.exportHTML.append(tempTest);
	}
	
	private void writeResult() {
		try {
			PrintWriter pw = new PrintWriter(this.exportPath, "UTF-8");
			pw.print(this.exportHTML);
			pw.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
