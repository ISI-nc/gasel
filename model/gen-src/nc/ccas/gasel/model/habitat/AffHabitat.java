package nc.ccas.gasel.model.habitat;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.habitat.enums.AssuranceHabitation;
import nc.ccas.gasel.model.habitat.enums.TypeLogement;
import nc.ccas.gasel.model.mairie.Quartier;

@Feminin
public interface AffHabitat {

	public TypeLogement getType();

	public void setType(TypeLogement type);

	public Quartier getQuartier();

	public void setQuartier(Quartier quartier);

	public AssuranceHabitation getAssuranceHabitation();

	public void setAssuranceHabitation(AssuranceHabitation assurance);

	public Integer getMontantAssuranceHabitation();

	public void setMontantAssuranceHabitation(Integer montant);

}
