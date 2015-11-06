package test.model.valid;

import java.util.regex.Pattern;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.validator.GaselValidator;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.access.DataContext;

public class ValidatorTest<E extends DataObject> extends TestCase {

	protected final DataContext dc = CayenneUtils.createDataContext();
	protected final GaselValidator<E> validator;

	public ValidatorTest(Class<E> clazz) {
		validator = GaselValidator.validatorFor(clazz);
	}

	protected <T extends DataObject> T create(Class<T> clazz) {
		return clazz.cast(dc.newObject(clazz));
	}

	protected <T extends DataObject> T get(Class<T> clazz, int id) {
		return clazz.cast(DataObjectUtils.objectForPK(dc, clazz, id));
	}

	protected void assertError(String regex) {
		Pattern p = Pattern.compile(regex);
		for (String error : validator.getErrors()) {
			if (p.matcher(error).find())
				return;
		}
		throw new AssertionFailedError("No error matching \"" + regex + "\"");
	}

	protected void assertNoError(String regex) {
		Pattern p = Pattern.compile(regex);
		for (String error : validator.getErrors()) {
			if (p.matcher(error).find())
				throw new AssertionFailedError("Error matching \"" + regex
						+ "\"");
		}
	}

	protected void showErrors() {
		for (String s : validator.getErrors()) {
			System.out.println("ERR: " + s);
		}
	}

}
