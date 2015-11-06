package nc.ccas.gasel.pages.parametres.pi;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pi.enums.TypeParcelle;

public abstract class EditerTypeParcelle extends EditPage<TypeParcelle>{

	public EditerTypeParcelle() {
		super(TypeParcelle.class);
	}
	
	@Override
	protected void prepareNewObject(TypeParcelle object) {
		object.setActif(true);
		object.setLocked(false);
	}
}
