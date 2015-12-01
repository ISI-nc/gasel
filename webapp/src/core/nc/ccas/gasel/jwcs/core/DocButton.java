package nc.ccas.gasel.jwcs.core;

import static nc.ccas.gasel.modelUtils.CommonQueries.unique;
import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.model.core.docs.ModeleDocument;
import nc.ccas.gasel.model.core.docs.RefModeleDocument;

import org.apache.cayenne.Persistent;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.Any;

public abstract class DocButton extends BaseComponent {
	
	@Parameter(required = true)
	public abstract String getLabel();

	@Parameter(required = true)
	public abstract Persistent getSource();

	@Parameter
	public abstract String getRef();

	@Parameter(name = "modele")
	public abstract ModeleDocument getModeleParameter();

	@Parameter(defaultValue = "literal:false")
	public abstract boolean getDisabled();
	
	@Component(type = "Any", inheritInformalParameters = true)
	public abstract Any getButton();

	public ModeleDocument getModele() {
		ModeleDocument modele = getModeleParameter();

		if (modele == null) {
			if (getRef() == null)
				throw new ApplicationRuntimeException(
						"Paramètres modele et ref nuls");

			RefModeleDocument ref = unique(getObjectContext(),
					RefModeleDocument.class, "key", getRef());

			if (ref == null)
				throw new IllegalArgumentException("Pas de référence "
						+ getRef());

			modele = ref.getModele();
		}

		return modele;
	}

}
