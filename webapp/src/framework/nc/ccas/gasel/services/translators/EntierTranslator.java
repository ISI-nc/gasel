package nc.ccas.gasel.services.translators;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.form.translator.AbstractTranslator;
import org.apache.tapestry.valid.ValidatorException;

public class EntierTranslator extends AbstractTranslator {

	public static final NumberFormat FORMAT = DecimalFormat
			.getIntegerInstance(Locale.FRENCH);
	static {
		FORMAT.setMaximumFractionDigits(10);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(new EntierTranslator().parse(null, null, FORMAT.format(2008)));
	}

	public EntierTranslator() {
		setTrim(true);
	}

	public EntierTranslator(String initializer) {
		super(initializer);
		setTrim(true);
	}

	@Override
	protected String formatObject(IFormComponent field, Locale locale,
			Object object) {
		return FORMAT.format(object);
	}

	@Override
	protected Object parseText(IFormComponent field,
			ValidationMessages messages, String text) throws ValidatorException {
		String str = text.replaceAll("[\\s\u00A0]", "");

		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			throw new ValidatorException("\"" + text
					+ "\" n'est pas un nombre entier");
		}
	}

}
