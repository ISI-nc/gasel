package nc.ccas.gasel.jwcs.core.edit;

import java.util.Map;
import java.util.TreeMap;

import nc.ccas.gasel.input.GaselDateTranslator;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectScript;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.valid.ValidatorException;

@ComponentClass(reservedParameters = "id,name")
public abstract class Date extends AbstractFormComponent {

	@Parameter(required = true)
	public abstract java.util.Date getValue();

	public abstract void setValue(java.util.Date value);

	@Parameter(defaultValue = "literal:Date")
	public abstract String getTitle();

	@Parameter(defaultValue = "literal:10")
	public abstract Integer getSize();

	/**
	 * @return Ignoré
	 */
	@Parameter
	public abstract Object getType();

	// ------------------------------------------------------------------------

	@Asset("classpath:/org/apache/tapestry/form/DatePickerIcon.png")
	public abstract IAsset getIcon();

	@InjectScript("Date.script")
	public abstract IScript getScript();

	// ------------------------------------------------------------------------
	// Traitement de la saisie
	//

	@Override
	protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		String value = cycle.getParameter(getName());
		try {
			java.util.Date date = (java.util.Date) GaselDateTranslator.INSTANCE
					.parse(this, null, value);
			setValue(date);
		} catch (ValidatorException ex) {
			getForm().getDelegate().record(ex);
		}
	}

	// ------------------------------------------------------------------------
	// Rendu
	//

	@Override
	protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		PageRenderSupport pageRenderSupport = TapestryUtils
				.getPageRenderSupport(cycle, this);
		String name = getName();
		String clientId = getClientId();

		Map<String, Object> symbols = new TreeMap<String, Object>();
		symbols.put("clientId", clientId);
		getScript().execute(this, cycle, pageRenderSupport, symbols);
		String comp = (String) symbols.get("comp");

		// Champ texte
		writer.beginEmpty("input");
		renderIdAttribute(writer, cycle);
		writer.attribute("type", "text");
		writer.attribute("name", name);
		writer.attribute("value", valueAsString());
		writer.attribute("title", getTitle());
		writer.attribute("size", getSize());
		writer.attribute("onchange", //
				(String) symbols.get("fieldChangeHandler"));
		if (isDisabled())
			writer.attribute("disabled", "disabled");
		renderInformalParameters(writer, cycle);

		if (!isDisabled()) {
		
		// Icône calendrier
		writer.printRaw("&nbsp;");
		writer.beginEmpty("img");
		writer.attribute("id", clientId + "_icon");
		writer.attribute("class", "date-icon");
		writer.attribute("src", getIcon().buildURL());
		writer.attribute("alt", "");
		writer.attribute("onclick", (String) symbols
				.get("iconClickHandler"));

		// Div calendrier
		writer.begin("div");
		writer.attribute("id", clientId + "_cal");
		writer.attribute("class", "date-calendar");
		writer.attribute("style", "display:none;position:absolute;");

		renderTable(writer, clientId, (String) symbols.get("tdClickHandler"),
				comp);

		writer.end(); // div _cal
		
		}
	}

	private void renderTable(IMarkupWriter writer, String clientId,
			String tdClickHandler, String comp) {
		// Tableau du calendrier
		writer.begin("table");
		writer.attribute("id", clientId + "_table");
		writer.attribute("class", "date-calendar");

		// Header
		writer.begin("thead");

		writer.begin("tr");
		writer.begin("td");
		writer.attribute("colspan", 8);
		renderMonthSpan(writer, clientId, comp);
		renderYearSpan(writer, clientId, comp);
		writer.end();
		writer.end();

		writer.begin("tr");
		th(writer, "&nbsp;");
		th(writer, "Lu");
		th(writer, "Ma");
		th(writer, "Me");
		th(writer, "Je");
		th(writer, "Ve");
		th(writer, "Sa");
		th(writer, "Di");
		writer.end(); // tr
		writer.end(); // thead

		writer.begin("tbody");
		for (int i = 0; i < 6; i++) {
			writer.begin("tr");
			th(writer, "&nbsp;");
			for (int j = 0; j < 7; j++) {
				writer.begin("td");
				writer.attribute("onclick", tdClickHandler);
				writer.printRaw("&nbsp;");
				writer.end();
			}
			writer.end(); // tr
		}
		writer.end(); // tbody

		writer.end(); // table
	}

	private void renderMonthSpan(IMarkupWriter writer, String clientId,
			String comp) {
		writer.begin("div");
		writer.attribute("style", "float:left;");

		writer.begin("span");
		writer.attribute("style", "cursor:pointer;");
		writer.attribute("onclick", "javascript:" + comp + ".prevMonth()");
		writer.print("<");
		writer.end();

		writer.begin("span");
		writer.attribute("id", clientId + "_mois");
		writer.attribute("style", "width: 8em;");
		writer.printRaw("&nbsp;");
		writer.end(); // span mois

		writer.begin("span");
		writer.attribute("style", "cursor:pointer;");
		writer.attribute("onclick", "javascript:" + comp + ".nextMonth()");
		writer.print(">");
		writer.end();

		writer.end();
	}

	private void renderYearSpan(IMarkupWriter writer, String clientId,
			String comp) {
		writer.begin("div");
		writer.attribute("style", "float:right;");

		writer.begin("span");
		writer.attribute("style", "cursor:pointer;");
		writer.attribute("onclick", "javascript:" + comp + ".prevYear()");
		writer.print("<");
		writer.end();

		writer.begin("span");
		writer.attribute("id", clientId + "_annee");
		writer.attribute("style", "width: 4ex;");
		writer.printRaw("&nbsp;");
		writer.end(); // span annee

		writer.begin("span");
		writer.attribute("style", "cursor:pointer;");
		writer.attribute("onclick", "javascript:" + comp + ".nextYear()");
		writer.print(">");
		writer.end();

		writer.end();
	}

	private void th(IMarkupWriter writer, String value) {
		writer.begin("th");
		writer.printRaw(value);
		writer.end();
	}

	private String valueAsString() {
		return GaselDateTranslator.INSTANCE.format(this, getPage().getLocale(),
				getValue());
	}

}
