package com.asystan.common.beans;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Réimplémentation minimale pour suppression d'une dépendance morte.
 */
public class BeanUtils {
	
	public static boolean nullSafeEquals(Object o1, Object o2) {
		return new EqualsBuilder().append(o1, o2).isEquals();
	}

}
