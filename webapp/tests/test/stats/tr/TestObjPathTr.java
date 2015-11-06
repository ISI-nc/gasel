package test.stats.tr;

import junit.framework.TestCase;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.stats.ColumnDefinition;
import nc.ccas.gasel.stats.TableauStat;
import nc.ccas.gasel.stats.TransformationQuery;
import nc.ccas.gasel.stats.tr.ObjPathTr;

import org.apache.cayenne.Persistent;

public class TestObjPathTr extends TestCase {

	public void testAttributePath() throws Exception {
		TransformationQuery path = trPath(Personne.class, "nom");
		assertEquals("$t.nom", path.getColumnDefinition());
	}

	public void testToOnePath() throws Exception {
		TransformationQuery path = trPath(Dossier.class, "chefFamille.nom");
		// TODO checks
		// assertEquals("SELECT DISTINCT t0.nom" //
		// + " FROM $t" //
		// + " JOIN gasel_v2.personne t0 ON ($t.chef_famille_id=t0.id)",
		// path);
	}

	public void testToManyPath() throws Exception {
		TransformationQuery path = trPath(Dossier.class, "personnesACharge.nom");
		// TODO checks
		// String implJoin =
		// "gasel_v2.dossier_personnes_acharge_personne_dossiers_acharge";
		// assertEquals("ARRAY(" //
		// + "SELECT DISTINCT t1.nom" //
		// + " FROM $t" //
		// + " JOIN " + implJoin + " t0 ON ($t.id=t0.dossier_id)" //
		// + " JOIN gasel_v2.personne t1 ON (t0.personne_id=t1.id)" + ")",
		// path);
	}

	public void testCount() throws Exception {
		TransformationQuery path = trPath(Dossier.class, "personnesACharge",
				"COUNT(*)").aggregate();
		// TODO checks
		// String implJoin =
		// "gasel_v2.dossier_personnes_acharge_personne_dossiers_acharge";
		// assertEquals("SELECT DISTINCT COUNT(*)" //
		// + " FROM $t" //
		// + " JOIN " + implJoin + " t0 ON ($t.id=t0.dossier_id)" //
		// + " JOIN gasel_v2.personne t1 ON (t0.personne_id=t1.id)", path);
	}

	private TransformationQuery trPath(Class<? extends Persistent> srcClass,
			String path) throws Exception {
		return trPath(srcClass, path, null);
	}

	private TransformationQuery trPath(Class<? extends Persistent> srcClass,
			String path, String select) throws Exception {
		ObjPathTr tr = new ObjPathTr(path, select);
		TableauStat ts = new TableauStat(srcClass,
				new ColumnDefinition("X", tr));
		return tr.getQuery(ts);
	}

}
