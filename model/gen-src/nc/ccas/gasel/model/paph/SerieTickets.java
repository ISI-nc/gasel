package nc.ccas.gasel.model.paph;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.paph.auto._SerieTickets;

@Feminin
public class SerieTickets extends _SerieTickets implements ModifListener {

	private static final long serialVersionUID = 2640930957917474566L;

	public int getNumFin() {
		return getNumDebut() + getNombreTickets();
	}

	public void setNumFin(int numeroFin) {
		setNombreTickets(numeroFin - getNumDebut());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDemandeTaxi());
	}

}
