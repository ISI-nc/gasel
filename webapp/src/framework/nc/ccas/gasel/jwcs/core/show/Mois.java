package nc.ccas.gasel.jwcs.core.show;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;

public abstract class Mois extends AbstractComponent {

	@Parameter(required = true)
	public abstract Integer getValue();

	@Parameter(defaultValue = "ognl:false")
	public abstract boolean getShort();

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		DateFormat format = new SimpleDateFormat(getShort() ? "MMM" : "MMMMM", Locale.FRENCH);
		GregorianCalendar cal = new GregorianCalendar(2000, getValue(), 1);
		writer.print(format.format(cal.getTime()));
	}

}
