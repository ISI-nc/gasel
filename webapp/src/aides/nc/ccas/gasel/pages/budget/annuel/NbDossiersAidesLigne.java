package nc.ccas.gasel.pages.budget.annuel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.reports.Cumulable;

public class NbDossiersAidesLigne implements Cumulable<NbDossiersAidesLigne>,
		Serializable {
	private static final long serialVersionUID = -3048368975599491325L;

	private String titre;

	private final Map<TypePublic, Integer> values = new HashMap<TypePublic, Integer>();

	public NbDossiersAidesLigne() {
		// skip
	}

	public NbDossiersAidesLigne(String titre) {
		this.titre = titre;
	}

	public void cumule(NbDossiersAidesLigne other) {
		for (TypePublic tp : other.values.keySet()) {
			setValue(tp, value(tp) + other.value(tp));
		}
	}

	public void setValue(TypePublic tp, int value) {
		values.put(tp, value);
	}

	public int value(TypePublic tp) {
		Integer x = values.get(tp);
		return x == null ? 0 : x;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

}
