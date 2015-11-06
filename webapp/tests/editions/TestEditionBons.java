package editions;

import static editions.AideTestsSample.bon1;
import static editions.AideTestsSample.bon2;
import static nc.ccas.gasel.starjet.StarjetWriter.PAGE_BREAK;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.starjet.aides.EditionBons;

public class TestEditionBons extends TestCase {

	private static final String REGEX_PAGE_BREAK = PAGE_BREAK.replace("*",
			"\\*");

	/**
	 * Asserts that a string matches a regular expression.
	 */
	public static void assertMatch(String regexp, String actual) {
		if (!actual.matches(regexp))
			throw new AssertionFailedError("Match expected:<" + regexp
					+ "> but was:<" + actual + ">");
	}

	private static final String OUTPUT_BON1 = "" //
			+ "Bon n°: 00000001\n" // Numéro du bon
			+ "n° bon code: 1\n" // numéro du bon codé base 64
			+ "Action: 1 Nature de l\'aide\n" // Nature de l'aide (ID+libelle)
			+ "Programme: 1\n" // ID de l'imputation
			+ "Date: ........ ../../....\n" // Date d'émission
			+ "Dossier n°: 1\n" //
			+ "Nom: Nom personne Prénom personne\n" //
			+ "Montant: 1000\n" //
			+ "Date debut: 01/08/2008\n" //
			+ "Date fin: 31/08/2008\n" //
			// Ligne frn 1
			+ "Fournisseur 1                                Fournisseur 2\n"
			// Ligne frn2
			+ "Fournisseur 3                                \n" //
			+ "\n" // ligne 3
			+ "\n" // ligne 4
			+ "\n" // ligne 5
			+ "\n" // ligne 6
	;

	private static final String OUTPUT_BON2 = "" //
			+ "Bon n°: 00000002\n" // Numéro du bon
			+ "n° bon code: 2\n" // numéro du bon codé base 64
			+ "Action: 1 Nature de l\'aide\n" // Nature de l'aide (ID+libelle)
			+ "Programme: 1\n" // ID de l'imputation
			+ "Date: ........ ../../....\n" // Date d'émission
			+ "Dossier n°: 1\n" //
			+ "Nom: Nom personne Prénom personne\n" //
			+ "Montant: 1000\n" //
			+ "Date debut: 01/08/2008\n" //
			+ "Date fin: 31/08/2008\n" //
			// Ligne frn 1
			+ "Fournisseur 1                                Fournisseur 2\n"
			// Ligne frn2
			+ "Fournisseur 3                                \n" //
			+ "\n" // ligne 3
			+ "\n" // ligne 4
			+ "\n" // ligne 5
			+ "\n" // ligne 6
	;

	public void testUnBon() throws Exception {
		String output = process(bon1);
		System.out.println(output);
		assertMatch(OUTPUT_BON1, output);
	}

	public void testDeuxBons() throws Exception {
		String output = process(bon1, bon2);
		assertMatch(OUTPUT_BON1 + REGEX_PAGE_BREAK + OUTPUT_BON2, output);
	}

	private String process(Bon... bons) {
		return process(Arrays.asList(bons));
	}

	private String process(List<Bon> bons) {
		EditionBons edition = new EditionBons();
		StringWriter out = new StringWriter();
		edition.write(out, bons);
		String output = out.toString();
		return output;
	}

}
