package nc.ccas.gasel.model.habitat;

import static java_gaps.NumberUtils.add;
import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.habitat.auto._AffectationLocatif;

@Feminin
public class AffectationLocatif extends _AffectationLocatif implements
		AffHabitat, ComplexDeletion {

	private static final long serialVersionUID = -6182055582848815794L;

	public Integer getLoyerCC() {
		return add(getLoyerHC(), getCharges());
	}

	public void prepareForDeletion() {
		DeletionUtils.empty(this, PROBLEMES_PROPERTY);
	}

}
