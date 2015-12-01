package nc.ccas.gasel.pages.pe.stats;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.SqlParams;
import nc.ccas.gasel.model.pe.AspectDossierEnfant;
import nc.ccas.gasel.model.pe.EnfantRAM;
import nc.ccas.gasel.modelUtils.SqlUtils;
import nc.ccas.gasel.reports.PeriodeProps;
import nc.ccas.gasel.stats.ColumnDefinition;
import nc.ccas.gasel.stats.TableauStat;
import nc.ccas.gasel.stats.tr.ObjPathTr;

import org.apache.cayenne.query.SQLTemplate;

public abstract class Frequentation extends BasePage implements PeriodeProps {

	public abstract Object getRow();

	private static final String CRIT_PERIODE = "date(entree) <= $fin AND (date(sortie) IS NULL OR date(sortie) >= $debut)";

	private static final Map<String, SQLTemplate> TMPLS = new HashMap<String, SQLTemplate>();

	static {
		TMPLS.put("inscriptions", new SQLTemplate(AspectDossierEnfant.class,
				"SELECT COUNT(*) AS v FROM enfant_ram e" //
						+ " WHERE " + CRIT_PERIODE));

		TMPLS
				.put(
						"accueil",
						new SQLTemplate(
								AspectDossierEnfant.class,
								"SELECT COUNT(*)AS v FROM enfant_ram e"
										+ " WHERE "
										+ CRIT_PERIODE
										+ " AND EXISTS ("
										+ " SELECT 1 FROM garde g"
										+ ""
										+ " WHERE date(g.debut) <= $fin AND date(g.fin) >= $debut)"));

		TMPLS.put("entrees", new SQLTemplate(AspectDossierEnfant.class,
				"SELECT COUNT(*) AS v FROM enfant_ram e"
						+ " WHERE date(entree) BETWEEN $debut AND $fin"));

		TMPLS.put("sorties", new SQLTemplate(AspectDossierEnfant.class,
				"SELECT COUNT(*) AS v FROM enfant_ram e"
						+ "	WHERE date(sortie) BETWEEN $debut AND $fin"));
	}

	public List<Map<String, Object>> getTableau() {
		SqlParams params = sql.params() //
				.set("debut", getPeriodeDebut()) //
				.set("fin", getPeriodeFin());

		Map<String, Object> values = new HashMap<String, Object>();
		for (Map.Entry<String, SQLTemplate> entry : TMPLS.entrySet()) {
			try {
				values.put(entry.getKey(), sql.query().rows(entry.getValue(),
						params).get(0).get("v"));
			} catch (Exception ex) {
				throw new RuntimeException("On " + entry.getKey() + ": "
						+ entry.getValue().getDefaultTemplate(), ex);
			}
		}
		return Collections.singletonList(values);
	}

	public TableauStat getRepartitionParQuartier() {
		TableauStat ts = new TableauStat(EnfantRAM.class, new ColumnDefinition(
				"Quartier", new ObjPathTr("dossier.dossier.adresseHabitation",
						"adresse_quartier($t.id)")));
		ts.addToSelector(CRIT_PERIODE //
				.replace("$debut", SqlUtils.dateToSql(getPeriodeDebut())) //
				.replace("$fin", SqlUtils.dateToSql(getPeriodeFin())));
		return ts;
	}

	public Date getDefaultPeriodeDebut() {
		return dates.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return dates.finAnnee();
	}

}
