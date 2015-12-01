package test.input;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import nc.ccas.gasel.input.GaselDateTranslator;

import org.apache.tapestry.valid.ValidatorException;

public class TestGaselDateTranslator extends TestCase {

	private GaselDateTranslator v;

	private final Date refDate = new GregorianCalendar(2003, Calendar.FEBRUARY,
			1).getTime();

	@Override
	protected void setUp() throws Exception {
		v = new GaselDateTranslator();
	}

	private void test(String input) throws ValidatorException {
		assertEquals(refDate, v.parse(null, null, input));
	}

	public void testCourtAvecSep() throws ValidatorException {
		test("1/2/3");
		test("1-2-3");
		test("1.2.3");
		test("1.2-3");
		test("1-2/3");
	}

	public void testLongAvecSep() throws ValidatorException {
		test("01/02/2003");
		test("01-02-2003");
		test("01.02.2003");
	}

	public void testCourtSansSep() throws ValidatorException {
		test("010203");
	}

	public void testLongSansSep() throws ValidatorException {
		test("01022003");
	}

	public void testFormattage() {
		assertEquals("01/02/2003", v.toString(null, refDate));
	}

}
