package org.dmnk.graalJSchakraTD.test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.dmnk.graalJSchakraTD.interfaces.TestGroup;

public class GraalJSTestFetcher {
	private FilenameFilter dirFilter, jsFilter;
	
	public GraalJSTestFetcher() {
		dirFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return dir.isDirectory();
			}
		};
		
		jsFilter = new FilenameFilter() {
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
	}

	public List<TestGroup> fetchFromDir(String chakraPath) {
		List<TestGroup> ltg = new LinkedList<TestGroup>();		
		
		File root = new File(chakraPath);
		TestGroup tg = null;
		if(root.canRead() && root.isDirectory()) {
			if(root.listFiles(jsFilter).length == 0) {
				//NO js files in current directory, expecting to be in the main dir of the chakra tests
				for(File d : root.listFiles(dirFilter)) {
					tg = addJSofFolder(d);
					ltg.add(tg);
				}
			} else {
				// expecting to be in a test-subfolder
				tg = addJSofFolder(root);
				ltg.add(tg);
			}
		} //else throw new IOException("Provided Test-Dir can't be found or read!");
		
		return ltg;
	}
	
	private TestGroup addJSofFolder(File directory) {
		TestGroup tg = null;
		for(File f : directory.listFiles(jsFilter)) {
			String path = new File(f.getParent()).getName();
			if(tg == null) {
				tg = new GraalJSTestGroup(path);
			}
			tg.addTest(new GraalJSTest(f.getName()));
		}
		return tg;
	}

}
