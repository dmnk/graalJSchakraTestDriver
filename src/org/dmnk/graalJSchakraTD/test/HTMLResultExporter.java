package org.dmnk.graalJSchakraTD.test;

import java.util.List;

import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;

public class HTMLResultExporter implements ResultExporter {
	private String exportPath;
	private StringBuilder exportHTML;
	
	private static String htmlHeader = 
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
	private static String htmlTestGroupStart ="";
	private static String htmlTestGroupEnd ="";
	private static String htmlTest = "";
	private static String htmlFooter ="";
	
	//PLACEHOLDER
	private static String phTestGroup = "%%GROUPNAME%%";
	private static String phTestName = "%%TESTNAME%%";
	
	public HTMLResultExporter(String path) {
		this.exportPath = path;
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
			for(Test test : group.getTests()) {
				
			}
			addGroupFooter();
		}
		
		addHTMLFooter();
		
		//TODO: writeResult();
	}

	private void addHTMLHeader() {
		this.exportHTML.append(HTMLResultExporter.htmlHeader);
	}
	
	private void addHTMLFooter() {
		this.exportHTML.append(HTMLResultExporter.htmlFooter);
	}
	
	private void addGroupHeader(TestGroup tg) {
		
	}
	
	private void addGroupFooter() {
		
	}
}
