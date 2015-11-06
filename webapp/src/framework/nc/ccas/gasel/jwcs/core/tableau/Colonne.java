package nc.ccas.gasel.jwcs.core.tableau;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nc.ccas.gasel.Formats;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;

public abstract class Colonne extends AbstractComponent implements
		SourceColonnes, ModeleColonne {

	@Parameter(required = true)
	public abstract String getTitre();

	@Parameter(required = true)
	public abstract Object getValue();

	@Parameter
	public abstract Format getFormat();

	@Parameter(defaultValue = "ognl:false")
	public abstract boolean getHighlight();

	@Parameter(defaultValue = "ognl:false")
	public abstract boolean getDottedTop();

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		// skip
	}

	public List<String> getTitres() {
		return Collections.singletonList(getTitre());
	}

	public List<Cellule> getValeurs() {
		return Collections.singletonList(getValeur());
	}

	public Cellule getValeur() {
		return new Cellule(getValue(), format(), getHighlight(), getDottedTop());
	}

	public List<ModeleColonne> getColonnes() {
		return Collections.singletonList((ModeleColonne) this);
	}

	private Format format() {
		Format format = getFormat();
		if (format != null) {
			return format;
		}
		return DefaultFormat.INSTANCE;
	}

}

class DefaultFormat extends Format {
	private static final long serialVersionUID = -2193754259182304459L;

	public static final Format INSTANCE = new DefaultFormat();

	private DefaultFormat() {
		// skip
	}

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo,
			FieldPosition pos) {
		if (obj == null) {
			toAppendTo.append("--");
		} else if (obj instanceof Number) {
			// Nombres
			Formats.MONTANT.format(obj, toAppendTo, pos);
		} else if (obj instanceof String) {
			// Chaînes
			toAppendTo.append((String) obj);
		} else if (obj instanceof Date) {
			// Dates
			Formats.DATE_FORMAT.format(obj, toAppendTo, pos);
		} else {
			// Fallback
			toAppendTo.append(obj.toString());
		}
		return toAppendTo;
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		throw new NoSuchMethodError();
	}

}