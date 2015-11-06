package nc.ccas.gasel.model.habitat;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.habitat.auto._DemandeAffectation;
import nc.ccas.gasel.model.habitat.enums.TypeAffDemandeHabitat;

@Feminin
public class DemandeAffectation extends _DemandeAffectation implements
		ModifListener {

	private static final long serialVersionUID = -4266635753467335427L;

	public AffHabitat getAffectation() {
		if (getTypeDemande() == null) {
			return null;
		}
		switch (getTypeDemande().getId()) {
		case TypeAffDemandeHabitat.ACCESSION:
			return getAffAccession();
		case TypeAffDemandeHabitat.REHABILITATION:
			return getAffRehabilitation();
		case TypeAffDemandeHabitat.LOCATIF:
			return getAffLocatif();
		default:
			throw new IllegalArgumentException("TypeDemande n°"
					+ getTypeDemande().getId());
		}
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
