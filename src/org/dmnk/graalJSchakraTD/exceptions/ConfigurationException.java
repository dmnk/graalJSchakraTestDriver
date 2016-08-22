package org.dmnk.graalJSchakraTD.exceptions;

public class ConfigurationException extends Exception {

	/**
	 * thrown when a problem either with, or caused by, the configuration occurs.
	 * 
	 * @author Dominik
	 */
	private static final long serialVersionUID = 1L;

	public ConfigurationException (String error) {
		super(error);
	}
}
