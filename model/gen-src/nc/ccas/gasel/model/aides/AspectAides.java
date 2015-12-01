package nc.ccas.gasel.model.aides;

import java.util.Date;
import java.util.LinkedList;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.aides.auto._AspectAides;
import nc.ccas.gasel.model.core.Utilisateur;

public class AspectAides extends _AspectAides implements ComplexDeletion,
		ModifListener {
	private static final long serialVersionUID = -3086631529556184576L;

	public void prepareForDeletion() {
		DeletionUtils.delete(getAides());
		DeletionUtils.delete(getAidesRefusees());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

	/**
	 * Bascule (transfère) les aides d'un aspect aides vers cet aspect.
	 * 
	 * @param source
	 *            L'aspect aides du dossier source duquel transférer les aides.
	 */
	public void basculerAidesDepuis(AspectAides source) {
		if (source.equals(this)) {
			throw new RuntimeException("Tentative de fusion avec moi-même!");
		}
		for (Aide aide : new LinkedList<Aide>(source.getAides())) {
			aide.setDossier(this);
		}
		for (AideRefusee aideRefusee : new LinkedList<AideRefusee>(source.getAidesRefusees())) {
			aideRefusee.setDossier(this);
		}
	}

}
