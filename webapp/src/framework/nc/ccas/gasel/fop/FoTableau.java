package nc.ccas.gasel.fop;

import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.Formats;
import nc.ccas.gasel.services.fop.FoContributor;
import nc.ccas.gasel.services.fop.FoElement;
import nc.ccas.gasel.services.fop.FoTable;
import nc.ccas.gasel.services.fop.FoTableBody;
import nc.ccas.gasel.services.fop.FoTableCell;
import nc.ccas.gasel.services.fop.FoTableHeader;
import nc.ccas.gasel.services.fop.FoTableRow;

import org.apache.tapestry.IMarkupWriter;

public class FoTableau extends FoContributor {

	private static final Object[] CELL_ATTRIBUTES = new Object[] { "padding",
			"1pt 2pt" };

	private FoTable table;

	private final List<String> columnTitles = new LinkedList<String>();

	private final List<Object[]> columnAttrs = new LinkedList<Object[]>();

	private final List<Format> formats = new ArrayList<Format>();

	private FoTableBody body;

	private FoTableRow currentRow;

	private boolean allowMonospace = true;

	public FoTableau(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoTableau(IMarkupWriter writer) {
		super(writer);
	}

	public FoTableau column(String title, Object... attributes) {
		return column(title, null, attributes);
	}

	public FoTableau column(String title, Format format, Object... attributes) {
		columnTitles.add(title);
		formats.add(format);
		columnAttrs.add(prependAttributes(attributes, //
				"border-right", "1pt solid black"));
		return this;
	}

	public int columnCount() {
		return columnTitles.size();
	}

	private FoTableau begin() {
		table = new FoTable(writer, xmlns).begin("border", "1pt solid black");

		// Columns definition
		for (Object[] attrs : columnAttrs) {
			table.column(attrs);
		}

		// Header
		FoTableHeader header = table.header();
		header.cellAttributes(CELL_ATTRIBUTES);
		FoTableRow row = header.row("border-bottom", "1pt solid black",
				"font-weight", "bold");

		for (String title : columnTitles) {
			row.textCell(title);
		}

		row.end();
		header.end();

		return this;
	}

	private void beginBody() {
		// Start the body
		body = table.body();
		body.cellAttributes(CELL_ATTRIBUTES);
		body.rowAttributes("border-bottom", "1px dotted black");
	}

	public void end() {
		if (table == null)
			return;

		body.end();
		body = null;

		table.end();
		table = null;
	}

	public FoTableau row(Object... values) {
		manualRow();
		for (int i = 0; i < formats.size(); i++) {
			Object value = values[i];

			autoCell(i, value);
		}
		currentRow.end();

		return this;
	}

	public void autoCell(int i, Object value, Object... attributes) {
		FoTableCell cell = currentRow.cell(attributes);
		if (value != null) {
			FoElement block = cell.child("block");
			contributeStyle(value);

			String valueString;

			if (value instanceof String
					&& ((String) value).trim().length() == 0) {
				valueString = Formats.NBSP;
			} else {
				Format format = formats.get(i);
				if (format == null)
					format = defaultFormat(value);

				if (format == null) {
					valueString = String.valueOf(value);
				} else {
					valueString = format.format(value);
				}
				if (valueString.trim().length() == 0)
					valueString = Formats.NBSP;
			}

			block.print(valueString);
			block.end();
		}
		cell.end();
	}

	public FoTableRow manualRow(Object... attributes) {
		if (table == null)
			begin();

		if (body == null)
			beginBody();

		currentRow = body.row(attributes);
		currentRow.cellAttributes(CELL_ATTRIBUTES);
		return currentRow;
	}

	private void contributeStyle(Object value) {
		if (value instanceof Number) {
			writer.attribute("text-align", "right");
			if (allowMonospace)
				writer.attribute("font-family", "monospace");
		} else if (value instanceof Date) {
			writer.attribute("text-align", "center");
			if (allowMonospace)
				writer.attribute("font-family", "monospace");
		}
	}

	private Format defaultFormat(Object value) {
		if (value instanceof Number) {
			return Formats.MONTANT;
		} else if (value instanceof Date) {
			return Formats.DATE_FORMAT;
		}
		return null;
	}

	public boolean isAllowMonospace() {
		return allowMonospace;
	}

	public void setAllowMonospace(boolean allowMonospace) {
		this.allowMonospace = allowMonospace;
	}

}
