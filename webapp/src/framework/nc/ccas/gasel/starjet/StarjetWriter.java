package nc.ccas.gasel.starjet;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;

import nc.ccas.gasel.Formats;
import nc.ccas.gasel.utils.StarjetCoder;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.Persistent;

public class StarjetWriter {

	public static final String PAGE_BREAK = "*****";

	private final PrintWriter out;

	public StarjetWriter(Writer out) {
		this.out = new PrintWriter(out);
	}

	public StarjetWriter write(String s) {
		out.write(s);
		return this;
	}

	public StarjetWriter write(Object o) {
		return write(String.valueOf(o));
	}

	public StarjetWriter ln() {
		out.println();
		return this;
	}

	public StarjetWriter pageBreak() {
		return write(PAGE_BREAK).ln();
	}

	public StarjetWriter date(Date date) {
		return write(Formats.DATE_FORMAT.format(date));
	}

	public StarjetWriter coder(String numero) {
		return write(StarjetCoder.coder(numero));
	}

	public StarjetWriter coder(Number numero) {
		return write(StarjetCoder.coder(numero));
	}

	public StarjetWriter printf(String format, Object... args) {
		out.printf(format, args);
		return this;
	}

	public StarjetWriter writeId(Persistent object) {
		return write(DataObjectUtils.intPKForObject(object));
	}

}
