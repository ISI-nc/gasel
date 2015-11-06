package nc.ccas.gasel.model.core;

import nc.ccas.gasel.model.mairie.Commune;
import nc.ccas.gasel.model.mairie.VilleEtrangere;

import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjEntity;

import dia_modeller.ext.AlterationUtils;
import dia_modeller.ext.Alterator;

public class PersonneAlterator implements Alterator {

	public void alterDomain(DataDomain domain) {
		EntityResolver resolver = domain.getEntityResolver();

		ObjEntity personne = resolver.lookupObjEntity(Personne.class);
		ObjEntity ville = resolver.lookupObjEntity(Commune.class);
		ObjEntity villeEtrangere = resolver
				.lookupObjEntity(VilleEtrangere.class);

		personne.removeAttribute("neCodeVille");
		AlterationUtils.addToOne(personne, ville, "neVille", //
				"ne_code_ville", "codcom");

		personne.removeAttribute("neVilleEtrangereCodpay");
		personne.removeAttribute("neVilleEtrangereScodpa");
		AlterationUtils.addToOne(personne, villeEtrangere, "neVilleEtrangere", //
				"ne_ville_etrangere_codpay", "codpay", //
				"ne_ville_etrangere_scodpa", "scodpa");
	}
}
