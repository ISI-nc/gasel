package nc.ccas.gasel.reports.dossiers.actualite;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nc.ccas.gasel.BasePageSort;
import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.paph.AccompagnementPAPH;
import nc.ccas.gasel.model.paph.AspectDossierPAPH;
import nc.ccas.gasel.model.paph.DossierPAPH;
import nc.ccas.gasel.model.paph.HandicapPAPH;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;
import nc.ccas.gasel.reports.PeriodePropsBean;
import nc.ccas.gasel.reports.StatsColonne;
import nc.ccas.gasel.reports.StatsTableau;
import nc.ccas.gasel.reports.StatsTransform;
import nc.ccas.gasel.xls.CellStyles;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ActualiteReport {

	public static void main(String[] args) throws IOException {
//		AllModelTests.setupDatabase("noumea");
		DataContext.bindThreadDataContext(CayenneUtils.createDataContext());

		BasePageSql sql = new BasePageSql();

		creerRapport(sql, 2008, 2008);
		creerRapport(sql, 2009, 2009);
		creerRapport(sql, 2010, 2010);

		System.out.println("Fin.");
	}

	// -----

	private static final String AIDES_QUERY = "debut <= $fin and fin >= $debut";

	private static final StatsTableau<Dossier> TABLEAU_DOSSIERS = new StatsTableau<Dossier>() //
			.add("N° GASEL", new StatsTransform<Dossier>() {
				public Object transform(Dossier input) {
					return input.getId();
				}
			}) //

			.add("ANNEE ENTREE", new StatsTransform<Dossier>() {
				public Object transform(Dossier input) {
					return DateUtils.gc(input.getDateOuverture()) //
							.get(Calendar.YEAR);
				}
			}) //

			.add("TYPE HABITAT", new StatsTransform<Dossier>() {
				public Object transform(Dossier input) {
					return String.valueOf(input.getTypeHabitat());
				}
			}) //

			.add("ADRESSE", new StatsTransform<Dossier>() {
				public Object transform(Dossier input) {
					return input.getAdresseHabitation().toString();
				}
			}) //

			.add("QUARTIER", new StatsTransform<Dossier>() {
				public Object transform(Dossier input) {
					return "???"; // TODO NOT FOUND
				}
			}) //

			.add("REVENU GLOBAL", new StatsTransform<Dossier>() {
				public Object transform(Dossier input) {
					return input.getTotalRessources();
				}
			}) //

			.add("NOMBRE PERSONNES", new StatsTransform<Dossier>() {
				public Object transform(Dossier input) {
					return input.getNbPersonnes();
				}
			}) //

			.add("DATE NAISSANCE ENFANTS", new StatsTransform<Dossier>() {
				public Object transform(Dossier input) {
					StringBuilder buf = new StringBuilder();
					for (Personne p : input.getEnfantsACharge()) {
						if (buf.length() > 0) buf.append(", ");
						buf.append(DateUtils.DATE_FORMAT.format(p.getDateNaissance()));
					}
					return buf.toString();
				}
			}) //
	;

	private static final StatsTableau<Personne> TABLEAU_CHEFS = new StatsTableau<Personne>() //
			.add("N° GASEL", new StatsTransform<Personne>() {
				public Object transform(Personne input) {
					return input.getDossiersChefFamille().get(0).getId();
				}
			}) //

			.add("SEXE", new StatsTransform<Personne>() {
				public Object transform(Personne input) {
					return input.isHomme() ? "H" : "F";
				}
			}) //

			.add("DATE NAISSANCE", new StatsTransform<Personne>() {
				public Object transform(Personne input) {
					return input.getDateNaissance();
				}
			}) //

			.add("VILLE NAISSANCE", new StatsTransform<Personne>() {
				public Object transform(Personne input) {
					return String.valueOf(input.getVilleNaissance());
				}
			}) //

			.add("SITUATION FAMILIALE", new StatsTransform<Personne>() {
				public Object transform(Personne input) {
					return String.valueOf(input.getSituationFamiliale());
				}
			}) //

			.add("STATUT LOGEMENT", new StatsTransform<Personne>() {
				public Object transform(Personne input) {
					return String.valueOf(input.getStatut());
				}
			}) //

			.add("HANDICAP", new StatsTransform<Personne>() {
				public Object transform(Personne input) {
					return input.getHandicap() ? "Oui" : "Non";
				}
			}) //

			.add("NATIONALITE", new StatsTransform<Personne>() {
				public Object transform(Personne input) {
					return input.getNationaliteFrancaise() ? "FRANCAISE"
							: "AUTRE";
				}
			}) //

			.add("DATE ARRIVEE TERRITOIRE", new StatsTransform<Personne>() {
				public Object transform(Personne input) {
					return input.getDateArriveeSurTerritoire();
				}
			}) //
	;

	private static final StatsTableau<Aide> TABLEAU_AIDES = new StatsTableau<Aide>() //
			.add("N° GASEL", new StatsTransform<Aide>() {
				public Object transform(Aide input) {
					return input.getDossier().getDossier().getId();
				}
			}) //

			.add("SECTEUR", new StatsTransform<Aide>() {
				public Object transform(Aide input) {
					return input.getNature().getParent().getLibelle();
				}
			}) //

			.add("NATURE", new StatsTransform<Aide>() {
				public Object transform(Aide input) {
					return input.getNature().getLibelle();
				}
			}) //

			.add("TYPE", new StatsTransform<Aide>() {
				public Object transform(Aide input) {
					return input.getCode();
				}
			}) //

			.add("DEMANDE PONCTUELLE", new StatsTransform<Aide>() {
				public Object transform(Aide input) {
					return input.getStatut().getLibelle();
				}
			}) //

			.add("PUBLIC", new StatsTransform<Aide>() {
				public Object transform(Aide input) {
					return input.getPublic().getLibelle();
				}
			}) //

			.add("MONTANT", new StatsTransform<Aide>() {
				public Object transform(Aide input) {
					return input.getMontant();
				}
			}) //

			.add("PERIODE (DEBUT)", new StatsTransform<Aide>() {
				public Object transform(Aide input) {
					return input.getDebut();
				}
			}) //

			.add("PERIODE (FIN)", new StatsTransform<Aide>() {
				public Object transform(Aide input) {
					return input.getFin();
				}
			}) //

			.add("NB ADMIN", new StatsTransform<Aide>() {
				public Object transform(Aide input) {
					return input.getDossier().getDossier().getNbPersonnes();
				}
			}) //
	;

	private static final StatsTableau<HandicapPAPH> TABLEAU_PAPH = new StatsTableau<HandicapPAPH>() //
			.add("N° GASEL", new StatsTransform<HandicapPAPH>() {
				public Object transform(HandicapPAPH input) {
					return input.getDossier().getDossier().getDossier().getId();
				}
			}) //

			.add("TYPE HANDICAP", new StatsTransform<HandicapPAPH>() {
				public Object transform(HandicapPAPH input) {
					return input.getHandicap().getLibelle();
				}
			}) //

			.add("NIVEAU HANDICAP", new StatsTransform<HandicapPAPH>() {
				public Object transform(HandicapPAPH input) {
					return "???"; // TODO NOT FOUND
				}
			}) //

			.add("TAUX HANDICAP", new StatsTransform<HandicapPAPH>() {
				public Object transform(HandicapPAPH input) {
					return input.getTaux().getLibelle();
				}
			}) //

			.add("MOTIF ACCOMPAGNEMENT", new StatsTransform<HandicapPAPH>() {
				public Object transform(HandicapPAPH input) {
					StringBuilder buf = new StringBuilder();
					for (AccompagnementPAPH acc : input.getDossier().getAccompagnements()) {
						if (buf.length() > 0) buf.append("; ");
						buf.append(acc.getProjet());
					}
					return buf.toString();
				}
			}) //
	;

	// -----

	private final BasePageSql sql;

	private final PeriodeProps periode;

	private List<Dossier> dossiers;

	private List<Aide> aides;

	private List<Personne> chefsMenage;

	private List<HandicapPAPH> paPh;
	
	private static void creerRapport(BasePageSql sql, int anneeDebut,
			int anneeFin) throws FileNotFoundException, IOException {
		ActualiteReport report = new ActualiteReport(sql, anneeDebut, anneeFin);

		HSSFWorkbook wb = new HSSFWorkbook();
		// Titre de la page = "Tableau" parce que trop de contraintes (et donc
		// de problèmes) sur les titres libres...

		createSheetTableau(wb, "Menage", TABLEAU_DOSSIERS, report.getDossiers());
		createSheetTableau(wb, "Chef menage", TABLEAU_CHEFS, report
				.getChefsMenage());
		createSheetTableau(wb, "Aide", TABLEAU_AIDES, report.getAides());
		createSheetTableau(wb, "PAPH", TABLEAU_PAPH, report.getPaPh());

		// Ecriture
		System.out.println("Ecriture " + anneeDebut + " - " + anneeFin + "...");
		OutputStream out = new FileOutputStream("stats " + anneeDebut + " - "
				+ anneeFin + ".xls");
		wb.write(out);
		out.close();
	}

	private static <T> void createSheetTableau(HSSFWorkbook wb,
			String sheetName, StatsTableau<T> tableau, List<T> values) {
		HSSFSheet sheet = wb.createSheet(sheetName);
		CellStyles styles = new CellStyles(wb);

		HSSFRow row;

		int colCount = tableau.getColonnes().size();

		int rowNum = 0;
		short colNum = 0;
		row = sheet.createRow(rowNum++);
		for (StatsColonne<T> col : tableau.getColonnes()) {
			HSSFCell cell = row.createCell(colNum++);
			cell.setCellStyle(styles.title);
			setCellValue(cell, col.getTitre());
		}
		System.out.println();

		for (T value : values) {
			row = sheet.createRow(rowNum++);
			colNum = 0;
			for (StatsColonne<T> col : tableau.getColonnes()) {
				HSSFCell cell = row.createCell(colNum++);
				cell.setCellStyle(styles.cell(false, rowNum == 0,
						value instanceof Date, colNum == colCount));
				setCellValue(cell, col.valueFor(value));
			}
		}
	}

	private static void setCellValue(HSSFCell cell, Object value) {
		if (value == null) {
			cell.setCellValue(new HSSFRichTextString("--"));
			return;
		} else if (value instanceof String) {
			cell.setCellValue(new HSSFRichTextString((String) value));
		} else if (value instanceof Date) {
			cell.setCellValue((Date) value);
		} else if (value instanceof Number) {
			cell.setCellValue(((Number) value).doubleValue());
		} else {
			throw new IllegalArgumentException( //
					value.getClass().toString() + ": " + value.toString());
		}
	}

	public ActualiteReport(BasePageSql sql, PeriodeProps periode) {
		this.sql = sql;
		this.periode = periode;
	}

	public ActualiteReport(BasePageSql sql, Date debut, Date fin) {
		this(sql, new PeriodePropsBean(debut, fin));
	}

	public ActualiteReport(BasePageSql sql, int anneeDebut, int anneeFin) {
		this(sql, DateUtils.annee(anneeDebut)[0], DateUtils.annee(anneeFin)[1]);
	}

	public List<Dossier> getDossiers() {
		if (dossiers == null) {
			Set<Dossier> set = new TreeSet<Dossier>(new Comparator<Dossier>() {
				public int compare(Dossier o1, Dossier o2) {
					return sql.idOf(o1).compareTo(sql.idOf(o2));
				}
			});
			for (Aide aide : getAides()) {
				set.add(aide.getDossier().getDossier());
			}
			dossiers = new ArrayList<Dossier>(set);
		}
		return dossiers;
	}

	public List<Aide> getAides() {
		if (aides == null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("debut", periode.getPeriodeDebut());
			params.put("fin", periode.getPeriodeFin());

			Expression expr = Expression.fromString(AIDES_QUERY)
					.expWithParameters(params);

			aides = sql.query(Aide.class, expr, "dossier.dossier",
					"dossier.dossier.chefFamille");

			aides = new BasePageSort<Aide>(aides) //
					.by("dossier.dossier.id") //
					.by("debut") //
					.by("fin") //
					.results();
		}
		return aides;
	}

	public List<Personne> getChefsMenage() {
		if (chefsMenage == null) {
			chefsMenage = new ArrayList<Personne>(getDossiers().size());
			for (Dossier dossier : getDossiers()) {
				chefsMenage.add(dossier.getChefFamille());
			}
		}
		return chefsMenage;
	}

	public List<HandicapPAPH> getPaPh() {
		if (paPh == null) {
			paPh = new LinkedList<HandicapPAPH>();
			for (Dossier dossier : getDossiers()) {
				AspectDossierPAPH aspect = dossier
						.getAspect(AspectDossierPAPH.class);
				if (aspect == null)
					continue;

				for (DossierPAPH paph : aspect.getDossiers()) {
					paPh.addAll(paph.getHandicaps());
				}
			}
		}
		return paPh;
	}

}
