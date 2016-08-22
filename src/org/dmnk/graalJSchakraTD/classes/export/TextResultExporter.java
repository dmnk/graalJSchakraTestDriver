package org.dmnk.graalJSchakraTD.classes.export;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dmnk.graalJSchakraTD.interfaces.ExecutedTest;
import org.dmnk.graalJSchakraTD.interfaces.PassedTest;
import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;
import org.dmnk.graalJSchakraTD.interfaces.TestExecutedGroup;

/**
 * very simple export of the results into a csv format
 * implements {@link ResultExporter}
 * 
 * @author dominik
 *
 */
public class TextResultExporter implements ResultExporter {
	private String exportPath;
	private StringBuilder exportTXT;
	
	public TextResultExporter(String path) {
		exportTXT = new StringBuilder();
		this.exportPath = path;
	}
	
	@Override
	public void setExportPath(String path) {
		this.exportPath = path;
	}

	@Override
	public void export(List<TestExecutedGroup> testlist) {
		addTXTHeader();
		
		for (TestGroup group : testlist) {
			//addGroupHeader(group);
			for(Test test : group.getTests()) {
				exportTest(test);
			}
		}
		
		writeResult();
	}

	private void addTXTHeader() {
		exportTXT.append("Filename, Status\n");
	}
	
	private void exportTest(Test t) {
		StringBuilder text = new StringBuilder(t.getFilename() + ", ");
		if(t instanceof ExecutedTest) {
			if(t instanceof PassedTest) {
				text.append("passed");
			} else {
				text.append("failed");
			}
		} else {
			text.append("excluded");
		}
		exportTXT.append(text + "\n");
	}
	
	private void writeResult() {
		try (PrintWriter pw = new PrintWriter(this.exportPath, "UTF-8")) {
			pw.print(exportTXT);
			System.out.println("exported results as text in file " + exportPath);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
}
