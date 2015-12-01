package nc.ccas.gasel.pages.pi;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pi.ArreteJF;
import nc.ccas.gasel.model.pi.JardinFamilial;

public abstract class EditerArreteJF extends EditPage<ArreteJF> {

	public EditerArreteJF() {
		super(ArreteJF.class);
	}

	public JardinFamilial getJardin() {
		return getObject().getAttribution().getDemande().getJardin();
	}

}
