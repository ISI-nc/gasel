package nc.ccas.gasel.jwcs.habitat;

import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.model.habitat.AideLogement;
import nc.ccas.gasel.model.habitat.DerogationAideLogement;

import org.apache.tapestry.annotations.Parameter;

public abstract class Derogation extends BaseComponent {

	@Parameter(required = true)
	public abstract AideLogement getAide();

	public void creer() {
		DerogationAideLogement derogation = (DerogationAideLogement) getAide()
				.getObjectContext().newObject(DerogationAideLogement.class);
		setDerogation(derogation);
		reloadPage();
	}

	public void supprimer() {
		getDerogation().getObjectContext().deleteObject(getDerogation());
		setDerogation(null);
		reloadPage();
	}

	public DerogationAideLogement getDerogation() {
		return getAide().getDerogation();
	}

	public void setDerogation(DerogationAideLogement derogation) {
		getAide().setDerogation(derogation);
	}

}
