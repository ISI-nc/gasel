package nc.ccas.gasel.pages.budget;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.budget.LigneVirement;

public abstract class Mouvement extends EditPage<LigneVirement> {

	public Mouvement() {
		super(LigneVirement.class);
	}

	public LigneVirement getLigneVirement() {
		return getObject();
	}

	public void setLigneVirement(LigneVirement lv) {
		setObject(lv);
	}

}