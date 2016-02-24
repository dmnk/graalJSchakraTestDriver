package org.dmnk.graalJSchakraTD.test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.dmnk.graalJSchakraTD.interfaces.TestGroup;

public class GraalJSTestFetcher {

	public static List<TestGroup> fetchFromDir(String chakraPath) {
		List<TestGroup> ltg = new LinkedList<TestGroup>();
		
		FilenameFilter jsFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				String lcName = name.toLowerCase();
				if(lcName.endsWith(".js")) {
					return true;
				} else {
					return false;
				}
			}
		};
		
		File root = new File(chakraPath);
		TestGroup tg = null;
		if(root.canRead() && root.isDirectory()) {
			for(File f : root.listFiles(jsFilter)) {
				String path = new File(f.getParent()).getName();
				if(tg == null) {
					tg = new GraalJSTestGroup(path);
				}
				tg.addTest(new GraalJSTest(f.getName()));
			}
			ltg.add(tg);
		} //else throw new IOException("Provided Test-Dir can't be found or read!");
		
		return ltg;
	}

}
