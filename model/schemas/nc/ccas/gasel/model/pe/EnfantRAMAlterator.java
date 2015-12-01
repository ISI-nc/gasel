package nc.ccas.gasel.model.pe;

import nc.ccas.gasel.model.mairie.Quartier;

import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjEntity;

import dia_modeller.ext.AlterationUtils;
import dia_modeller.ext.Alterator;

public class EnfantRAMAlterator implements Alterator {

	public void alterDomain(DataDomain domain) {
		EntityResolver er = domain.getEntityResolver();

		ObjEntity from = er.lookupObjEntity(EnfantRAM.class);
		ObjEntity to = er.lookupObjEntity(Quartier.class);

		from.removeAttribute("codeQuartierTravailParent");
		AlterationUtils.addToOne(from, to, "quartierTravailParent", //
				"code_quartier_travail_parent", "quarti");
	}

}
