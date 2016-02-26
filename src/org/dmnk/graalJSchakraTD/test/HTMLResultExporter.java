package org.dmnk.graalJSchakraTD.test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;

public class HTMLResultExporter implements ResultExporter {
	private String exportPath;
	private StringBuilder exportHTML;
	private int exportedGroups = 1;
	private int exportedTests;
	
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
			"<div class=\"panel panel-default\">"
			+"<div class=\"panel-heading\">"
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
    +"</div>"
   +"<div id=\"collapse%%GROUPNR%%\" class=\"panel-collapse collapse in\">"
      + "<div class=\"panel-body\">"
      	+"<div class=\"col-md-12\">"
	          +"<table class=\"table table-bordered\">"
	            +"<thead>"
	             + "<tr>"
	                +"<th class=\"col-md-1\">#</th>"
	                +"<th class=\"col-md-1\">Name</th>"
	                +"<th class=\"col-md-7\">Files</th>"
	              +"</tr>"
	            +"</thead>"
	            +"<tbody>";
			
	private final String htmlTestGroupEnd =
			"\t\t\t</tbody>"
			+ "\t\t</table>"
			+ "\t</div>"
			+ "</div>"
			+ "</div>"
			+ "</div>";
	
	private final String htmlTest =
			"\t\t<tr class=\"%%TESTSTATUS%%\">\n"
			+ "\t\t\t<td>%%TESTNR%%<span class=\"label label-default\">output</span></td>\n"
			+ "\t\t\t<td>%%TESTNAME%%</td>"
			+ "\t\t\t<td class=\"code-container\">";
//	<div class="panel-group">
//	  <div class="panel panel-default">
//	    <div class="panel-heading">
//	      <h4 class="panel-title">
//	        <a data-toggle="collapse" href="#collapse1_js">arr_bailout.js</a>
//			<a href="arr_bailout.js" target="_blank">
//				<span class="glyphicon glyphicon-open-file"></span>
//			</a>
//	      </h4>
//	    </div>
//	    <div id="collapse1_js" class="panel-collapse collapse">
//			<div class="panel-body code">
//              <pre class="line-numbers" data-src="arr_bailout.js" data-line="13,19-22"></pre>
//              <!--  http://prismjs.com/plugins/line-highlight/#examples  -->
//			</div>
//	    </div>
//	  </div>
//	</div>
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
//              <!--  http://prismjs.com/plugins/line-highlight/#examples  -->
//			</div>
//	    </div>
//	  </div>
//	</div>
//	<!--  / DIFF -->
//</td>
//</tr>
	
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
	
	public HTMLResultExporter(String path) {
		this.exportPath = path;
		this.exportHTML = new StringBuilder();
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
		String  tempTest = this.htmlTest.replaceAll(this.phTestName, t.getFilename());
		tempTest = tempTest.replaceAll(this.phTestNr, ""+this.exportedTests++);
		//TODO: color depending on test status
		//TODO: Output/output diff
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
