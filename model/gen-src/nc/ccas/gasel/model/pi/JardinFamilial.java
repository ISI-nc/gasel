package nc.ccas.gasel.model.pi;

import java.util.ArrayList;
import java.util.List;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.core.docs.ModeleDocument;
import nc.ccas.gasel.model.core.docs.RefModeleDocument;
import nc.ccas.gasel.model.pi.auto._JardinFamilial;
import nc.ccas.gasel.modelUtils.CommonQueries;

public class JardinFamilial extends _JardinFamilial implements ComplexDeletion {

	private static final long serialVersionUID = 4158764572870697587L;

	public List<DemandeJF> getDemandesEnAttente() {
		List<DemandeJF> retval = new ArrayList<DemandeJF>(getDemandes().size());
		List<AttributionJF> attributions = getAttributions();
		for (DemandeJF d : getDemandes()) {
			if (hasAttribution(d, attributions)) {
				continue;
			}
			retval.add(d);
		}
		return retval;
	}

	private boolean hasAttribution(DemandeJF d, List<AttributionJF> attributions) {
		for (AttributionJF a : attributions) {
			if (a.getDemande() == d) {
				return true;
			}
		}
		return false;
	}

	public List<AttributionJF> getAttributions() {
		List<AttributionJF> retval = new ArrayList<AttributionJF>();
		for (Parcelle p : getParcelles()) {
			retval.addAll(p.getAttributions());
		}
		return retval;
	}

	@Override
	public String toString() {
		return getNom();
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getDemandes());
		DeletionUtils.delete(getParcelles());

		DeletionUtils.delete(getArreteAttribution());
		DeletionUtils.delete(getArreteRenouvellement());
		DeletionUtils.delete(getCourierPaiement());
		DeletionUtils.delete(getCourierEntretien());
		DeletionUtils.delete(getCourierPaiementEntretien());
	}

	public ModeleDocument getArreteAttributionToUse() {
		return getDoc(getArreteAttribution(), "pi.arrete_attribution");
	}

	public ModeleDocument getArreteRenouvellementToUse() {
		return getDoc(getArreteRenouvellement(), "pi.arrete_renouvellement");
	}

	public ModeleDocument getCourierPaiementToUse() {
		return getDoc(getCourierPaiement(), "pi.retard_paiement");
	}

	public ModeleDocument getCourierEntretienToUse() {
		return getDoc(getCourierEntretien(), "pi.entretien");
	}

	public ModeleDocument getCourierPaiementEntretienToUse() {
		return getDoc(getCourierPaiementEntretien(), "pi.entretien_paiement");
	}

	private ModeleDocument getDoc(ModeleDocument doc, String key) {
		if (doc == null) {
			RefModeleDocument ref = CommonQueries.unique(getObjectContext(),
					RefModeleDocument.class, RefModeleDocument.KEY_PROPERTY,
					key);
			if (ref == null)
				return null;
			return ref.getModele();
		}
		return doc;
	}

}
