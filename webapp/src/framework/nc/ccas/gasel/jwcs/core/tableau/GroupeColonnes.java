package nc.ccas.gasel.jwcs.core.tableau;

import static nc.ccas.gasel.jwcs.core.Tableau.componentsOfClass;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;

public abstract class GroupeColonnes extends AbstractComponent implements
		ModeleColonne {

	private List<Colonne> _colonnes;

	@Parameter(required = true)
	public abstract String getTitre();

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		// skip
	}

	public List<Colonne> getColonnes() {
		if (_colonnes == null) {
			_colonnes = componentsOfClass(Colonne.class, this);
		}
		return _colonnes;
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_colonnes = null;
	}

	public List<String> getTitres() {
		List<String> titres = new ArrayList<String>(getColonnes().size());
		for (Colonne col : getColonnes()) {
			if (!col.getVisible())
				continue;
			titres.add(col.getTitre());
		}
		return titres;
	}

	public List<Cellule> getValeurs() {
		List<Cellule> valeurs = new ArrayList<Cellule>(getColonnes().size());
		for (Colonne col : getColonnes()) {
			if (!col.getVisible())
				continue;
			valeurs.add(col.getValeur());
		}
		return valeurs;
	}

}
