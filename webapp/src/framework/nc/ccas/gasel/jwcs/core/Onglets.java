package nc.ccas.gasel.jwcs.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.Persist;

import com.asystan.common.beans.BeanUtils;

public abstract class Onglets extends BaseComponent {

	@Persist("workflow")
	public abstract String getOngletActifId();

	public abstract void setOngletActifId(String value);

	@InjectState("workflow")
	public abstract nc.ccas.gasel.workflow.Workflow getWorkflow();

	/**
	 * @return Onglet en cours de rendu dans l'en-tête.
	 */
	public abstract Onglet getOnglet();

	public void activeOnglet(IRequestCycle cycle, String id) {
		setOngletActifId(id);
		getWorkflow().redirect(cycle);
	}

	public Onglet getOngletActif() {
		if (getOnglets().isEmpty()) {
			return null;
		}
		String idActif = getOngletActifId();
		if (idActif == null) {
			return getOnglets().get(0);
		}
		for (Onglet onglet : getOnglets()) {
			if (onglet.getClientId().equals(idActif)) {
				return onglet;
			}
		}
		return getOnglets().get(0);
	}

	public void setOngletActif(Onglet value) {
		setOngletActifId(value.getClientId());
	}

	public boolean isOngletActif(Onglet onglet) {
		return BeanUtils.nullSafeEquals(onglet.getClientId(), getOngletActif()
				.getClientId());
	}

	public String getSuffixeClasse() {
		if (isOngletActif(getOnglet())) {
			return "Actif";
		} else {
			return "Inactif";
		}
	}

	/*
	 * ------------------------------------------------------------------------
	 * Gestion des onglets
	 */

	private List<Onglet> _onglets;

	@Override
	protected void prepareForRender(IRequestCycle cycle) {
		super.prepareForRender(cycle);

	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_onglets = null;
	}

	public List<Onglet> getOnglets() {
		if (_onglets == null) {
			_onglets = new ArrayList<Onglet>();
			for (IRender render : getBody()) {
				if (render instanceof Onglet) {
					_onglets.add((Onglet) render);
				}
			}
		}
		return _onglets;
	}

}
