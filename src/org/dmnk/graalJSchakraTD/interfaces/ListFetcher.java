package org.dmnk.graalJSchakraTD.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;

/**
 * returns the content of files, either as Map containing the foldernames as string-key, or as
 * Map in a Map, containing the filename as string-key, where the outer map is holding the folder 
 * 
 * @author dominik
 *
 */
public interface ListFetcher {

	public Map<String, Integer> fetchFolderList(String file) throws ConfigurationException;
	
	public Map<String, HashMap<String, Integer>> fetchFileList(String file) throws ConfigurationException;
}
