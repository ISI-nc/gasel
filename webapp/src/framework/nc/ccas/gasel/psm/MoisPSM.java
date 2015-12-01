package nc.ccas.gasel.psm;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.tapestry.form.IPropertySelectionModel;

public class MoisPSM implements IPropertySelectionModel {

	public static final MoisPSM INSTANCE = new MoisPSM();

	private static final Format format = new SimpleDateFormat("MMMMM", Locale.FRANCE);

	public String getLabel(int index) {
		return format.format(new GregorianCalendar(2000, index, 1).getTime());
	}

	public Object getOption(int index) {
		return index + 1;
	}

	public int getOptionCount() {
		return 12;
	}

	public String getValue(int index) {
		return Integer.toString(index + 1);
	}

	public Object translateValue(String value) {
		return Integer.parseInt(value);
	}

	public boolean isDisabled(int index) {
		return false;
	}

}
