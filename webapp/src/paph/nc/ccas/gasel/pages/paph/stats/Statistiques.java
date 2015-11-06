package nc.ccas.gasel.pages.paph.stats;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.stats.ColumnDefinition;
import nc.ccas.gasel.stats.TableauStat;
import nc.ccas.gasel.stats.repr.RangeRepr;
import nc.ccas.gasel.stats.tr.ColumnTr;
import nc.ccas.gasel.stats.tr.ObjPathTr;

public abstract class Statistiques extends BasePage {

	public TableauStat getTableau() {
		return new TableauStat("DossierPAPH", //
				new ColumnDefinition("Quartier", //
						new ObjPathTr("dossier.dossier.adresseHabitation",
								"adresse_quartier($t.id)")), //
				new ColumnDefinition("Situation sociale", new ObjPathTr(
						"situationSociale.libelle")), //
				new ColumnDefinition("Nombre d'enfants", //
						new ColumnTr("nombre_enfants"), //
						new RangeRepr(0, 2, 4, 5)), //
//				new ColumnDefinition("Problématique", new ObjPathTr(
//						"dossier.dossier.problematiques.libelle"),
//						StringRepr.INSTANCE, true), //

				new ColumnDefinition("Revenus", //
						new ColumnTr("revenu_net_mensuel"), //
						new RangeRepr(0, 50000, 100000, 150000)) //
		);
	}

}
