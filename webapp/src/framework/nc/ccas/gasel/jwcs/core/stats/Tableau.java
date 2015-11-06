package nc.ccas.gasel.jwcs.core.stats;

import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.stats.TableauStat;

public abstract class Tableau extends BaseComponent {

	public abstract TableauStat getTableau();

	public TableauStat getTableauRempli() {
		getTableau().fill();
		return getTableau();
	}

}
