package nc.ccas.gasel.model.habitat;

import nc.ccas.gasel.model.mairie.Quartier;

import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjEntity;

import dia_modeller.ext.AlterationUtils;
import dia_modeller.ext.Alterator;

public class AspectDossierHabitatAlterator implements Alterator {

	public void alterDomain(DataDomain domain) {
		EntityResolver er = domain.getEntityResolver();

		ObjEntity from = er.lookupObjEntity(AspectDossierHabitat.class);
		ObjEntity to = er.lookupObjEntity(Quartier.class);

		from.removeAttribute("quartierRelogement");
		AlterationUtils.addToOne(from, to, "quartierRelogement", //
				"quartier_relogement", "quarti");
	}

}
