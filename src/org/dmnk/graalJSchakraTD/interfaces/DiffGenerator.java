package org.dmnk.graalJSchakraTD.interfaces;

/**
 * finds the differences between string 1 and 2 and returns them in 
 * unified patch format
 * @author dominik
 *
 */
public interface DiffGenerator {
	public String  getDiff(String original, String actual);
}
