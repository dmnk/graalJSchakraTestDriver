package org.dmnk.graalJSchakraTD.interfaces;

import java.util.List;

public interface ResultExporter {
	public void setExportPath(String path);
	public void export(List<TestExecutedGroup> executedTests);
}
