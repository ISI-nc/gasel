package test;

import ognl.ClassResolver;
import ognl.Node;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.enhance.ExpressionAccessor;

import org.apache.tapestry.services.impl.OgnlClassResolver;

public class TestOgnlCompilation {

	public static void main(String[] args) throws Exception {
		Container value = new Container();
		value.setObject(new Class1());

		Container target = new Container();
		target.setObject(value);

		String expression = "object.object.name";

		Node compiledExpression = Ognl.compileExpression(createContext(target),
				target, expression);

		target = new Container();
		value = new Container();
		value.setObject(new Class1());
		target.setObject(value);

		System.out.println("First read:");
		performReads(target, compiledExpression);

		System.out.println();
		value.setObject(new Class2());
		System.out.println("Second read with a different class:");
		performReads(target, compiledExpression);
	}

	private static void performReads(Container target, Node compiledExpression)
			throws OgnlException {
		System.out.println("  - compiledExpression.getValue() gives "
				+ compiledExpression.getValue(createContext(target), target));

		ExpressionAccessor accessor;
		accessor = compiledExpression.getAccessor();
		System.out.println("  - accessor.get() gives "
				+ evalAccessor(target, accessor));
	}

	private static Object evalAccessor(Container target,
			ExpressionAccessor accessor) {
		try {
			return accessor.get(createContext(target), target);
		} catch (Exception e) {
			e.printStackTrace();
			return e;
		}
	}

	private static final ClassResolver _ognlResolver = new OgnlClassResolver();

	private static OgnlContext createContext(Container target) {
		return (OgnlContext) Ognl.createDefaultContext(target, _ognlResolver);
	}

	public static class Container {

		private Object object;

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}
	}

	public static class Class1 {
		public String getName() {
			return "Class1";
		}
	}

	public static class Class2 {
		public String getName() {
			return "Class2";
		}
	}

}
