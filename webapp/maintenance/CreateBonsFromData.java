import static nc.ccas.gasel.modelUtils.DateUtils.intersection;
import static org.apache.cayenne.DataObjectUtils.intPKForObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.collections.Sort;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.AspectAides;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.lang.builder.EqualsBuilder;


public class CreateBonsFromData {

	private static final DateFormat FORMAT_DATE = new SimpleDateFormat(
			"dd/MM/yyyy");

	private static final DateFormat SESSION_FORMAT = new SimpleDateFormat(
			"yyyyMMdd.kkmmss");

	public static void main(String[] args) throws Exception {

		DataContext oc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(oc);

		FileWriter writer = new FileWriter("c:/isinc/bons_dupliques.csv");
		BufferedWriter dupesWriter = new BufferedWriter(writer);

		FileReader fileReader = new FileReader("c:/isinc/bons_manquants.dat");
		BufferedReader reader = new BufferedReader(fileReader);

		Bon bon = null;
		BonData data = null;

		String line;

		int count = 0;
		int found = 0;
		int foundU = 0;
		int foundO = 0;
		int foundP = 0;
		int dupes = 0;

		List<String> errors = new LinkedList<String>();

		while ((line = reader.readLine()) != null) {
			if (line.length() == 0 && data != null) {
				// Fin de bon
				if (bon != null) {
					if (data.matches(bon)) {
						// Si un bon trouvé et correspondant
						found++;
						if (bon.getAide().getStatut().isImmediate()) {
							foundU++;
						} else if (bon.getAide().getStatut()
								.isOccasionnelle()) {
							foundO++;
						} else if (bon.getAide().getStatut().isPlurimensuelle()) {
							foundP++;
						}
					} else {
						writeCsvBon(dupesWriter, bon);
						dupes++;
						bon = null;
					}
				}
				if (bon == null) {
					// Pas de bon correspondant
					try {
						data.createBon(oc);
					} catch (RuntimeException ex) {
						errors.add("[ERREUR] Bon n°" + data.numero + " : "
								+ ex.getMessage());
					}
				}
				data = null;
			}
			// Traitement des champs
			int indexOfColon = line.indexOf(':');
			if (indexOfColon < 0)
				continue; // Pas intéressant
			String prop = line.substring(0, indexOfColon).trim();
			String value = line.substring(indexOfColon + 1).trim();

			if (prop.equals("Bon n°")) {
				count++;
				bon = findBon(oc, value);
				// Numéro de bon
				data = new BonData();
				data.numero = value;
			} else if (data != null) {
				// Action: 4.1.11 Alimentation
				if ("Action".equals(prop)) {
					data.codeAide = value.split(" ")[0];
				}
				// Programme: 1
				else if ("Programme".equals(prop)) {
					data.imputation = Integer.parseInt(value);
				}
				// Date: Lundi 28/06/2010
				else if ("Date".equals(prop)) {
					data.creation = FORMAT_DATE.parse(value.split(" ")[1]);
				}
				// Dossier n°: 20040461
				else if ("Dossier n°".equals(prop)) {
					data.numeroDossier = Integer.parseInt(value);
				}
				// Nom: A MAURI Teriitohoura, Tuarae
				else if ("Nom".equals(prop)) {
					// skip
				}
				// Montant: 3000
				else if ("Montant".equals(prop)) {
					data.montant = Integer.parseInt(value);
				}
				// Date debut: 01/07/2010
				else if ("Date debut".equals(prop)) {
					data.debut = FORMAT_DATE.parse(value);
				}
				// Date fin: 31/07/2010
				else if ("Date fin".equals(prop)) {
					data.fin = FORMAT_DATE.parse(value);
				}
			}
		}
		
		dupesWriter.close();

		System.out.println();
		System.out.println("Stats:");
		System.out.println(" - total = " + count);
		System.out.println(" - found = " + found);
		System.out.println("   - U = " + foundU);
		System.out.println("   - O = " + foundO);
		System.out.println("   - P = " + foundP);
		System.out.println("   - dupes = " + dupes);
		System.out.println();
		System.out.println(" - new objects : " + oc.newObjects().size());

		// System.exit(0); // XXX

		ValidationResult validationResult = new ValidationResult();
		foundU = foundO = foundP = 0;
		for (Object o : oc.newObjects()) {
			if (!(o instanceof Bon))
				continue;

			Bon b = (Bon) o;
			b.validateForInsert(validationResult);

			StatutAide f = bon.getAide().getStatut();
			if (f.isImmediate()) {
				foundU++;
			} else if (f.isOccasionnelle()) {
				foundO++;
			} else if (f.isPlurimensuelle()) {
				foundP++;
			}
		}

		if (validationResult.hasFailures()) {
			System.out.println("--- FAILURES ---");
			for (Object o : validationResult.getFailures()) {
				System.out.println(" - " + o);
			}
			System.exit(1);
		}

		System.out.println("   - U = " + foundU);
		System.out.println("   - O = " + foundO);
		System.out.println("   - P = " + foundP);

		System.out.println(" - ERRORS : " + errors.size());
		for (String error : errors) {
			System.out.println(error);
			System.out.println("-- -- -- --");
		}

		//oc.commitChanges();
	}

	private static void writeCsvBon(BufferedWriter out, Bon bon) throws IOException {
		out.append("'" + bon.getNumero());
		out.append(";");
		out.append(FORMAT_DATE.format(bon.getDebut()));
		out.append(";");
		out.append(FORMAT_DATE.format(bon.getFin()));
		out.append(";");
		out.append(bon.getAide().getCode());
		out.append(";");
		out.append(String.valueOf(intPKForObject(bon.getAide().getDossier())));
		out.newLine();
	}

	private static Bon findBon(DataContext oc, String numero) {
		return CommonQueries.unique(oc, Bon.class, "numero", numero);
	}

	private static class BonData {
		String numero;

		String codeAide;

		Integer imputation;

		Integer numeroDossier;

		Integer montant;

		Date creation;

		Date debut;

		Date fin;

		Bon createBon(DataContext dc) {
			Dossier dossier = findDossier(dc);
			Aide aide = findAide(dossier);

			Bon bon = (Bon) dc.newObject(Bon.class);
			bon.setAide(aide);
			bon.setDebut(debut);
			bon.setEtat(EtatBon.EDITE);
			bon.setFin(fin);
			bon.setModifDate(creation);
			bon.setModifUtilisateur(aide.getModifUtilisateur());
			bon.setMontant(montant);
			bon.setNumero(numero);
			bon.setPersonne(dossier.getChefFamille());
			bon.setSessionEdition("EM_" + SESSION_FORMAT.format(new Date())
					+ "isinc");

			return bon;
		}

		public boolean matches(Bon bon) {
			return new EqualsBuilder() //
					.append(debut, bon.getDebut()) //
					.append(fin, bon.getFin()) //
					.append(codeAide, bon.getAide().getCode()) //
					.append(montant, bon.getMontant()) //
					.append(numero, bon.getNumero()) //
					.append(numeroDossier.intValue(),
							intPKForObject(bon.getAide().getDossier())) //
					.isEquals();
		}

		private Dossier findDossier(DataContext dc) {
			Dossier dossier = CommonQueries.findById(Dossier.class,
					numeroDossier, dc);
			if (dossier == null) {
				throw new RuntimeException("Dossier non trouvé : "
						+ numeroDossier);
			}
			return dossier;
		}

		private Aide findAide(Dossier dossier) {
			AspectAides aspect = dossier.getAspect(AspectAides.class);
			for (Aide aide : aspect.getAides()) {
				if (!intersection(debut, fin, aide.getDebut(), aide.getFin())) {
					continue;
				}
				if (!codeAide.equals(aide.getCode())) {
					continue;
				}
				if (!aide.getNature().getImputation().getId()
						.equals(imputation)) {
					continue;
				}
				return aide;
			}
			String periodes = "";
			for (Aide aide : new Sort<Aide>(aspect.getAides()).by("debut").by(
					"fin").results()) {
				if (!codeAide.equals(aide.getCode())) {
					continue;
				}
				periodes += String.format("\n   - %s - %s", //
						FORMAT_DATE.format(aide.getDebut()), //
						FORMAT_DATE.format(aide.getFin()));
			}
			throw new RuntimeException("Pas d'aide trouvée :\n" //
					+ " - dossier n°   " + numeroDossier + "\n" //
					+ " - code aide :  " + codeAide + "\n" //
					+ " - imputation : " + imputation + "\n" //
					+ " - début :      " + FORMAT_DATE.format(debut) + "\n" //
					+ " - fin :        " + FORMAT_DATE.format(fin) + "\n" //
					+ "\n" //
					+ " - périodes d'aide pour ce code : " + periodes //
			);
		}
	}

}
