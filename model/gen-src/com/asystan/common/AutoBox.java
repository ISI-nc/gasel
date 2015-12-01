package com.asystan.common;

/**
 * Réimplémentation minimale pour suppression d'une dépendance morte.
 */
public class AutoBox {

	public static boolean valueOf(Boolean value) {
		return value == null ? false : value;
	}

	public static int valueOf(Integer value) {
		return value == null ? 0 : value;
	}

	public static double valueOf(Double value) {
		return value == null ? 0.0 : value;
	}

}
