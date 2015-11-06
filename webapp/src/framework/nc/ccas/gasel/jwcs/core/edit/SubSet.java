package nc.ccas.gasel.jwcs.core.edit;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.form.Checkbox;
import org.apache.tapestry.services.DataSqueezer;

public abstract class SubSet<T> extends AbstractSubSet<T> {

	// ------------------------------------------------------------------------

	@Component(type = "Checkbox", bindings = { "value=checked" })
	public abstract Checkbox getCheckbox();

	// ------------------------------------------------------------------------
	// Rendu
	//

	@Override
	protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		// Mode rendu : on stocke les éléments dans le formulaire.
		List<T> values = getElements();
		IForm form = getForm();
		DataSqueezer squeezer = cycle.getInfrastructure().getDataSqueezer();
		for (T v : values) {
			String repr = squeezer.squeeze(v);
			form.addHiddenValue(getClientId(), repr);
		}

		// Rendu
		renderCommon(writer, cycle, values);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		// Mode rewind : on récupère les valeurs depuis le formulaire
		List<T> values = new ArrayList<T>();
		DataSqueezer squeezer = cycle.getInfrastructure().getDataSqueezer();
		String[] savedValues = cycle.getParameters(getClientId());
		if (savedValues != null) {
			for (String repr : savedValues) {
				values.add((T) squeezer.unsqueeze(repr));
			}
		}
		// Rendu
		renderCommon(writer, cycle, values);
	}

	protected void renderCellContents(IMarkupWriter writer, IRequestCycle cycle) {
		getCheckbox().render(writer, cycle);
		writer.print(' ');
		writer.print(String.valueOf(getLabel()));
	}

	// ------------------------------------------------------------------------
	// Gestion des cases à cocher.
	//

	public boolean getChecked() {
		return handler().contains(getValue());
	}

	public void setChecked(boolean checked) {
		if (getChecked() == checked)
			return;
		if (checked) {
			handler().add(getValue());
		} else {
			handler().remove(getValue());
		}
	}

}
