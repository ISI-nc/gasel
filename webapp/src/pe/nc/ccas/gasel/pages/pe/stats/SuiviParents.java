package nc.ccas.gasel.pages.pe.stats;

import static nc.ccas.gasel.modelUtils.SqlUtils.dateToSql;

import java.util.Date;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.Formats;
import nc.ccas.gasel.model.pe.EnfantRAM;
import nc.ccas.gasel.reports.PeriodeProps;
import nc.ccas.gasel.stats.ColumnDefinition;
import nc.ccas.gasel.stats.Representation;
import nc.ccas.gasel.stats.TableauStat;
import nc.ccas.gasel.stats.Transformation;
import nc.ccas.gasel.stats.repr.FormatRepr;
import nc.ccas.gasel.stats.repr.StringRepr;
import nc.ccas.gasel.stats.tr.ObjPathTr;
import nc.ccas.gasel.stats.tr.SqlTr;

public abstract class SuiviParents extends BasePage implements PeriodeProps {

	private ColumnDefinition def(String titre, Transformation tr) {
		return def(titre, tr, StringRepr.INSTANCE);
	}

	private ColumnDefinition def(String titre, Transformation tr,
			Representation<?> repr) {
		return new ColumnDefinition(titre, tr, repr, true, false);
	}

	public TableauStat getTableau() {
		String debut = dateToSql(getPeriodeDebut());
		String fin = dateToSql(getPeriodeFin());
		ObjPathTr assMatTr = new ObjPathTr(
				"gardes.assistanteMaternelle.personne", "prenom||' '||nom");
		assMatTr.addQualifier("date(${gardes}.debut) <= " + fin);
		assMatTr.addQualifier("date(${gardes}.fin) >= " + debut);

		TableauStat ts = new TableauStat(EnfantRAM.class, //
				// new ColumnDefinition("ID", new SqlTr("$t.id")),
				def("Enfant", //
						new ObjPathTr("personne", "prenom||' '||nom")), def(
						"Parent", new ObjPathTr("dossier.dossier.chefFamille",
								"prenom||' '||nom")), //
				def("Dernier contact", new SqlTr("dernier_contact"),
						new FormatRepr(Formats.DATE_FORMAT)), //
				def("Tél. fixe", new ObjPathTr(
						"dossier.dossier.chefFamille.telephoneFixe")), //
				def("Tél. port.", new ObjPathTr(
						"dossier.dossier.chefFamille.telephonePortable")) //
		// def("Ass. mat.", assMatTr) //
		);

		ts.addToSelector("EXISTS (SELECT 1 FROM garde g"
				+ " WHERE g.enfant_id=$t.id AND date(g.debut) <= " + fin
				+ " AND date(g.fin) >= " + debut + ")");

		return ts;
	}

	public Date getDefaultPeriodeDebut() {
		return dates.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return dates.finAnnee();
	}

}
