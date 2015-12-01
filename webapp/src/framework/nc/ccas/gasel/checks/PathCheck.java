package nc.ccas.gasel.checks;

import nc.ccas.gasel.Check;

import org.apache.cayenne.exp.parser.ASTObjPath;

public class PathCheck<F, T> implements Check<F> {

	private ASTObjPath path;
	private final Class<T> clazz;
	private final Check<? super T> delegate;

	public PathCheck(String path, Class<T> clazz, Check<? super T> delegate) {
		this.clazz = clazz;
		this.delegate = delegate;
		this.path = new ASTObjPath(path);
	}

	public boolean check(F value) {
		Object targetValue = path.evaluate(value);
		if (targetValue instanceof Iterable) {
			// Cas particulier : on arrive sur une liste
			// --> si une valeur est ok, le test est ok
			for (Object v : (Iterable<?>) targetValue) {
				if (delegate.check(clazz.cast(v)))
					return true;
			}
			return false;
		}
		return delegate.check(clazz.cast(targetValue));
	}

}
