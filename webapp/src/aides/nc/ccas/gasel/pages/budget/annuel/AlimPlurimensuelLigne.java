package nc.ccas.gasel.pages.budget.annuel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.ccas.gasel.model.core.enums.TypePublic;

import org.apache.cayenne.DataRow;

import com.asystan.common.AutoBox;

public class AlimPlurimensuelLigne implements Serializable  {

	private static final long serialVersionUID = -1859211280237810120L;

	private String titre;

	private Map<TypePublic, Integer> quantite = new HashMap<TypePublic, Integer>();

	public AlimPlurimensuelLigne() {
		// skip
	}

	public AlimPlurimensuelLigne(String titre) {
		this.titre = titre;
	}

	public int getTotal() {
		// Ce total sera faux si un dossier a des aides alim. dans
		// plusieurs types de public... mais c'était comme ça.
		int somme = 0;
		for (Integer v : quantite.values()) {
			somme += v;
		}
		return somme;
	}

	public void setQuantite(TypePublic tp, int quantite) {
		this.quantite.put(tp, quantite);
	}

	public void setQuantite(TypePublic tp, DataRow row, String colName) {
		Number value = (Number) row.get(colName);
		setQuantite(tp, value == null ? 0 : value.intValue());
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public int quantite(TypePublic tp) {
		return AutoBox.valueOf(quantite.get(tp));
	}

}
