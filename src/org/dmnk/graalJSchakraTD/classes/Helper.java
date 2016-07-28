package org.dmnk.graalJSchakraTD.classes;

import java.util.HashMap;
import java.util.Map;

public class Helper {
	/**
	 * returns -1 if no Configuration isn't set
	 * @param c
	 * @return
	 */
	public static int getVerbosity(Configuration c) {
		if(c == null) {
			return -1;
		} else {
			return c.getVerbosity();
		}
	}
	
	public static void debugOut(Configuration c, int reqLevel, String area, String message) {
		if(getVerbosity(c) > 0) {
			int vb = getVerbosity(c);
			if(vb >= reqLevel) {
				System.out.println("DEBUG from ["+area+"]: "+message);
			}
		}
	}
}
