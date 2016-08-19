package org.dmnk.graalJSchakraTD.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;

public interface ListFetcher {

	public Map<String, Integer> fetchFolderList(String file) throws ConfigurationException;
	
	public Map<String, HashMap<String, Integer>> fetchFileList(String file) throws ConfigurationException;
}
