package org.dmnk.graalJSchakraTD.classes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.dmnk.graalJSchakraTD.interfaces.ListFetcher;

/**
 * provides the content of the textfiles as maps
 * @author dominik
 *
 */
public class GraalJSListFetcher implements ListFetcher {
	
	Configuration c;
	public GraalJSListFetcher () {
		
	}
	
	public GraalJSListFetcher (Configuration config) {
		c = config;
	}
	
	@Override
	public Map<String, Integer> fetchFolderList(String file) {

		HashMap<String, Integer> fl = new HashMap<String, Integer>();
		
		try (Scanner s = new Scanner(new FileReader(file))){
			while (s.hasNext()) {
				String l = s.nextLine();
				//if /Array is written...
				//TODO maybe additional checks here, to ensure it's just one folder name per line?
				
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fl;
	}
	
	@Override
	public Map<String, HashMap<String, Integer>> fetchFileList(String file) {
		/**
		 * contains the folders (which contain the files)
		 */
		Map<String, HashMap<String, Integer>> folderList;

		folderList = new HashMap<String, HashMap<String, Integer>>();
				
		try (Scanner s = new Scanner(new FileReader(file))){
			while (s.hasNext()) {
				String l = s.nextLine();
				
				String[] in = l.split("/");
				if(l.charAt(0) == '#') {
					//ignore comment lines
					continue;
				}
				if(in.length != 3) { //actually .split without param shout map to split (.., 0), which should ommit the leading empty results
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			//TODO
			//no parameter was set
		}
		
		return folderList;
	}
}
