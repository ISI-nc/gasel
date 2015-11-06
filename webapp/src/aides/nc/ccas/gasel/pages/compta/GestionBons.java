package nc.ccas.gasel.pages.compta;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;
import nc.ccas.gasel.model.aides.UsageBon;

import org.apache.tapestry.event.PageEvent;

public abstract class GestionBons extends EditPage<Bon> {

	public GestionBons() {
		super(Bon.class);
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		Object o = getObject(); // l'hallu genre java qui bug !!!
		if (o instanceof UsageBon) {
			setObject(((UsageBon) o).getBon());
		}
	}

	@Override
	protected void prepareEnregistrer() {
		Bon bon = getBon();

		if (bon.getUsage() != null) {
			int etatBon;

			if (bon.getMontant() > bon.getUsage().getMontantUtilise()) {
				etatBon = EtatBon.PARTIELLEMENT_UTILISE;
			} else {
				etatBon = EtatBon.UTILISE;
			}

			bon.setEtat(objectForPk(EtatBon.class, etatBon));
		}
	}

	public Bon getBon() {
		return getObject();
	}

	public void setBon(Bon bon) {
		setObject(bon);
	}

}
