package nc.ccas.gasel.model.core;

import nc.ccas.gasel.model.mairie.Commune;
import nc.ccas.gasel.model.mairie.Voie;

import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjEntity;

import dia_modeller.ext.AlterationUtils;
import dia_modeller.ext.Alterator;

public class AdresseAlterator implements Alterator {

	public void alterDomain(DataDomain domain) {
		EntityResolver resolver = domain.getEntityResolver();

		ObjEntity adresse = resolver.lookupObjEntity(Adresse.class);
		ObjEntity voie = resolver.lookupObjEntity(Voie.class);
		ObjEntity ville = resolver.lookupObjEntity(Commune.class);

		adresse.removeAttribute("codeVoie");
		AlterationUtils.addToOne(adresse, voie, "rue", //
				"code_voie", "cdvoie");

		adresse.removeAttribute("codeVille");
		AlterationUtils.addToOne(adresse, ville, "ville", //
				"code_ville", "codcom");
	}

}
