package nc.ccas.gasel.input;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import nc.ccas.gasel.Formats;

import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.form.translator.AbstractTranslator;
import org.apache.tapestry.valid.ValidatorException;

public class GaselDateTranslator extends AbstractTranslator {

	public static final GaselDateTranslator INSTANCE = new GaselDateTranslator();

	private static final String SEP = "[\\./-]";
	private static final String CH_1_2 = "[0-9]{1,2}";
	private static final String CH_1_4 = "[0-9]{1,4}";

	@Override
	protected String formatObject(IFormComponent field, Locale locale,
			Object object) {
		if (object == null)
			return null;
		return Formats.DATE_FORMAT.format(object);
	}

	@Override
	protected Object parseText(IFormComponent field,
			ValidationMessages messages, String text) throws ValidatorException {
		if (text.matches("[0-9]{6}")) {
			int j = Integer.parseInt(text.substring(0, 2));
			int m = Integer.parseInt(text.substring(2, 4)) - 1;
			int a = Integer.parseInt(text.substring(4, 6));
			a = convertYear(a);
			return composeDate(a, m, j);
		}

		if (text.matches("[0-9]{8}")) {
			int j = Integer.parseInt(text.substring(0, 2));
			int m = Integer.parseInt(text.substring(2, 4)) - 1;
			int a = Integer.parseInt(text.substring(4, 8));
			return composeDate(a, m, j);
		}

		if (text.matches(CH_1_2 + SEP + CH_1_2 + SEP + CH_1_4)) {
			String[] parts = text.split(SEP);
			int j = Integer.parseInt(parts[0]);
			int m = Integer.parseInt(parts[1]) - 1;
			int a = Integer.parseInt(parts[2]);
			a = convertYear(a);
			return composeDate(a, m, j);
		}

		throw new ValidatorException("Format de date inconnu : " + text);
	}

	private int convertYear(int a) {
		if (a < 100) {
			if (a < 30) {
				a += 2000;
			} else {
				a += 1900;
			}
		}
		return a;
	}

	private Date composeDate(int a, int m, int j) {
		return new GregorianCalendar(a, m, j).getTime();
	}

	public String toString(IFormComponent field, Object value) {
		if (value == null)
			return null;
		return Formats.DATE_FORMAT.format(value);
	}

}
