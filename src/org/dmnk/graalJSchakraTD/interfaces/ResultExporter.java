package org.dmnk.graalJSchakraTD.interfaces;

import java.util.List;

/**
 * (ideally) wraps the content of the provided test-list into a nice-human readable format
 * @author dominik
 *
 */
public interface ResultExporter {
	public void setExportPath(String path);
	public void export(List<TestExecutedGroup> executedTests);
}
