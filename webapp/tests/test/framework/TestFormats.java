package test.framework;

import java.text.NumberFormat;

import junit.framework.TestCase;
import nc.ccas.gasel.Formats;

public class TestFormats extends TestCase {

	public void testFormatMontant() {
		NumberFormat format = Formats.INSTANCE.getMontant();

		assertEquals("1 000", format.format(1000));
		assertEquals("1 000 000", format.format(1000000));
		assertEquals("1 000,12", format.format(1000.12));
	}

}
