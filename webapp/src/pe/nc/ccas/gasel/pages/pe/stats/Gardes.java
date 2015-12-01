package nc.ccas.gasel.pages.pe.stats;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.SqlParams;
import nc.ccas.gasel.model.pe.AssistanteMaternelle;
import nc.ccas.gasel.model.pe.EnfantRAM;
import nc.ccas.gasel.model.pe.enums.ModeGarde;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.cayenne.query.SQLTemplate;

public abstract class Gardes extends BasePage implements PeriodeProps {

	private static final SQLTemplate TMPL_ASS_MAT = new SQLTemplate(
			AssistanteMaternelle.class,
			"SELECT am.* FROM assistante_maternelle am"
					+ " WHERE EXISTS (SELECT 1 FROM garde g"
					+ " WHERE g.assistante_maternelle_id = am.id"
					+ " AND date(g.debut)<=$fin AND date(g.fin)>=$debut)");

	private static final SQLTemplate TMPL_ENFANT = new SQLTemplate(
			EnfantRAM.class,
			"SELECT e.* FROM enfant_ram e"
					+ " WHERE EXISTS (SELECT 1 FROM garde g"
					+ " WHERE g.enfant_id = e.id AND g.assistante_maternelle_id = $amId"
					+ " AND date(g.debut)<=$fin AND date(g.fin)>=$debut)");

	public List<LigneGarde> getTableau() {
		List<LigneGarde> retval = new LinkedList<LigneGarde>();

		SqlParams params = sql.params().setPeriode(this);
		List<AssistanteMaternelle> assMats = sql.query().objects(TMPL_ASS_MAT,
				params);
		for (AssistanteMaternelle assMat : assMats) {
			params.set("amId", assMat);
			List<EnfantRAM> enfantsGardes = sql.query().objects(TMPL_ENFANT,
					params);
			for (EnfantRAM enfant : enfantsGardes) {
				retval.add(new LigneGarde(assMat, enfant));
			}
		}
		Collections.sort(retval);
		return retval;
	}

	public Date getDefaultPeriodeDebut() {
		return dates.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return dates.finAnnee();
	}

	public abstract LigneGarde getRow();

	public AssistanteMaternelle getAssMat() {
		return getRow().getAssistante();
	}

	public EnfantRAM getEnfant() {
		return getRow().getEnfant();
	}

	private static final SQLTemplate TMPL_MODES_GARDE = new SQLTemplate(
			ModeGarde.class, "SELECT mg.* FROM mode_garde mg"
					+ " WHERE EXISTS (SELECT 1 FROM garde g"
					+ " WHERE g.mode_id=mg.id AND g.enfant_id=$enfId"
					+ " AND g.assistante_maternelle_id=$assMatId"
					+ " AND date(g.debut)<=$fin AND date(g.fin)>=$debut)");

	public List<ModeGarde> getModesGarde() {
		return sql.query().objects(TMPL_MODES_GARDE,
				sql.params().set("enfId", getEnfant()) //
						.set("assMatId", getAssMat()) //
						.setPeriode(this));
	}

}
