package nc.ccas.gasel.model.pi;

import static com.asystan.common.cayenne_new.QueryFactory.createEquals;
import static nc.ccas.gasel.modelUtils.CommonQueries.select;
import static org.apache.cayenne.DataObjectUtils.intPKForObject;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pi.auto._AspectDossierPI;

public class AspectDossierPI extends _AspectDossierPI implements
		ComplexDeletion, ModifListener {

	private static final long serialVersionUID = 3411340924181011844L;

	public List<AttributionJF> getAttributions() {
		if (getObjectId().isTemporary())
			return Collections.emptyList();

		return select(AttributionJF.class, //
				createEquals("demande.dossier", intPKForObject(this)));
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getDemandes());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
