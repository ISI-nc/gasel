package com.asystan.common.cayenne;

import static nc.ccas.gasel.modelUtils.DateUtils.sqlTimestamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.parser.ASTAnd;
import org.apache.cayenne.exp.parser.ASTBetween;
import org.apache.cayenne.exp.parser.ASTDbPath;
import org.apache.cayenne.exp.parser.ASTEqual;
import org.apache.cayenne.exp.parser.ASTIn;
import org.apache.cayenne.exp.parser.ASTLike;
import org.apache.cayenne.exp.parser.ASTLikeIgnoreCase;
import org.apache.cayenne.exp.parser.ASTList;
import org.apache.cayenne.exp.parser.ASTNot;
import org.apache.cayenne.exp.parser.ASTObjPath;
import org.apache.cayenne.exp.parser.ASTOr;
import org.apache.cayenne.exp.parser.ASTPath;
import org.apache.cayenne.exp.parser.Node;

/**
 * Réimplémentation minimale pour suppression d'une dépendance morte.
 */
public class QueryFactory {

	public static Expression createEquals(String path, Object value) {
		return new ASTEqual(path(path), value);
	}

	public static Expression createNot(Expression expr) {
		return new ASTNot((Node) expr);
	}

	public static Expression createTrue(String field) {
		return createEquals(field, true);
	}

	public static Expression createIsNull(String field) {
		return createEquals(field, null);
	}

	public static Expression createIn(String field, Collection<?> values) {
		return new ASTIn(path(field), new ASTList(values));
	}

	public static Expression createBetween(String field, Object v1, Object v2) {
		return new ASTBetween(path(field), correctSqlType(v1), correctSqlType(v2));
	}

	public static Expression createContains(String path, String part) {
		return createLikeIgnoreCase(path, "%" + part + "%");
	}

	public static Expression createLike(String path, String pattern) {
		return new ASTLike(path(path), pattern);
	}

	public static Expression createLikeIgnoreCase(String path, String pattern) {
		return new ASTLikeIgnoreCase(path(path), pattern);
	}

	public static Expression createAnd(Expression... expressions) {
		return new ASTAnd(checkedNodes(expressions));
	}

	public static Expression createOr(Expression... expressions) {
		return new ASTOr(checkedNodes(expressions));
	}

	public static Expression createAnd(
			Collection<? extends Expression> expressions) {
		return new ASTAnd(checkedNodes(expressions));
	}

	private static Collection<? extends Node> checkedNodes(
			Expression... objects) {
		return checkedNodes(Arrays.asList(objects));
	}

	/**
	 * Check all objects are nodes, strip nulls, and return that result.
	 */
	private static List<Node> checkedNodes(Collection<?> objects) {
		List<Node> nodes = new ArrayList<Node>(objects.size());
		for (Object object : objects) {
			if (object == null) {
				continue;
			}
			nodes.add((Node) object);
		}
		return nodes;
	}

	private static ASTPath path(String path) {
		if (path.startsWith("db:")) {
			return new ASTDbPath(path.substring("db:".length()));
		}
		return new ASTObjPath(path);
	}

	private static Object correctSqlType(Object v) {
		if (v instanceof Date) {
			return sqlTimestamp((Date) v);
		}
		return v;
	}

}
