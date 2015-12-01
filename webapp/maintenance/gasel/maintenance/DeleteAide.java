package gasel.maintenance;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.BasePageSqlQuery;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.access.DataContext;
import org.apache.hivemind.util.Defense;


public class DeleteAide {

	public static void main(String[] args) throws Exception {
		// ------------------------------------------------------------
		DataContext.bindThreadDataContext(CayenneUtils.createDataContext());
		// ------------------------------------------------------------

		if (true) {
			return;
		}

		DeleteAide deleteAide = new DeleteAide();

		deleteAide.setNumeroDossier(20100245);
		deleteAide.setType("Alimentation", "Alimentation");
		deleteAide.setStatut(StatutAide.IMMEDIATE);
		deleteAide.setDateDebut("2013-06-17");
		deleteAide.setDateFin("2013-06-30");

		deleteAide.delete();
	}

	private Dossier dossier;

	private NatureAide type;

	private StatutAide statut;

	private String dateDebut;

	private String dateFin;

	public void delete() {
		Defense.notNull(dossier, "dossier");
		Defense.notNull(type, "type");
		Defense.notNull(statut, "statut");
		Defense.notNull(dateDebut, "dateDebut");
		Defense.notNull(dateFin, "dateFin");

		Aide aide = require(query().unique(Aide.class,
				"statut=$statut and " + //
						"nature=$nature and " + //
						"dossier.dossier=$dossier and " + //
						"debut=$debut and " + //
						"fin=$fin", //
				sql().params() //
						.set("dossier", dossier) //
						.set("nature", type) //
						.set("statut", statut) //
						.set("debut", dateDebut) //
						.set("fin", dateFin)));

		System.out.println(aide);

		aide.getObjectContext().deleteObject(aide);
		aide.getObjectContext().commitChanges();
	}

	public void setDateDebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}

	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}

	public void setStatut(int statutId) {
		this.statut = require(query().byId(StatutAide.class, statutId));
	}

	public void setNumeroDossier(int numeroDossier) {
		this.dossier = require(query().byId(Dossier.class, numeroDossier));
	}

	public void setType(String secteur, String type) {
		this.type = require(query().unique(NatureAide.class,
				"libelle=$type and parent.libelle=$secteur",
				sql().params().set("type", type).set("secteur", secteur)));
	}

	private BasePageSql sql;

	private BasePageSql sql() {
		if (sql == null) {
			sql = new BasePageSql(DataContext.getThreadDataContext());
		}
		return sql;
	}

	private BasePageSqlQuery query() {
		return sql().query();
	}

	private <T> T require(T o) {
		if (o == null) {
			throw new NullPointerException("required but not found");
		}
		return o;
	}

}
