package nc.ccas.gasel.pages.parametres.pi;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pi.AvisCommissionJF;

public abstract class EditerAvisCommissionJF extends EditPage<AvisCommissionJF> {

	public EditerAvisCommissionJF() {
		super(AvisCommissionJF.class);
	}

	@Override
	protected void prepareNewObject(AvisCommissionJF object) {
		object.setActif(true);
		object.setLocked(false);
		object.setValidant(false);
	}
	
}
