package nc.ccas.gasel.pages.paph;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.core.enums.ReponseProblematique;
import nc.ccas.gasel.model.paph.AccompagnementPAPH;

public abstract class EditerAccompagnement extends EditPage<AccompagnementPAPH> {

	public EditerAccompagnement() {
		super(AccompagnementPAPH.class);
	}

	public abstract AccompagnementPAPH getAccompagnement();

	public void setAccompagnement(AccompagnementPAPH acc) {
		setObject(acc);
	}

	public abstract ReponseProblematique getReponse();

	public void ajouterObjectif() {
		((EditerObjectif) getRequestCycle().getPage("paph/EditerObjectif"))
				.activateForParent(getObject(), "accompagnement");
	}
}
