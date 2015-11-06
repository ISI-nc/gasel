package nc.ccas.gasel.pages.habitat;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.core.enums.TypeProblematique;
import nc.ccas.gasel.model.habitat.ObjectifProbHabitat;

import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageEvent;

public abstract class EditerProblematique extends EditPage<ObjectifProbHabitat> {

	public EditerProblematique() {
		super(ObjectifProbHabitat.class);
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		if (getObject().getProblematique() != null) {
			setType(getObject().getProblematique().getParent());
		}
	}

	@Persist("workflow")
	public abstract TypeProblematique getType();

	public abstract void setType(TypeProblematique type);

}
