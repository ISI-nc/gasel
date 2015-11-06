package nc.ccas.gasel.model.v1;

import java.util.ArrayList;
import java.util.List;

import nc.ccas.gasel.model.v1.auto._DossierV1;

import com.asystan.common.StringUtils;

public class DossierV1 extends _DossierV1 {

	private static final long serialVersionUID = -6500052911811706263L;

	@Override
	public String toString() {
		return "Dossier n°" + getNumero() + " : " + getPersonnesDesc();
	}

	private String getPersonnesDesc() {
		List<PersonneV1> personnes = getPersonnes();
		List<String> descs = new ArrayList<String>(personnes.size());
		for (PersonneV1 p : personnes) {
			descs.add(p.getNom() + ", " + p.getPrenom() + " ("
					+ p.getAdministre().getEstAuChefDeFamille() + ")");
		}
		return StringUtils.join("; ", descs);
	}

}
