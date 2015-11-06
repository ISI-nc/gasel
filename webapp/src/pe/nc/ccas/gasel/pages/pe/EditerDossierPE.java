package nc.ccas.gasel.pages.pe;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pe.EnfantRAM;
import nc.ccas.gasel.model.pe.Garde;
import nc.ccas.gasel.model.pe.HandicapPE;

import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

public abstract class EditerDossierPE extends EditPage<EnfantRAM> implements PageBeginRenderListener{

	public EditerDossierPE() {
		super(EnfantRAM.class);
	}

	public EnfantRAM getEnfant() {
		return getObject();
	}

	public void setEnfant(EnfantRAM enfant) {
		setObject(enfant);
	}

	public abstract Garde getGarde();

	public void prepareGarde() {
		int ordreMax = -1;
		for (Garde gardeExistante : getEnfant().getGardes()) {
			if (gardeExistante.getOrdre() > ordreMax)
				ordreMax = gardeExistante.getOrdre();
		}
		getGarde().setOrdre(ordreMax + 1);
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		
		EnfantRAM enfant = getObject();
		if (enfant.getHandicap() == null) {
			enfant.setHandicap(createDataObject(HandicapPE.class));
		}
	}

	@Override
	protected void prepareEnregistrer() {
		EnfantRAM enfant = getObject();
		HandicapPE handicap = enfant.getHandicap();
		handicap.setEnfant(enfant);
		if (handicap.getLibelle() == null && handicap.getPonderation() == null) {
			// Pas de handicap
			enfant.setHandicap(null);
			getObjectContext().deleteObject(handicap);
		}
	}

}
