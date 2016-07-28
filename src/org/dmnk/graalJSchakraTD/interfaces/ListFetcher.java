package org.dmnk.graalJSchakraTD.interfaces;

import java.util.HashMap;
import java.util.Map;

public interface ListFetcher {

	public Map<String, Integer> fetchFolderList(String file);
	
	public Map<String, HashMap<String, Integer>> fetchFileList(String file);
}
