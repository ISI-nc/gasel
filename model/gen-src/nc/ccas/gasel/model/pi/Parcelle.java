package nc.ccas.gasel.model.pi;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pi.auto._Parcelle;

@Feminin
public class Parcelle extends _Parcelle implements ComplexDeletion,
		ModifListener {

	private static final long serialVersionUID = 5419531212390195775L;

	@Override
	public String toString() {
		return String.valueOf(getNumero());
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getAttributions());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getJardin());
	}

}
