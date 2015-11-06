package nc.ccas.gasel.pages.paph;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.paph.DemandeTaxi;

public abstract class DemanderTaxi extends EditPage<DemandeTaxi> {

	public DemanderTaxi() {
		super(DemandeTaxi.class);
	}

	public abstract DemandeTaxi getDemandetaxi();

	public void setDemandeTaxi(DemandeTaxi dt) {
		setObject(dt);
	}

	@Override
	protected void prepareNewObject(DemandeTaxi dt) {
		if (dt == null) {
			throw new RuntimeException(
					"Impossible de créer une demande de taxi");
		}
		dt.setNecessiteAccompagnateur(false);
		dt.setDerogation(false);

	}

}
