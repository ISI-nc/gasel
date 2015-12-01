package test;

import static nc.ccas.gasel.French.nombreToutesLettres;
import junit.framework.TestCase;
import nc.ccas.gasel.French;

public class TestFrench extends TestCase {

	public void testNombreToutesLettres() throws Exception {
		assertEquals("un", French.nombreToutesLettres(1));

		assertEquals("soixante-et-un", nombreToutesLettres(61));
		assertEquals("soixante-et-onze", nombreToutesLettres(71));
		assertEquals("quatre-vingt un", nombreToutesLettres(81));
		assertEquals("quatre-vingt onze", nombreToutesLettres(91));

		assertEquals("cent", nombreToutesLettres(100));
		assertEquals("cent un", nombreToutesLettres(101));
		assertEquals("cent soixante douze", nombreToutesLettres(172));
		assertEquals("deux cent", nombreToutesLettres(200));
		assertEquals("deux cent quatre-vingt deux", nombreToutesLettres(282));
		
		assertEquals("deux mille un", nombreToutesLettres(2001));
		
		assertEquals("un million", nombreToutesLettres((long) 1e6));
		assertEquals("deux millions", nombreToutesLettres((long) 2e6));
		
		assertEquals("un milliard", nombreToutesLettres((long) 1e9));
		assertEquals("deux milliards", nombreToutesLettres((long) 2e9));
		
		assertEquals("deux cent dix-sept mille six cent cinquante", nombreToutesLettres(217650));
		assertEquals("deux cent dix-sept mille six cent cinquante-et-un", nombreToutesLettres(217651));
		assertEquals("deux cent dix-sept mille six cent cinquante neuf", nombreToutesLettres(217659));
	}

}
