package nc.ccas.gasel.model.budget;

import nc.ccas.gasel.model.aides.Facture;
import nc.ccas.gasel.model.mairie.FournisseurMairie;

import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;

import dia_modeller.ext.AlterationUtils;
import dia_modeller.ext.Alterator;

public class LiensFournisseursAlterator implements Alterator {

	public void alterDomain(DataDomain domain) {
		ObjEntity fournisseur = domain.getEntityResolver().lookupObjEntity(
				FournisseurMairie.class);

		for (Class<?> clazz : new Class<?>[] { //
		NatureAideFournisseur.class, Facture.class }) {
			ObjEntity objEntity = domain.getEntityResolver().lookupObjEntity(
					clazz);
			objEntity.removeAttribute("codeFournisseur");

			AlterationUtils.addToOne(objEntity, fournisseur, "fournisseur",
					"code_fournisseur", "idetbs");
		}

		ObjEntity nature = domain.getEntityResolver().lookupObjEntity(
				NatureAide.class);
		ObjRelationship relLiens = (ObjRelationship) nature
				.getRelationship("liensFournisseurs");
		ObjRelationship relFournisseurs = new ObjRelationship("fournisseurs");
		relFournisseurs.setSourceEntity(nature);
		relFournisseurs.setTargetEntity(fournisseur);
		relFournisseurs.setDbRelationshipPath(relLiens.getDbRelationshipPath()
				+ ".fournisseur");
		nature.addRelationship(relFournisseurs);
		nature.removeRelationship("liensFournisseurs");
	}

}
