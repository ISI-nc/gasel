package nc.ccas.gasel.pages.paph;

import java.util.List;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.paph.AvisCommissionTaxi;
import nc.ccas.gasel.model.paph.DemandeTaxi;

public abstract class EditerAvisCommission extends EditPage<AvisCommissionTaxi> {

	public EditerAvisCommission() {
		super(AvisCommissionTaxi.class);
	}

	@Override
	protected void prepareNewObject(AvisCommissionTaxi taxi) {
		if (taxi == null) {
			throw new RuntimeException("Impossible de créer un avis de commission");
		}
		taxi.setValide(false);
	}
	
	public void ajouterDemande(DemandeTaxi demande) {
		getObject().addToDemandesTaxi(demande);
		redirect();
	}

	public void enleverDemande(DemandeTaxi demande) {
		getObject().removeFromDemandesTaxi(demande);
		redirect();
	}

	public List<DemandeTaxi> getDemandesDispo() {
		List<DemandeTaxi> retval = sql.query(DemandeTaxi.class,
				"commission = NULL");
		retval.removeAll(getObject().getDemandesTaxi());
		return retval;
	}

}
