package org.dmnk.graalJSchakraTD.test;

import java.util.List;

import org.dmnk.graalJSchakraTD.interfaces.ResultExporter;
import org.dmnk.graalJSchakraTD.interfaces.Test;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;

public class TextResultExporter implements ResultExporter {
	private String exportPath;
	private StringBuilder exportTXT;
	
	public TextResultExporter(String path) {
		this.exportPath = path;
	}
	
	@Override
	public void setExportPath(String path) {
		this.exportPath = path;
	}

	@Override
	public void export(List<TestGroup> testlist) {
		addTXTHeader();
		
		for (TestGroup group : testlist) {
			//addGroupHeader(group);
			for(Test test : group.getTests()) {
				exportTest(test);
			}
			//addGroupFooter();
		}
		
		//TODO: writeResult();
	}

	private void addTXTHeader() {
		this.exportTXT.append("Filename, Status\n");
	}
	
	private void exportTest(Test t) {
		this.exportTXT.append(t.getFilename()+ ", "+ t.getFailed()? "failed" : "passed");
	}
}
