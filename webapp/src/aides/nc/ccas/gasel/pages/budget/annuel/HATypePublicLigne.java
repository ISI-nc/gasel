package nc.ccas.gasel.pages.budget.annuel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.reports.Cumulable;
import nc.ccas.gasel.reports.NombreValeur;

public class HATypePublicLigne implements Serializable,
		Cumulable<HATypePublicLigne> {

	private static final long serialVersionUID = -1933556534547468358L;

	private final String titre;

	private final boolean highlight;

	private final Map<TypePublic, NombreValeur> values = new HashMap<TypePublic, NombreValeur>();

	private HATypePublicLigne reference;

	public HATypePublicLigne(String titre, boolean highlight) {
		this.titre = titre;
		this.highlight = highlight;
	}

	public int nombre(TypePublic tp) {
		NombreValeur nv = values.get(tp);
		if (nv == null) {
			return 0;
		}
		return nv.getNombre();
	}

	public Integer propNombre(TypePublic tp) {
		if (getReference() == null) {
			return null;
		}
		int ref = getReference().nombre(tp);
		if (ref == 0) {
			return null;
		}
		return nombre(tp) * 100 / ref;
	}

	public int valeur(TypePublic tp) {
		NombreValeur nv = values.get(tp);
		if (nv == null) {
			return 0;
		}
		return nv.getValeur();
	}

	public Integer propValeur(TypePublic tp) {
		if (getReference() == null) {
			return null;
		}
		int ref = getReference().valeur(tp);
		if (ref == 0) {
			return null;
		}
		return valeur(tp) * 100 / ref;
	}

	/*
	 * Totaux
	 */

	public int totalNombre() {
		int sum = 0;
		for (TypePublic tp : values.keySet()) {
			sum += nombre(tp);
		}
		return sum;
	}

	public int totalValeur() {
		int sum = 0;
		for (TypePublic tp : values.keySet()) {
			sum += valeur(tp);
		}
		return sum;
	}

	public Integer propTotalNombre() {
		if (getReference() == null) {
			return null;
		}
		int ref = getReference().totalNombre();
		if (ref == 0) {
			return null;
		}
		return totalNombre() * 100 / ref;
	}

	public Integer propTotalValeur() {
		if (getReference() == null) {
			return null;
		}
		int ref = getReference().totalValeur();
		if (ref == 0) {
			return null;
		}
		return totalValeur() * 100 / ref;
	}

	// ----

	public void setValue(TypePublic tp, int nombre, int valeur) {
		NombreValeur nv = new NombreValeur();
		nv.setNombre(nombre);
		nv.setValeur(valeur);
		values.put(tp, nv);
	}

	public String getTitre() {
		return titre;
	}

	public void cumule(HATypePublicLigne other) {
		for (TypePublic tp : other.values.keySet()) {
			merge(tp, other.nombre(tp), other.valeur(tp));
		}
	}

	public void merge(TypePublic tp, int nombre, int valeur) {
		setValue(tp, nombre(tp) + nombre, valeur(tp) + valeur);
	}

	public boolean isHighlight() {
		return highlight;
	}

	public HATypePublicLigne getReference() {
		return reference;
	}

	public void setReference(HATypePublicLigne reference) {
		this.reference = reference;
	}

}
