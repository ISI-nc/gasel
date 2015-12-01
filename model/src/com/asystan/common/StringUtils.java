package com.asystan.common;

import java.util.Collection;

/**
 * Réimplémentation minimale pour suppression d'une dépendance morte.
 */
public class StringUtils {

	public static String join(String joinString, Collection<String> strings) {
		return org.apache.commons.lang.StringUtils.join(strings.iterator(), joinString);
	}

	public static String join(String joinString, String[] strings) {
		return org.apache.commons.lang.StringUtils.join(strings, joinString);
	}
	
	

}
