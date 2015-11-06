package test.stats;

import junit.framework.TestCase;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.stats.TransformationQuery;

public class TestTransformationQuery extends TestCase {

	public void testBasic() {
		TransformationQuery query = new TransformationQuery() //
				.join(Personne.class, "p", "p.id = $t.chef_famille_id") //
				.definition("p.prenom||' '||p.nom");
		String sql = query.sql(Dossier.class, "t", "$t.id < 300");
		System.out.println(sql);
	}

}
