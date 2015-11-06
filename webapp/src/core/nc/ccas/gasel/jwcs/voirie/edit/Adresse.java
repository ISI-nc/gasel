package nc.ccas.gasel.jwcs.voirie.edit;

import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.model.mairie.Commune;
import nc.ccas.gasel.model.mairie.Voie;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;

public abstract class Adresse extends BaseComponent {

	@Parameter(name = "value")
	public abstract nc.ccas.gasel.model.core.Adresse getAdresse();

	public abstract void setAdresse(nc.ccas.gasel.model.core.Adresse value);

	@Parameter(defaultValue = "true")
	public abstract boolean isRequired();
	
	@Parameter(defaultValue = "false")
	public abstract boolean isAvecBoitePostale();

	public void supprimer(IRequestCycle cycle) {
		nc.ccas.gasel.model.core.Adresse adresse = getAdresse();
		if (adresse != null) {
			getObjectContext().deleteObject(adresse);
			setAdresse(null);
		}
		reloadPage(cycle);
	}

	public void creer(IRequestCycle cycle) {
		nc.ccas.gasel.model.core.Adresse adresse = new nc.ccas.gasel.model.core.Adresse();
		getObjectContext().registerNewObject(adresse);
		setAdresse(adresse);
		reloadPage(cycle);
	}

	// -- listeners de ToOne --

	public void rueChanged() {
		nc.ccas.gasel.model.core.Adresse adr = getAdresse();
		if (adr == null) {
			return;
		}
		Voie voie = adr.getRue();
		Commune commune = voie.getCommune();
		adr.setCodePostal(commune.getCodePostal().getCode());
		adr.setVille(commune);
	}

}
