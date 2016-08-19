package org.dmnk.graalJSchakraTD.classes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.dmnk.graalJSchakraTD.exceptions.ConfigurationException;
import org.dmnk.graalJSchakraTD.interfaces.ListFetcher;

/**
 * provides the content of the textfiles as maps.
 * maps are used because of the convenient contains function, 
 * the actual value is 0 for each entry!
 * 
 * @author dominik
 *
 */
public class ListFetcherGeneric implements ListFetcher {
	
	Configuration c;
	
	public ListFetcherGeneric () {
		
	}
	
	public ListFetcherGeneric (Configuration config) {
		c = config;
	}
	
	@Override
	public Map<String, Integer> fetchFolderList(String file) throws ConfigurationException {

		HashMap<String, Integer> fl = new HashMap<String, Integer>();

		try (Scanner s = new Scanner(new FileReader(file))){
			while (s.hasNext()) {
				String l = s.nextLine();
				
				//if /Array is written...
				l.replace("/", "");

				if(l.charAt(0) == '#') {
					//ignore comment lines
					continue;
				}
				if(!fl.containsKey(l)) {
					fl.put(l, 0);
				}
			}
		} catch (FileNotFoundException e) {
			throw new ConfigurationException("the list requested under " +e.getMessage() +" could not be accessed!");
		}
		
		return fl;
	}
	
	@Override
	public Map<String, HashMap<String, Integer>> fetchFileList(String file) throws ConfigurationException {
		/**
		 * contains the folders (which contain the files)
		 */
		Map<String, HashMap<String, Integer>> folderList;

		folderList = new HashMap<String, HashMap<String, Integer>>();
		
		if (file == null || file.length() == 0) {
			//early stop, no parameter was set
			return folderList;
		}
		
		try (Scanner s = new Scanner(new FileReader(file))){
			while (s.hasNext()) {
				String l = s.nextLine();
				
				String[] in = l.split("/");
				if(l.charAt(0) == '#') {
					//ignore comment lines
					continue;
				}
				if(in.length != 3) { //actually .split without param should map to split (.., 0), which should omit the leading empty results
					Helper.debugOut(c, 0, "LF-Issue", "Fetching failed for " + file + "; invalid entry: " +l);
					continue;
				}
				
				if(!folderList.containsKey(in[1])) {
					//folder doesn't exist -- create
					folderList.put(in[1], new HashMap<String, Integer>());
				}
				if(!folderList.get(in[1]).containsKey(in[2])) {
					//file entry doesn't exist -- add
					folderList.get(in[1]).put(in[2], 0);
				}
			}
		} catch (FileNotFoundException e) {
			throw new ConfigurationException("the list requested under " +e.getMessage() +" could not be accessed!");
		}
		
		return folderList;
	}
}
