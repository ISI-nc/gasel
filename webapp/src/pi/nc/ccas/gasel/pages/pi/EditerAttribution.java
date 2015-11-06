package nc.ccas.gasel.pages.pi;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pi.AttributionJF;
import nc.ccas.gasel.model.pi.Collectivite;
import nc.ccas.gasel.model.pi.DemandeJF;

import org.apache.cayenne.DataObject;

public abstract class EditerAttribution extends EditPage<AttributionJF> {

	public EditerAttribution() {
		super(AttributionJF.class);
	}
	
	@Override
	public String getTitre() {
		return "Attribution";
	}

	public String getTitreLong() {
		if (getParent() == null) {
			return "Attribution";
		}
		DataObject demandeur = ((DemandeJF) getParent()).getDemandeur();
		String prefixe;
		if (demandeur instanceof Collectivite) {
			prefixe = "de la collectivité";
		} else {
			prefixe = "du dossier";
		}
		return "Attribution de la demande " + prefixe + " «" + demandeur + "»";
	}

}
