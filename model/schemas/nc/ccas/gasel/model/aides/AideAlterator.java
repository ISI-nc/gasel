package nc.ccas.gasel.model.aides;

import static dia_modeller.ext.AlterationUtils.addReverse;

import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.map.DbRelationship;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;

import dia_modeller.ext.Alterator;

public class AideAlterator implements Alterator {

	public void alterDomain(DataDomain domain) {
		EntityResolver resolver = domain.getEntityResolver();

		ObjEntity aideResumeMontant = resolver
				.lookupObjEntity("AideResumeMontants");

		ObjEntity aide = resolver.lookupObjEntity(Aide.class);

		DbRelationship dbRel = (DbRelationship) aideResumeMontant.getDbEntity()
				.getRelationship("aide");
		dbRel = dbRel.createReverseRelationship();
		dbRel.setName("montants");
		aide.getDbEntity().addRelationship(dbRel);

		ObjRelationship rel = new ObjRelationship("montants");
		rel.setTargetEntity(aideResumeMontant);
		rel.setDbRelationshipPath(dbRel.getName());
		aide.addRelationship(rel);

		// Et les autres, on le fait simplement ici au lieu de créer
		// 36 altérateurs...
		addReverse(aideResumeMontant, "bim", "aideMontants");
		addReverse(aideResumeMontant, "public", "aideMontants");
		addReverse(aideResumeMontant, "secteur", "aideMontants");
	}
}
