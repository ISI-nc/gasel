package nc.ccas.gasel.pages.pi;

import static nc.ccas.gasel.modelUtils.DateUtils.addMonths;
import static nc.ccas.gasel.modelUtils.DateUtils.today;

import java.util.Date;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pi.Collectivite;
import nc.ccas.gasel.model.pi.DemandeJF;

public abstract class EditerCollectivite extends EditPage<Collectivite> {

	public EditerCollectivite() {
		super(Collectivite.class);
	}

	@Override
	protected void prepareNewObject(Collectivite col) {
		if (col == null) {
			throw new RuntimeException(
					"Impossible de créer une demande de jardin");
		}
	}

	public void ajouterAttribution(DemandeJF demande) {
		((EditerAttribution) getRequestCycle().getPage("pi/EditerAttribution"))
				.activateForParent(demande, "demande");
	}

	public Boolean estActif(Date dateDemande) {
		if (dateDemande == null)
			return false;

		Date dateLimite = addMonths(today(), -6);
		return dateDemande.after(dateLimite);
	}

}
