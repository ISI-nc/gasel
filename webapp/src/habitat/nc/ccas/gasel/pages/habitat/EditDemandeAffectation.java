package nc.ccas.gasel.pages.habitat;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.habitat.AffectationAccession;
import nc.ccas.gasel.model.habitat.AffectationLocatif;
import nc.ccas.gasel.model.habitat.AffectationRehabilitation;
import nc.ccas.gasel.model.habitat.DemandeAffectation;

import org.apache.cayenne.access.DataContext;

public abstract class EditDemandeAffectation extends
		EditPage<DemandeAffectation> {

	public EditDemandeAffectation() {
		super(DemandeAffectation.class);
	}

	@Override
	protected void prepareCommit() {
		DataContext dc = getObjectContext();
		DemandeAffectation d = getObject();

		if ((d.getTypeDemande() == null || !d.getTypeDemande().isLocatif())
				&& d.getAffLocatif() != null) {
			dc.deleteObject(d.getAffLocatif());
			d.setAffLocatif(null);
		}
		if ((d.getTypeDemande() == null || !d.getTypeDemande().isRehabilitation())
				&& d.getAffRehabilitation() != null) {
			dc.deleteObject(d.getAffRehabilitation());
			d.setAffRehabilitation(null);
		}
		if ((d.getTypeDemande() == null || !d.getTypeDemande().isAccession())
				&& d.getAffAccession() != null) {
			dc.deleteObject(d.getAffAccession());
			d.setAffAccession(null);
		}
	}

	public AffectationLocatif getAffLocatif() {
		if (getObject().getAffLocatif() == null) {
			getObject().setAffLocatif(createDataObject(AffectationLocatif.class));
		}
		return getObject().getAffLocatif();
	}

	public AffectationRehabilitation getAffRehabilitation() {
		if (getObject().getAffRehabilitation() == null) {
			getObject().setAffRehabilitation(
					createDataObject(AffectationRehabilitation.class));
		}
		return getObject().getAffRehabilitation();
	}

	public AffectationAccession getAffAccession() {
		if (getObject().getAffAccession() == null) {
			getObject().setAffAccession(
					createDataObject(AffectationAccession.class));
		}
		return getObject().getAffAccession();
	}

}
