package org.dmnk.graalJSchakraTD.classes;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.dmnk.graalJSchakraTD.classes.test.GenericTest;
import org.dmnk.graalJSchakraTD.classes.test.GenericTestGroup;
import org.dmnk.graalJSchakraTD.enums.TestType;
import org.dmnk.graalJSchakraTD.interfaces.TestFetcher;
import org.dmnk.graalJSchakraTD.interfaces.TestGroup;

public class TestFetcherGeneric implements TestFetcher {
	private FilenameFilter dirFilter, jsFilter;
	private File testDir;
	
	public TestFetcherGeneric(String directory) {
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
		
		testDir = new File(directory);
	}
	
	@Override
	public List<TestGroup> fetch() {
		return fetchFromDir(testDir.getAbsolutePath());
	}
	
	public List<TestGroup> fetchFromDir(String chakraPath) {
		List<TestGroup> ltg = new LinkedList<TestGroup>();		
		
		File root = new File(chakraPath);
		TestGroup tg = null;
		//TODO: throw some exceptions if root can't be read, no tests found, ...
		if(root.canRead() && root.isDirectory()) {			
			if(root.listFiles(jsFilter).length == 0) {
				//NO js files in current directory, expecting to be in the main dir of the chakra tests
				for(File d : root.listFiles(dirFilter)) {
					try {
						//TODO: WHY are there files (jenkings.build.cmd) passed to the addJSofFolder 
						// after applying the dirFilter
						tg = addJSofFolder(d);
						ltg.add(tg);
					} catch( IOException e) {
						;
					}
				}
			} else {
				// expecting to be in a test-subfolder
				try {
					tg = addJSofFolder(root);
					ltg.add(tg);
				} catch (IOException e) {
					;
				}
			}
		} //else throw new IOException("Provided Test-Dir can't be found or read!");
//		ltg.sort(c);
		return ltg;
	}
	
	private TestGroup addJSofFolder(File directory) throws IOException {
		TestGroup tg = null;
		
		if(!directory.isDirectory()) {
			throw new IOException(directory.getName() + " isn't a Directory!");
		}
		
		for(File f : directory.listFiles(jsFilter)) {
			String path = new File(f.getParent()).getName();
			if(tg == null) {
				tg = new GenericTestGroup(path);
			}			
			
			if(baselineExists(f)) {
				tg.addTest(new GenericTest(f.getPath(), TestType.BASELINE));
			} else {
				tg.addTest(new GenericTest(f.getPath(), TestType.PASSSTRING));
			}
		}
		return tg;
	}
	
	private boolean baselineExists(File f) {
		StringBuilder baseLinePath = new StringBuilder (f.getPath().substring(0, f.getPath().length()-2));
		baseLinePath.append("baseline");
//		File baseline = new File(f.getPath().replace(".js", ".baseline"));
		File baseline = new File(baseLinePath.toString());
//		if(f.getName().contains("FailToSetLength")){
//			int i = 1+f.getName().length();
//			i=i+2;
//			//debug jump in
//		}
		if(baseline.exists()) {
			return true;
		} else {
			return false;
		}
	}

}
