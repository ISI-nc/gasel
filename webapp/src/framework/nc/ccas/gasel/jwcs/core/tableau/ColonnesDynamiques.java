package nc.ccas.gasel.jwcs.core.tableau;

import java.util.ArrayList;
import java.util.List;

import nc.ccas.gasel.jwcs.core.Tableau;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;

/**
 * Support des colonnes dynamiques.
 * 
 * @author Mikaël Cluseau - ISI.NC
 * 
 */
public abstract class ColonnesDynamiques extends AbstractComponent implements
		ModeleColonne {

	@Parameter(required = true)
	public abstract Iterable<?> getSource();

	@Parameter
	public abstract Object getValue();

	public abstract void setValue(Object value);

	@Parameter
	public abstract Number getIndex();

	public abstract void setIndex(Number value);

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		// skip
	}

	public List<String> getTitres() {
		List<String> titres = new ArrayList<String>();
		int idx = 0;
		for (Object value : getSource()) {
			updateOutputParameters(idx++, value);
			for (ModeleColonne col : getColonnes()) {
				if (!col.getVisible())
					continue;
				titres.addAll(col.getTitres());
			}
		}
		return titres;
	}

	public List<Cellule> getValeurs() {
		List<Cellule> valeurs = new ArrayList<Cellule>();
		int idx = 0;
		for (Object value : getSource()) {
			updateOutputParameters(idx++, value);
			for (ModeleColonne col : getColonnes()) {
				if (!col.getVisible())
					continue;
				valeurs.addAll(col.getValeurs());
			}
		}
		return valeurs;
	}

	private void updateOutputParameters(int idx, Object value) {
		IBinding binding = getBinding("index");
		if (binding != null)
			binding.setObject(idx);
		binding = getBinding("value");
		if (binding != null)
			binding.setObject(value);
	}

	public List<ModeleColonne> getColonnes() {
		return Tableau.componentsOfClass(ModeleColonne.class, this);
	}

	public List<GroupeDesc> getGroupes() {
		List<GroupeDesc> groupes = new ArrayList<GroupeDesc>();
		int idx = 0;
		for (Object value : getSource()) {
			updateOutputParameters(idx++, value);
			Tableau.groupesAdd(groupes, getColonnes());
		}
		return groupes;
	}

}
