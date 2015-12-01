package nc.ccas.gasel.model.habitat;

import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.model.mairie.Quartier;

import org.apache.cayenne.Persistent;
import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjEntity;

import dia_modeller.ext.AlterationUtils;
import dia_modeller.ext.Alterator;

public class CodeQuartierHabitatAlterator implements Alterator {

	public static List<Class<? extends Persistent>> CLASSES = new LinkedList<Class<? extends Persistent>>();
	static {
		CLASSES.add(DemandeAffectation.class);
		CLASSES.add(AffectationAccession.class);
		CLASSES.add(AffectationLocatif.class);
		CLASSES.add(AffectationRehabilitation.class);
	}

	public void alterDomain(DataDomain domain) {
		EntityResolver er = domain.getEntityResolver();

		ObjEntity to = er.lookupObjEntity(Quartier.class);

		for (Class<? extends Persistent> clazz : CLASSES) {
			ObjEntity from = er.lookupObjEntity(clazz);
			from.removeAttribute("codeQuartier");
			AlterationUtils.addToOne(from, to, "quartier", //
					"code_quartier", "quarti");
		}
	}

}
