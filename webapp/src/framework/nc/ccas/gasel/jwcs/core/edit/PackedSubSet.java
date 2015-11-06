package nc.ccas.gasel.jwcs.core.edit;

import java.util.ArrayList;
import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.BasePagePsm;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.PropertySelection;
import org.apache.tapestry.form.Submit;

public abstract class PackedSubSet<T> extends AbstractSubSet<T> {

	@Parameter
	public abstract IActionListener getOnAdd();

	// ------------------------------------------------------------------------

	public BasePagePsm psm = new BasePagePsm(this);

	// ------------------------------------------------------------------------
	// Rendu
	//

	@Override
	protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		_render(writer, cycle);
	}

	@Override
	protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		_render(writer, cycle);
	}

	@Override
	protected void renderCellContents(IMarkupWriter writer, IRequestCycle cycle) {
		// writer.print(String.valueOf(getLabel()));
		getRemoveSubmit().render(writer, cycle);
	}

	private void _render(IMarkupWriter writer, IRequestCycle cycle) {
		renderCommon(writer, cycle, getSelectedValues());

		if (!getAvailableValues().isEmpty()) {
			writer.begin("div");
			getSelect().render(writer, cycle);
			writer.printRaw("&nbsp;");
			getAddSubmit().render(writer, cycle);
			writer.printRaw("&nbsp;");
			writer.end();
		}
	}

	// ------------------------------------------------------------------------
	// Gestion des valeurs sélectionnées/sélectionnables
	//

	public List<T> getAvailableValues() {
		return filterSelected(false);
	}

	public List<T> getSelectedValues() {
		return filterSelected(true);
	}

	private List<T> filterSelected(boolean selected) {
		List<T> allValues = getElements();
		List<T> values = new ArrayList<T>(allValues.size());
		for (T value : allValues) {
			if (handler().contains(value) != selected)
				continue;
			values.add(value);
		}
		return values;
	}

	// ------------------------------------------------------------------------
	// Gestion de l'ajout de valeur
	//

	public abstract T getValueToAdd();

	@Component(type = "PropertySelection", bindings = { "value=valueToAdd",
			"model=psm.dataObject(availableValues)" })
	public abstract PropertySelection getSelect();

	@Component(type = "Submit", bindings = { "action=listener:addValue",
			"value=literal:Ajouter", "class=literal:button" })
	public abstract Submit getAddSubmit();

	public void addValue(IRequestCycle cycle) {
		T valueToAdd = getValueToAdd();

		if (valueToAdd == null)
			return;

		if (!handler().contains(valueToAdd))
			handler().add(valueToAdd);

		if (getOnAdd() != null) {
			cycle.setListenerParameters(new Object[] { valueToAdd });
			getOnAdd().actionTriggered(this, cycle);
		}

		refreshPage();
	}

	private void refreshPage() {
		((BasePage) getPage()).redirect();
	}

	// ------------------------------------------------------------------------
	// Gestion de la suppression de valeur
	//

	@Component(type = "Submit", bindings = { "action=listener:removeValue",
			"parameters=value", "class=literal:invis-button", "value=label",
			"title=literal:Enlever" })
	public abstract Submit getRemoveSubmit();

	public void removeValue(T value) {
		handler().remove(value);
		refreshPage();
	}

}
